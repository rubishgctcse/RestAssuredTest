package com.google.apis;

import static io.restassured.RestAssured.given;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class GetRequestTest {

	private static Logger log=LogManager.getLogger(GetRequestTest.class.getName());
	Properties prop=new Properties();
	@BeforeTest
	public void getData() throws IOException
	{
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
	}

	  @Test
	  //Extracting a string from the response
	  public void getTestResult ()  {
		log.info("Host information"+prop.getProperty("HOST"));
		RestAssured.baseURI= prop.getProperty("HOST");
	    String res  = given ().param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .contentType(ContentType.JSON)
	    .extract()
	    .path ("results[0].formatted_address");
	    //.response();
	    System.out.println ("Extracting a string from the response");
	   Assert.assertEquals (res, "Churchgate, Mumbai, Maharashtra 400020, India");
	  }
	  
	  @Test
	  //Measuring Response StatusLine
	  public void getStatusLine()
	  {
	  	RestAssured.baseURI = prop.getProperty("HOST");
	  	Response response  = given().param ("query", "Churchgate Station")
		    	.queryParam("key",prop.getProperty("KEY"))
		    .when()
		    .get ("/maps/api/place/textsearch/json");
	  	// Get the status line from the Response and store it in a variable called statusLine
	  	String statusLine = response.getStatusLine();
	  	System.out.println ("Measuring Response StatusLine");
	  	Assert.assertEquals(statusLine /*actual value*/, "HTTP/1.1 200 OK" /*expected value*/, "Correct status code returned");
	  }
	  
	  @Test
	  //Measuring Response Time
	  public void getTime()
	  {
	  	RestAssured.baseURI = prop.getProperty("HOST");
	  	Response response  = given().param ("query", "Churchgate Station")
		    	.queryParam("key",prop.getProperty("KEY"))
		    .when()
		    .get ("/maps/api/place/textsearch/json");
	  	// Get the status line from the Response and store it in a variable called statusLine
	  	long time = response.getTime();
	  	System.out.println("Measuring Response Time");
	  	Assert.assertTrue(time < 2000 /*expected value*/);
	  }
	  
	  @Test
	  //Validating the Requested Header
	  public void validateRequestHeader()
	  {
	  	RestAssured.baseURI = prop.getProperty("HOST");
	  	String response  = given().param ("query", "Churchgate Station")
		    	.queryParam("key",prop.getProperty("KEY"))
		    .when()
		    .get ("/maps/api/place/textsearch/json")
		    .header("Content-Type");
	    System.out.println("Validating the Request Headers");
	  	Assert.assertEquals(response, "application/json; charset=UTF-8");
	  }
	  
	  @Test
	  //List all the Headers
	  public void getRequestHeaders()
	  {
	  	RestAssured.baseURI = prop.getProperty("HOST");
	  	Headers headers  = given().param ("query", "Churchgate Station")
		    	.queryParam("key",prop.getProperty("KEY"))
		    .when()
		    .get ("/maps/api/place/textsearch/json")
		    .headers();
	
	    System.out.println("Validating the Request Headers");
	    String response = null;
	    for(Header header : headers)
		{
			System.out.println("Key: " + header.getName() + " Value: " + header.getValue());
			if(header.getName().toString().equals("Content-Type")) {
				response = header.getValue().toString();
			}
		} 
	  	Assert.assertEquals(response, "application/json; charset=UTF-8");
	  }
	}
