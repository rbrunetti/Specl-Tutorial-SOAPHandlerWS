package it.polimi.bookstore.endpoint;

import it.polimi.bookstore.ws.ServerInfoImpl;

import javax.xml.ws.Endpoint;

public class WsPublisher {

	public static void main(String[] args) {
		   Endpoint.publish("http://localhost:8888/bookstorews/server", new ServerInfoImpl());
	 
		   System.out.println("BookStore WebService is published!");
	}
}
