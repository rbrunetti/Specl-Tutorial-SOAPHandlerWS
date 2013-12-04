package it.polimi.bookstore.handler;

import it.polimi.specl.SpeclAnalyzer;
import it.polimi.specl.helpers.SpeclException;

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

			SpeclAnalyzer analyzer = new SpeclAnalyzer();
			
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
					} catch (SpeclException e) {
						generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
					}
				} else if(name.equals("getBookByIsbn")) {
					String assertion = "let $isbn = /Envelope/Body/getBookByIsbn/arg0; $isbn.length() == 10;";
					try {
						if(!analyzer.evaluate(assertion)) {
							generateSOAPErrMessage(soapMsg, "Wrong ISBN format");
						}
					} catch (SpeclException e) {
						generateSOAPErrMessage(soapMsg, "Server could not respond due to validation errors in the server side SOAPHandler.");
					}
				} else if(name.equals("getBooksByIsbnList")) {
					String assertion = "let $isbns = /Envelope/Body/getBooksByIsbnList/arg0; forall($isbn in $isbns, $isbn.length() == 10);";
					try {
						if(!analyzer.evaluate(assertion)) {
							generateSOAPErrMessage(soapMsg, "Wrong ISBN format");
						}
					} catch (SpeclException e) {
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
					} catch (SpeclException e) {
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
