package com.bya.magic_carpet.books;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BooksService {

	@Value("${naver.api.id}")
	private String id;
	
	@Value("${naver.api.key}")
	private String key;
	
	private final RestClient restClient = RestClient.create();
	private static final String BASE_URI = "https://openapi.naver.com/v1/search/";
	private final XPath xpath = XPathFactory.newInstance().newXPath();
	
	private String getXmlElement(Document xml, String element) throws XPathExpressionException {
		return (String)xpath.evaluate("//rss//channel//item//"+element, xml, XPathConstants.STRING);
	}
	
	public BookDetailsDto retrieveBookByIsbn(String b_isbn) throws  XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		
		String uri = BASE_URI
				+ "book_adv.xml"
				+ "?d_isbn=" + b_isbn;

		String result = restClient.get()
				.uri(uri)
				.accept(MediaType.APPLICATION_XML)
				.header("X-Naver-Client-Id", id)
				.header("X-Naver-Client-Secret", key)
				.retrieve()
				.toEntity(String.class)
				.getBody();
		
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(result.getBytes()));
		xml.getDocumentElement().normalize();
		
		String title = getXmlElement(xml, "title");
		String link = getXmlElement(xml, "link");
		String image = getXmlElement(xml, "image");
		String author = getXmlElement(xml, "author");
		String price = getXmlElement(xml, "price");
		String discount = getXmlElement(xml, "discount");
		String publisher = getXmlElement(xml, "publisher");
		String pubdate = getXmlElement(xml, "pubdate");
		String isbn = getXmlElement(xml, "isbn");
		String description = getXmlElement(xml, "description");

		return BookDetailsDto.builder()
				.title(title)
				.link(link)
				.image(image)
				.author(author)
				.price(price)
				.discount(discount)
				.publisher(publisher)
				.pubdate(pubdate)
				.isbn(isbn)
				.description(description)
				.build();
	}
	
	
	public JsonNode retrieveBooksBySearch(BooksDto booksDto) throws UnsupportedEncodingException {
		String uri = BASE_URI
				+ "book.json"
				+ "?query=" + encode(booksDto.getQuery())
				+ "&display=" + booksDto.getDisplay()
				+ "&start=" + booksDto.getStart()
				+ "&sort=" + booksDto.getSort();
		
        // 검색 결과로 총 검색 결과 수, 검색 날짜 등의 데이터가 반환되는데 그 중 책 정보가 담긴 [items] 리스트만을 반환
		return RestClient.create()
				.get()
				.uri(uri)
				.accept(MediaType.APPLICATION_JSON)
				.header("X-Naver-Client-Id", id)
				.header("X-Naver-Client-Secret", key)
				.retrieve()
				.toEntity(JsonNode.class)
				.getBody();
				//.get("items");
	}
	
	private String encode(String s) throws UnsupportedEncodingException {
		return new String(s.getBytes("UTF-8"));
	}
}
