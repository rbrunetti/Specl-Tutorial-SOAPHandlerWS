
package it.polimi.bookstore.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getBooksNumberPerAuthorResponse", namespace = "http://ws.bookstore.polimi.it/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBooksNumberPerAuthorResponse", namespace = "http://ws.bookstore.polimi.it/")
public class GetBooksNumberPerAuthorResponse {

    @XmlElement(name = "return", namespace = "")
    private it.polimi.bookstore.ws.book.FastHashMap _return;

    /**
     * 
     * @return
     *     returns FastHashMap
     */
    public it.polimi.bookstore.ws.book.FastHashMap getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(it.polimi.bookstore.ws.book.FastHashMap _return) {
        this._return = _return;
    }

}
