
package it.polimi.bookstore.ws.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getAllBooksTitleResponse", namespace = "http://ws.bookstore.polimi.it/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllBooksTitleResponse", namespace = "http://ws.bookstore.polimi.it/")
public class GetAllBooksTitleResponse {

    @XmlElement(name = "return", namespace = "")
    private List<String> _return;

    /**
     * 
     * @return
     *     returns List<String>
     */
    public List<String> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(List<String> _return) {
        this._return = _return;
    }

}
