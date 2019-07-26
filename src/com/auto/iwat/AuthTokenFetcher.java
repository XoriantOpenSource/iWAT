package com.auto.iwat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Base64;

public class AuthTokenFetcher {

	public static String authTokenFetcher(String url,String username, String password , String tenant) throws Exception {
	 	HttpPost postRequest = new HttpPost(url);	 	
	 	DefaultHttpClient httpClient = new DefaultHttpClient();	 	
	 	String userPw = username + ":" + password;
	 	byte[] encodedBytes = Base64.getEncoder().encode(userPw.getBytes());
	 	userPw = "Basic " + new String(encodedBytes);
	 	
	 	postRequest.addHeader("tenant",tenant);
	 	postRequest.setHeader(HttpHeaders.AUTHORIZATION,userPw);
	 	
	 	 HttpResponse response = httpClient.execute(postRequest);
	        String output;
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader((response.getEntity().getContent())));

	        	        
	        StringBuffer totalOutput = new StringBuffer();
	        //System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	            //System.out.println(output);
	            totalOutput.append(output);
	        }
	        //System.out.println("Output is as follows : \n" + totalOutput.toString());
	 	
	        
	        //Storing the  authtoken file
	        
	        
			ObjectNode json = new ObjectMapper().readValue(totalOutput.toString(),
					ObjectNode.class);
			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(new File("authToken.json"),
					json);
	 	
	 String authToken = JsonParser.getJsonPathResult("authToken.json", "$.access_token");
	 String tokenType = "Bearer";
	 
	 return (tokenType + " " + authToken.replaceAll("^\"|\"$", ""));

	}

}
