package com.bya.magic_carpet.books;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;

@RequestMapping("/api/v1/books")
@RestController
public class BooksController {

	@Autowired
	private BooksService booksService;
	
	@GetMapping()
	public ResponseEntity<?> searchByQuery(BooksDto booksDto) {
		try {
			JsonNode result = booksService.retrieveBooksBySearch(booksDto);
			return ResponseEntity.ok(result);
		} catch(UnsupportedEncodingException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/details")
	public ResponseEntity<?> searchByIsbn(@RequestParam("isbn") String isbn) {
		try {
			BookDetailsDto bookDetailsDto = booksService.retrieveBookByIsbn(isbn);
			return ResponseEntity.ok(bookDetailsDto);
		} catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
