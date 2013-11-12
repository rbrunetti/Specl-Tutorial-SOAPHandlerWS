WSCoL-Tutorial-SOAPHandler-Server
=================================

WSCoL Analyzer Tutorial - SOAP Handler, Server Side project

The developed web service is called _BookstoreWS_, it also use an attached SOAP Handler for use WSCoL as pre-condition validator.

## Table of Content

1. [Setup](#example_handler_server_setup)
    1. [Needed Libraries](#example_handler_server_libs)
	2. [Installation](#example_handler_server_install)
2. [WS](#example_handler_server_wsclient)
3. [Server SOAP Handler](#example_handler_server_handler)
4. [Server SOAPHandler XML File](#example_handler_server_handler_xml)
5. [Attach Handler to BookstoreWS](#example_handler_server_handler_attach)
6. [BookstoreWS Project Organization](#example_handler_server_proj_dir_struct)
7. [Source](#example_handler_sources)

<a name="example_handler_server"></a>
## Server Side
<a name="example_handler_server_setup"></a>
### Setup
Download the required libraries, import them in your project and start coding.<br/>

<a name="example_handler_server_libs"></a>
#### Needed Libraries

* WSCoL-Analyzer.jar ([download]())
* WSCoL.jar (required by _WSCoL-Analyzer.jar_) ([download]())
* json-simple-1.1.1.jar (required by _WSCoL-Analyzer.jar_) ([download]())

Full Zip ([download]())

<a name="example_handler_server_install"></a>
#### Installation
Simply save libraries in a known folder inside your project and import them.<br/>
The procedure in Eclipse IDE is<br/>
![Import Libs](img/soap/00-ImportLibs.png)<br/>
_Import Libraries_

<a name="example_handler_server_ws"></a>
### Web Service

A simple WS, offering informations on books.<br/>
Methods are the following, the names are self-explanatory:

* `getBooksByAuthor(String)`
* `getBookByIsbn(String)`
* `getBooksByIsbnList(List<String>)`
* `getAllBooksTitle()`
* `getBooksNumberPerAuthor()`
* `getBooksByPublisherAndYearRange()`

_File: ServerInfo.java_
```Java
package it.polimi.bookstore.ws;

import it.polimi.bookstore.ws.book.HashMapWrapper;

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
	public HashMapWrapper getBooksNumberPerAuthor();
	
	@WebMethod
	public List<String> getBooksByPublisherAndYearRange(String publisher, int startYear, int endYear);
	
}
```

_File: ServerInfoImpl.java_
```Java
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

	@Override
	public List<String> getBooksByPublisherAndYearRange(String publisher, int startYear, int endYear) {
		if(books == null)
			setup();
		List<String> result = new ArrayList<>();
		for(Book b:books.getBookstore()) {
			if(b.getPublisher().equals(publisher) && b.getYear() >= startYear && b.getYear() <= endYear){
				result.add(b.getTitle());
			}
		}
		return result;
	}	
}
```

Note that `setup()` method is used for generate and fill a library of class `Bookstore`, this and `Book` classes are general POJO used for this tutorial.

_File: Book.java_
```Java
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
```

_File: Bookstore.java_
```Java
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

```

Then we declare a web service endpoint for the service which will publish outside the service for user access.<br/>
_File: WsPublisher.java_
```Java
package it.polimi.bookstore.endpoint;

import it.polimi.bookstore.ws.ServerInfoImpl;

import javax.xml.ws.Endpoint;

public class WsPublisher {

    public static void main(String[] args) {
		   Endpoint.publish("http://localhost:8888/bookstorews/server", new ServerInfoImpl());
	 
		   System.out.println("BookStore WebService is published!");
	}
}
```

Finally we generate necessary Java files for the web service deployment with `wsgen` command from the bin folder of the project
```
$ wsgen -keep -verbose -cp . it.polimi.bookstore.ws.ServerInfoImpl
```
This will create two files for each method of the service.

<a name="example_handler_server_handler"></a>
### Server SOAP Handler
The server side SOAP Handler we're going to build it's going to use WSCoL for checking constraints on the parameters passed to BookstoreService from service clients, for every incoming message.

We declare and initialize the analyzer with `WSCoLAnalyzer analyzer = new WSCoLAnalyzer();`, then we obtain the XML Document of the SOAP Message body and pass it to the analyzer that will use it as input file and parse it as an [SDO](#sdo) (`analyzer.setXMLInput(soapBody.getOwnerDocument());`).

```Java
//...
@Override
    public boolean handleMessage(SOAPMessageContext context) {

		System.out.println("Server : handleMessage()......");

		Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (!isResponse) {

			WSCoLAnalyzer analyzer = new WSCoLAnalyzer();
			
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPBody soapBody = soapMsg.getSOAPBody();
				
				// the analyzer parse the body of the SOAP Msg to an SDO 
				analyzer.setXMLInput(soapBody.getOwnerDocument());
				
				Node requestNode = soapBody.getFirstChild();
				String name = requestNode.getLocalName();
                
//...
```

At this point we check `name` (corresponding to the called method) and we evaluate WSCoL assertions on the passed arguments (see comments), we are checking pre-conditions.

```Java
//...

if (name.equals("getBooksByAuthor")) {
    // we assert that the argument could not be empty
    String assertion = "let $author = /Envelope/Body/getBooksByAuthor/arg0; $author.cardinality() == 0;";
	try {
    	if(analyzer.evaluate(assertion)) {
		    generateSOAPErrMessage(soapMsg, "Author field could not be empty.");
        }
    } catch (WSCoLException e) { // this catch errors in the assertion, but responde to client a generic message
    	generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
    }
} else if(name.equals("getBookByIsbn")) {
    // assert that the isbn argument must be long 10 characters
    String assertion = "let $isbn = /Envelope/Body/getBookByIsbn/arg0; $isbn.length() == 10;";
	try {
    	if(!analyzer.evaluate(assertion)) {
    		generateSOAPErrMessage(soapMsg, "Wrong ISBN format");
		}
    } catch (WSCoLException e) {
    	generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
    }
} else if(name.equals("getBooksByIsbnList")) {
    // assert that each isbn in the list must be long 10 characters
    String assertion = "let $isbns = /Envelope/Body/getBooksByIsbnList/arg0; forall($isbn in $isbns, $isbn.length() == 10);";
	try {
    	if(!analyzer.evaluate(assertion)) {
    		generateSOAPErrMessage(soapMsg, "Wrong ISBN format");
		}
	} catch (WSCoLException e) {
    	generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
	}
} else if(name.equals("getBooksByPublisherAndYearRange")) {
	String assertion = "let $publisher = /Envelope/Body/getBooksByPublisherAndYearRange/arg0;"
			 						 + "let $startYear = /Envelope/Body/getBooksByPublisherAndYearRange/arg1;"
									 + "let $endYear = /Envelope/Body/getBooksByPublisherAndYearRange/arg2;"
									 + "$publisher.cardinality() != 0 && $startYear > 1900 && $endYear <= 2013;";
	try {
		if(!analyzer.evaluate(assertion)){
			generateSOAPErrMessage(soapMsg, "Passed arguments are wrong (incorrect year or empty publisher)");
		}
	} catch (WSCoLException e) {
		generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
	}
} else if(name.equals("getAllBooksTitle") || name.equals("getBooksNumberPerAuthor")){
    // nothing to do here, no arguments
} else {
    generateSOAPErrMessage(soapMsg, "Wrong operation.");
}

//...
```

Here's the complete code of the handler.<br/>
_File: ValidationHandler.java_
```Java
package it.polimi.bookstore.handler;

import it.polimi.wscol.WSCoLAnalyzer;
import it.polimi.wscol.helpers.WSCoLException;

import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Node;

public class ValidationHandler implements SOAPHandler<SOAPMessageContext> {
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		System.out.println("Server : handleMessage()......");

		Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (!isResponse) {

			WSCoLAnalyzer analyzer = new WSCoLAnalyzer();
			
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPBody soapBody = soapMsg.getSOAPBody();
				
				// the analyzer parse the body of the SOAP Msg to an SDO 
				analyzer.setXMLInput(soapBody.getOwnerDocument());
				
				Node requestNode = soapBody.getFirstChild();
				String name = requestNode.getLocalName();
				
				if (name.equals("getBooksByAuthor")) {
					String assertion = "let $author = /Envelope/Body/getBooksByAuthor/arg0; $author.cardinality() == 0;";
					try {
						if(analyzer.evaluate(assertion)) {
							generateSOAPErrMessage(soapMsg, "Author field could not be empty.");
						}
					} catch (WSCoLException e) {
						generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
					}
				} else if(name.equals("getBookByIsbn")) {
					String assertion = "let $isbn = /Envelope/Body/getBookByIsbn/arg0; $isbn.length() == 10;";
					try {
						if(!analyzer.evaluate(assertion)) {
							generateSOAPErrMessage(soapMsg, "Wrong ISBN format");
						}
					} catch (WSCoLException e) {
						generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
					}
				} else if(name.equals("getBooksByIsbnList")) {
					String assertion = "let $isbns = /Envelope/Body/getBooksByIsbnList/arg0; forall($isbn in $isbns, $isbn.length() == 10);";
					try {
						if(!analyzer.evaluate(assertion)) {
							generateSOAPErrMessage(soapMsg, "Wrong ISBN format");
						}
					} catch (WSCoLException e) {
						generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
					}
				} else if(name.equals("getBooksByPublisherAndYearRange")) {
					String assertion = "let $publisher = /Envelope/Body/getBooksByPublisherAndYearRange/arg0;"
							+ "let $startYear = /Envelope/Body/getBooksByPublisherAndYearRange/arg1;"
							+ "let $endYear = /Envelope/Body/getBooksByPublisherAndYearRange/arg2;"
							+ "$publisher.cardinality() != 0 && $startYear > 1900 && $endYear <= 2013;";
					try {
						if(!analyzer.evaluate(assertion)){
							generateSOAPErrMessage(soapMsg, "Passed arguments are wrong (incorrect year or empty publisher)");
						}
					} catch (WSCoLException e) {
						generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
					}
				} else if(name.equals("getAllBooksTitle") || name.equals("getBooksNumberPerAuthor")){
					// nothing to do here
				} else {
					generateSOAPErrMessage(soapMsg, "Wrong operation.");
				}
				// tracking
				soapMsg.writeTo(System.out);
				System.out.println();

			} catch (SOAPException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}

		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("Server : handleFault()......");
		return true;
	}

	@Override
	public void close(MessageContext context) {
		System.out.println("Server : close()......");
	}

	@Override
	public Set<QName> getHeaders() {
		System.out.println("Server : getHeaders()......");
		return null;
	}

	private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
		try {
			SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
			SOAPFault soapFault = soapBody.addFault();
			soapFault.setFaultString(reason);
			throw new SOAPFaultException(soapFault);
		} catch (SOAPException e) {
		}
	}

}
```

<a name="example_handler_server_handler_xml"></a>
### SOAPHandler XML File
Create a SOAP handler XML file, and puts your SOAP handler declaration.

_File: handler-chain.xml_
```XML
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<javaee:handler-chains 
     xmlns:javaee="http://java.sun.com/xml/ns/javaee" 
     xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <javaee:handler-chain>
    <javaee:handler>
      <javaee:handler-class>it.polimi.bookstore.handler.ValidationHandler</javaee:handler-class>
    </javaee:handler>
  </javaee:handler-chain>
</javaee:handler-chains>
```

<a name="example_handler_server_handler_attach"></a>
#### Attach Handler to BookstoreWS
To attach above SOAP handler to web service ServerInfo.java, just annotate with @HandlerChain and specify the SOAP handler file name inside.

```Java
//...

@WebService
@HandlerChain(file = "handler-chain.xml")
public class ServerInfoImpl implements ServerInfo {

//...
```

<a name="example_handler_server_proj_dir_struct"></a>
### BookstoreWS Project Organization
Here's the directory structure of the project

![Directory Structure](/img/soap/01-DirStruct.png)
