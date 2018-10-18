package com.auto.iwat;
import java.util.Iterator;


import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;



/**
 * 
 */

/**
 * @author admin
 *
 */
public class UniversalNamespaceResolver implements NamespaceContext {

	// the delegate
    private Document sourceDocument;
 
    /**
     * This constructor stores the source document to search the namespaces in
     * it.
     * 
     * @param document
     *            source document
     */
    public UniversalNamespaceResolver(Document document) {
        sourceDocument = document;
    }
 
    /**
     * The lookup for the namespace uris is delegated to the stored document.
     * 
     * @param prefix
     *            to search for
     * @return uri
     */
    public String getNamespaceURI(String prefix) {
        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
        	System.out.println(sourceDocument.lookupNamespaceURI(null) + "No prefix");
            return sourceDocument.lookupNamespaceURI(null);
        } else {
        	System.out.println(sourceDocument.lookupNamespaceURI(prefix) + " prefix  == "+ XMLConstants.DEFAULT_NS_PREFIX);
            return sourceDocument.lookupNamespaceURI(prefix);
        }
    }
 
    /**
     * This method is not needed in this context, but can be implemented in a
     * similar way.
     */
    public String getPrefix(String namespaceURI) {
    	System.out.println(sourceDocument.lookupPrefix(namespaceURI) + "NameSpace");
        return sourceDocument.lookupPrefix(namespaceURI);
    }
 
    public Iterator getPrefixes(String namespaceURI) {
        // not implemented yet
        return null;
    }

}
