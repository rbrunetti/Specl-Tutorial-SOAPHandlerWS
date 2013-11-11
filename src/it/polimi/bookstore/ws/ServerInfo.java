package it.polimi.bookstore.ws;


import it.polimi.bookstore.ws.book.FastHashMap;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServerInfo {

	@WebMethod
	public List<String> getBooksByAuthor(String author);
	
	@WebMethod
	public String getBookByIsbn(String isbn);
	
	@WebMethod
	public List<String> getBooksByIsbnList(List<String> isbnList);
	
	@WebMethod
	public List<String> getAllBooksTitle();
	
	@WebMethod
	public FastHashMap getBooksNumberPerAuthor();
	
}