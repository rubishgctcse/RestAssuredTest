package com.google.apis;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class GetXmlTest {
	private static Logger log = LogManager.getLogger(GetXmlTest.class.getName());
	  Properties prop=new Properties();
	  @BeforeTest
	  public void getData() throws IOException
		{
			FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
			prop.load(fis);
		}
	 
	  @Test
	  //Validate the output XML
	  public void testXmlOutput() {
		log.info("Host information"+prop.getProperty("HOST"));
		RestAssured.baseURI= prop.getProperty("HOST"); 
	     Response res=given ().param ("query", "Churchgate Station")
	          .queryParam("key",prop.getProperty("KEY"))
	          .when ()
	          .get ("/maps/api/place/textsearch/xml")
	          .then ()
	          .contentType(ContentType.XML)
	          .extract()
	          //.response();
	          .path("placesearchresponse.result[0].formatted_address");            
	   //System.out.println (res.asString ());
	   Assert.assertEquals (res, "Maharshi Karve Rd, Churchgate, Mumbai, Maharashtra 400020, India");  
	  }
	  
	  @Test
	  //Validate the output XPath  
	  public void testXpath() {
		log.info("Host information"+prop.getProperty("HOST"));
		RestAssured.baseURI= prop.getProperty("HOST"); 
	     Response res=given ().param ("query", "Churchgate Station")
	          .queryParam("key",prop.getProperty("KEY"))
	          .when ()
	          .get ("/maps/api/place/textsearch/xml")
	          .then ()
	          .contentType(ContentType.XML)
	          .extract()
	          .response();
	          //.path("placesearchresponse.result[0].formatted_address");            
	   System.out.println (res.asString ());
	   //body(hasXPath("/maps/place", containsString("Church"))
	   //body(hasXPath("/greeting/firstName[text()='John']")
	   
	   //Assert.assertEquals (res, "Maharshi Karve Rd, Churchgate, Mumbai, Maharashtra 400020, India");  
	  }
	  
	  @Test
	  //Validate the output Xml for lists  
	  public void validateXmlOutputForLists() {
		log.info("Host information"+prop.getProperty("HOST"));
		RestAssured.baseURI= prop.getProperty("HOST"); 
	     List<String> response=given ().param ("query", "Churchgate Station")
	          .queryParam("key",prop.getProperty("KEY"))
	          .when ()
	          .get ("/maps/api/place/textsearch/xml")
	          .then ()
	          .contentType(ContentType.XML)
	          .extract()
	          //.response();
	          .path("placesearchresponse.result.find { it.@formatted_address == 'Mumbai').item");            
	   System.out.println (response);
	   //Depth First Search ==== .body("**.find { it.@type == 'groceries' }", hasItems("Chocolate", "Coffee"));
	   //Assert.assertEquals (res, "Maharshi Karve Rd, Churchgate, Mumbai, Maharashtra 400020, India");  
	  }
	  
	}