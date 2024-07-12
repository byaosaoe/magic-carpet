package com.bya.magic_carpet.books;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class BooksDto {

	@NotNull
	private String query;
	
	@Max(value=100)
	private int display = 10;
	
	@Max(value=1000)
	private int start = 1;
	
	private String sort = "sim";
	
	public String getQuery() {
		return query;
	}
	
	public int getDisplay() {
		return display;
	}
	
	public int getStart() {
		return start;
	}
	
	public String getSort() {
		return sort;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setDisplay(int display) {
		this.display = display;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "BooksDto [query=" + query + ", display=" + display + ", start=" + start + ", sort=" + sort + "]";
	}
}
