package com.auto.iwat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor.Uri;
/**
 * This class sends all the soap and rest request depending upon their method types.
 * @author Indroneel Sengupta
 *
 */
public class HttpHandler {
	/**
	 * This method is used for get & Post of soap request
	 * @author Indroneel Sengupta
	 * @param url1 URL in which to hit the soap request
	 * @param xmlFileToHit Path of the file to be hit on to the server
	 * @param xmlResponse Path of the response file where user wants to store the response
	 * @param reqMethod Specific type of the method
	 * @param Specific file type(eg : xml/plain , json/application)
	 * @return Returns a hashmap with key as response code and value as response message
	 */
	public HashMap<Integer, String> getResponse(String url1, String xmlFileToHit, String xmlResponse,String reqMethod,String fileType) {
		HashMap<Integer, String> response = new HashMap<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		
		try {

			PrintStream out = System.out;
			Properties props = PropertyFile.propertyFile();
			String soapAction = "";
			if ((null != props.getProperty("soapActionUrl")))
				soapAction = (props.getProperty("soapActionUrl"));

			// Reading the SOAP request message from a file
			File objFile = new File(xmlFileToHit);
			int reqLen = (int) objFile.length();
			byte[] reqBytes = new byte[reqLen];
			FileInputStream inStream = new FileInputStream(objFile);
			
			String s = new String(reqBytes);
			//System.out.println("ReqBytes = "+s);
			
			inStream.read(reqBytes);
			inStream.close();

			// Creating the HttpURLConnection object
			URL oURL = new URL(url1);
			HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
			//System.out.println("Request Method = " + reqMethod);
			con.setRequestMethod(reqMethod);
			con.setRequestProperty("Content-Type", fileType+"; charset=utf-8");
			//System.out.println("property = " + props.getProperty("soapActionUrl"));
			con.setRequestProperty("SOAPAction", soapAction);
			con.setDoOutput(true);
			con.setDoInput(true);
			
			s = new String(reqBytes);
			//System.out.println("ReqBytes = "+s);

			// Posting the SOAP request XML message
			OutputStream reqStream = con.getOutputStream();
			reqStream.write(reqBytes);
			reqStream.flush();

			// Reading the SOAP response XML message

			if (con.getResponseCode() == 200) {
				byte[] byteBuf = new byte[1024];
				FileOutputStream outStream = new FileOutputStream(xmlResponse);
				InputStream resStream = con.getInputStream();
				int resLen = 0;
				int len = resStream.read(byteBuf);
				// System.out.println("Length " + len);
				while (len > -1) {
					resLen += len;
					outStream.write(byteBuf, 0, len);
					len = resStream.read(byteBuf);
				}
				outStream.close();
				reqStream.close();
				resStream.close();							

			} else {
				byte[] byteBuf = new byte[1024];
				FileOutputStream outStream = new FileOutputStream(xmlResponse);
				InputStream resStream = con.getErrorStream();
				int resLen = 0;
				int len = resStream.read(byteBuf);
				while (len > -1){
					resLen += len;
					outStream.write(byteBuf, 0, len);
					len = resStream.read(byteBuf);
				}
				outStream.close();
				reqStream.close();
				resStream.close();
				
				builder = factory.newDocumentBuilder();
				//doc = builder.parse(xmlResponse);
				//System.out.println(xmlResponse);
				doc = builder.parse(new InputSource(xmlResponse));
				
				
				String resStr = prettyPrint(doc);
				File file = new File(xmlResponse);
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(resStr);
				fileWriter.flush();
				fileWriter.close();
			}

			response.put(con.getResponseCode(), con.getResponseMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * This method is used to POST the rest request
	 * @author Indroneel Sengupta
	 * @param url Provide the url to which user wants to hit the rest request
	 * @param message The request data to be sent to the server (Eg: Json Content)
	 * @param httpClient DefaultHttpClient Object
	 * @return Returns Hashmap<Integer,String> with reponse code and response data
	 * @throws Exception 
	 */
	 public static HashMap<Integer,String> postToURL(String url, String message, DefaultHttpClient httpClient,String authToken) throws Exception {
		  	Properties props = PropertyFile.propertyFile();
		 	String authTokenUrl = props.getProperty("authTokenUrl");
		 	String username = props.getProperty("username");
		 	String password = props.getProperty("password");
		 	String tenant = props.getProperty("tenant");
		 
		 	HttpPost postRequest = new HttpPost(url);
	        HashMap<Integer,String> responseData = new HashMap<>();
	        StringEntity input = new StringEntity(message);
	        input.setContentType("application/json");
	        postRequest.setEntity(input);
	        
	        
	        System.out.println(" * Method Type =  POST" );
	        if(authToken.equalsIgnoreCase("NoAuth")) {
	        	System.out.println(" * Is Authorised  = No");
	        }else {
	        	System.out.println(" * Is Authorised  = Yes");
	        	HttpAuthorization.httpAuthoriazation(postRequest, AuthTokenFetcher.authTokenFetcher(authTokenUrl, username, password,tenant));
	        }
	 
	        HttpResponse response = httpClient.execute(postRequest);
	        String output;
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader((response.getEntity().getContent())));

	        	        
	        StringBuffer totalOutput = new StringBuffer();
	       // System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	           // System.out.println(output);
	            totalOutput.append(output);
	        }
	        responseData.put(response.getStatusLine().getStatusCode(), totalOutput.toString());
	        return responseData;
	 }
	 
	 /**
	  * This method is used for GET method of the rest request
	  * @author Indroneel Sengupta
	  * @param url Pass the Url
	  * @param httpClient DefaultHttpClient Object
	  * @return Returns Hashmap<Integer,String> with reponse code and response data
	  * @throws IOException
	  * @throws IllegalStateException
	  * @throws UnsupportedEncodingException
	  * @throws RuntimeException
	  * @throws URISyntaxException
	  */	 
	 public static HashMap<Integer,String> getToURL(String url, DefaultHttpClient httpClient, String authToken) throws IOException, IllegalStateException, UnsupportedEncodingException, RuntimeException, URISyntaxException {
		 	String myUrl = url;
		 	URI myURI = new URI(myUrl);
		 	HttpGet getRequest = new HttpGet(myURI);
	        HashMap<Integer,String> responseData = new HashMap<>();
	        getRequest.setURI(myURI);
	        if(authToken.equalsIgnoreCase("NoAuth")) {
	        	System.out.println(" * Is Authorised  = No");
	        }else {
	        	System.out.println(" * Is Authorised  = Yes");
	        	HttpAuthorization.httpAuthoriazation(getRequest, authToken);
	        }
	        HttpResponse response = httpClient.execute(getRequest);
	        //System.out.println("Response = "+ response.getEntity().getContent().toString());
	        System.out.println(" * Method Type =  GET" );
	        String output;
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader((response.getEntity().getContent())));

	        	        
	        StringBuffer totalOutput = new StringBuffer();
	       // System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	           // System.out.println(output+"\n");
	            totalOutput.append(output);
	        }
	        responseData.put(response.getStatusLine().getStatusCode(), totalOutput.toString());
	        return responseData;
	 }
	 
	 /**
		 * This method is used for PUT method of the rest request
		 * @author Indroneel Sengupta
		 * @param url Provide the url to which user wants to hit the rest request
		 * @param message The request data to be sent to the server (Eg: Json Content)
		 * @param httpClient DefaultHttpClient Object
		 * @return Returns Hashmap<Integer,String> with reponse code and response data
		 * @throws IOException
		 * @throws IllegalStateException
		 * @throws UnsupportedEncodingException
		 * @throws RuntimeException
		 */
	 public static HashMap<Integer,String> putToURL(String url, String message, DefaultHttpClient httpClient,String authToken) throws IOException, IllegalStateException, UnsupportedEncodingException, RuntimeException {
         
		 	HttpPut putRequest = new HttpPut(url);
	        HashMap<Integer,String> responseData = new HashMap<>();
	        StringEntity input = new StringEntity(message);
	        input.setContentType("application/json");
	        putRequest.setEntity(input);
	       // putRequest.getHeaders(HttpHeaders.AUTHORIZATION);
	        System.out.println(" * Method Type =  PUT" );
	        if(authToken.equalsIgnoreCase("NoAuth")) {
	        	System.out.println(" * Is Authorised  = No");
	        }else {
	        	System.out.println(" * Is Authorised  = Yes");
	        	HttpAuthorization.httpAuthoriazation(putRequest, authToken);
	        }
	        
	        HttpResponse response = httpClient.execute(putRequest);
	        String output;
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader((response.getEntity().getContent())));

	        	        
	        StringBuffer totalOutput = new StringBuffer();
	       // System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	           // System.out.println(output);
	            totalOutput.append(output);
	        }
	        responseData.put(response.getStatusLine().getStatusCode(), totalOutput.toString());
	        return responseData;
	 }
	 

	 /**
	  * This method is used for DELETE method of the rest request
	  * @author Indroneel Sengupta
	  * @param url Pass the Url
	  * @param httpClient DefaultHttpClient Object
	  * @return Returns Hashmap<Integer,String> with reponse code and response data
	  * @throws IOException
	  * @throws IllegalStateException
	  * @throws UnsupportedEncodingException
	  * @throws RuntimeException
	  * @throws URISyntaxException
	  */
	 public static HashMap<Integer,String> deleteToURL(String url, DefaultHttpClient httpClient, String authToken) throws IOException, IllegalStateException, UnsupportedEncodingException, RuntimeException {
         
		 	HttpDelete deleteRequest = new HttpDelete(url);
	        HashMap<Integer,String> responseData = new HashMap<>();
	        
	        System.out.println(" * Method Type =  DELETE" );
	        
	        StringBuffer totalOutput = new StringBuffer();
	        if(authToken.equalsIgnoreCase("NoAuth")) {
	        	System.out.println(" * Is Authorised  = No");
	        }else {
	        	System.out.println(" * Is Authorised  = Yes");
	        	HttpAuthorization.httpAuthoriazation(deleteRequest, authToken);
	        }
	        HttpResponse response = httpClient.execute(deleteRequest);
	        String output;
	        if(response.getStatusLine().getStatusCode() != 204) {
	        	BufferedReader br = new BufferedReader(
		                new InputStreamReader((response.getEntity().getContent())));
	        	
		        	        
		        
		       // System.out.println("Output from Server .... \n");
		        while ((output = br.readLine()) != null) {
		           // System.out.println(output);
		            totalOutput.append(output);
		        }
	        }else {
	        	totalOutput = new StringBuffer(" {\"NoContent\" :  \"Content Not Found\" }");
	        }
	        
	        responseData.put(response.getStatusLine().getStatusCode(), totalOutput.toString());
	        return responseData;
	 }
	
	 
	 /**
	  * This method is used for proper formatting of the response
	  * @param xml Document object of the response
	  * @return Returns the formatted response
	  * @throws Exception
	  */
	public static final String  prettyPrint(Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		return out.toString();
	}

}
