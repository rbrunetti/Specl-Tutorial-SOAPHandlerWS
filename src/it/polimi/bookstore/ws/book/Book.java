package it.polimi.bookstore.ws.book;


public class Book {

	private Double year;
	private String title;
	private String author;
	private String coauthor;
	private String publisher;
	private String isbn;
	private Double price;
	
	public Book(Double year, String title, String author, String coauthor, String publisher, String isbn, Double price) {
		this.year = year;
		this.title = title;
		this.author = author;
		this.coauthor = coauthor;
		this.publisher = publisher;
		this.isbn = isbn;
		this.price = price;
	}

	public Double getYear() {
		return year;
	}

	public void setYear(Double year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCoauthor() {
		return coauthor;
	}

	public void setCoauthor(String coauthor) {
		this.coauthor = coauthor;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	
}
