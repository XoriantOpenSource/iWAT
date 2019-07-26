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
		
		//httpRequest.addHeader("tenant","RES_QA1");
		httpRequest.setHeader(HttpHeaders.AUTHORIZATION, authToken);
		httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				
		
//		if (authorizationHeader == null) {
//            System.out.println("Authorization Header should be provided");
//            return;
//        }
		
	}
}
