/**
 * 
 */
package com.auto.iwat;

import org.apache.http.HttpHeaders;
import org.apache.http.message.AbstractHttpMessage;

/**
 * @author Indroneel Sengupta
 *
 */
public class HttpAuthorization {
	public static void httpAuthoriazation(AbstractHttpMessage httpRequest , String authToken) {
		//String authorizationHeader = httpRequest.getHeaders(HttpHeaders.AUTHORIZATION).toString();
		
		httpRequest.setHeader(HttpHeaders.AUTHORIZATION, authToken);
		
		
		
//		if (authorizationHeader == null) {
//            System.out.println("Authorization Header should be provided");
//            return;
//        }
		
	}
}
