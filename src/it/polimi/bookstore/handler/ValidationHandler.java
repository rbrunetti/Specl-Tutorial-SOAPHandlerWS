package it.polimi.bookstore.handler;

import it.polimi.wscol.WSCoLAnalyzer;
import it.polimi.wscol.Helpers.WSCoLException;

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

		WSCoLAnalyzer analyzer = new WSCoLAnalyzer();
		
		Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (!isResponse) {

			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPBody soapBody = soapMsg.getSOAPBody();
				
				analyzer.setXMLInput(soapBody.getOwnerDocument());
				
				Node requestNode = soapBody.getFirstChild();
				String name = requestNode.getLocalName();
//				Node evaluateNode = requestNode.getFirstChild().getNextSibling().getNextSibling().getFirstChild();
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
				} else if(name.equals("getAllBooksTitle") || name.equals("getBooksNumberPerAuthor")){
					
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

		} else {

			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPBody soapBody = soapMsg.getSOAPBody();
				Node responseNode = soapBody.getFirstChild();
				String name = responseNode.getLocalName();
				if (name.equals("evaluateResponse") || name.equals("checkAssertionResponse")) {
				
				}
				// tracking
				soapMsg.writeTo(System.out);

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
