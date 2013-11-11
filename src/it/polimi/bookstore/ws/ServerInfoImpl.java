package it.polimi.bookstore.ws;

import it.polimi.bookstore.ws.book.Book;
import it.polimi.bookstore.ws.book.Bookstore;
import it.polimi.bookstore.ws.book.HashMapWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.HandlerChain;
import javax.jws.WebService;

@WebService
@HandlerChain(file = "handler-chain.xml")
public class ServerInfoImpl implements ServerInfo {

	private Bookstore books;
	
	private void setup() {
		Book first = new Book((double)2000, "Snow Crash", "Neal Stephenson", null, "Spectra", "0553380958", (double)15);
		Book second = new Book((double)2005, "Burning Tower", "Larry Niven", "Jerry Pournelle", "Pocket", "0743416910", (double)6);
		Book third = new Book((double)1995, "Zodiac", "Neal Stephenson", null, "Spectra", "0871131811", (double)7.5);
		
		ArrayList<Book> list = new ArrayList<>();
		
		list.add(first);
		list.add(second);
		list.add(third);
		
		books = new Bookstore(list);
	}
	
	@Override
	public List<String> getBooksByAuthor(String author) {
		if(books == null)
			setup();
		ArrayList<String> result = new ArrayList<>();
		for(Book b:books.getBookstore()) {
			if(b.getAuthor().equals(author) || (b.getCoauthor() != null && b.getCoauthor().equals(author))) {
				result.add(b.getTitle());
			}
		}
		return result;
	}

	@Override
	public String getBookByIsbn(String isbn) {
		if(books == null)
			setup();
		for(Book b:books.getBookstore()){
			if(b.getIsbn().equals(isbn)){
				return b.getTitle();
			}
		}
		return "No Books Found";
	}

	@Override
	public List<String> getBooksByIsbnList(List<String> isbnList) {
		List<String> result = new ArrayList<String>();
		for(String isbn:isbnList) {
			String book = getBookByIsbn(isbn);
			if(book.equals("No Books Found")) {
				result.add(book + " with ISBN: " + isbn);
			} else {
				result.add(book);
			}
		}
		return result;
	}

	@Override
	public List<String> getAllBooksTitle() {
		if(books == null)
			setup();
		ArrayList<String> result = new ArrayList<>();
		for(Book b:books.getBookstore()) {
			result.add(b.getTitle());
		}
		return result;
	}

	@Override
	// Cannot return an HashMap (see http://stackoverflow.com/a/13787059)
	public HashMapWrapper getBooksNumberPerAuthor() {
		if(books == null)
			setup();
		Map<String, Integer> authors = new HashMap<String, Integer>();
		for(Book b:books.getBookstore()){
			if(!authors.containsKey(b.getAuthor())){
				authors.put(b.getAuthor(), 1);
			} else {
				authors.put(b.getAuthor(), authors.get(b.getAuthor())+1);
			}
			if(b.getCoauthor() != null){
				if(!authors.containsKey(b.getCoauthor())){
					authors.put(b.getCoauthor(), 1);
				} else {
					authors.put(b.getCoauthor(), authors.get(b.getCoauthor())+1);
				}
			}
		}
		return new HashMapWrapper(authors);
	}	


}