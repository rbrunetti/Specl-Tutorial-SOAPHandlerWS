package it.polimi.bookstore.ws.book;

import java.util.ArrayList;

public class Bookstore {
	
	private ArrayList<Book> bookstore;

	public Bookstore(ArrayList<Book> bookstore) {
		this.bookstore = bookstore;
	}

	public ArrayList<Book> getBookstore() {
		return bookstore;
	}

	public void setBookstore(ArrayList<Book> bookstore) {
		this.bookstore = bookstore;
	}
	
	public void addBook(Book book) {
		bookstore.add(book);
	}
	
	public Book getBook(int i) {
		return bookstore.get(i);
	}

}
