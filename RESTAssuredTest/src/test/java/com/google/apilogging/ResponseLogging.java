package com.google.apilogging;

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
import io.restassured.response.Response;

public class ResponseLogging {
	private static Logger log=LogManager.getLogger(ResponseLogging.class.getName());
	Properties prop=new Properties();
	@BeforeTest
	public void getData() throws IOException
	{
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
	}

	  @Test
	  public void logAllTestResult ()  {
		log.info("Log all the test results");
		RestAssured.baseURI= prop.getProperty("HOST");
	    	given ()
	    	.param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .log().all();
	    //System.out.println ("Extracting a string from the response"+statusCode);
	   //Assert.assertEquals(statusCode.toString(), "200");
	  }
	  
	  @Test
	  public void logBodyTestResult ()  {
		log.info("Log Body of the test result");
		RestAssured.baseURI= prop.getProperty("HOST");
	    	given ()
	    	.param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .log().body();
	  }

	  
	  @Test
	  public void logHeadersTestResult ()  {
		log.info("Log Headers of the test result");
		RestAssured.baseURI= prop.getProperty("HOST");
	    	given ()
	    	.param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .log().headers();
	  }
	  
	  @Test
	  public void logCookiesTestResult ()  {
		log.info("Log Cookies of the test result");
		RestAssured.baseURI= prop.getProperty("HOST");
	    	given ()
	    	.param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .log().cookies();
	  }
	  
	  @Test
	  public void logStatusLineTestResult ()  {
		log.info("Log Cookies of the test result");
		RestAssured.baseURI= prop.getProperty("HOST");
	    	given ()
	    	.param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .log().status();
	  }
	 
	 
	  @Test
	  public void logTestResultValidationFails ()  {
		log.info("Log if validation fails for the test result");
		RestAssured.baseURI= prop.getProperty("HOST");
	    	given ()
	    	.param ("query", "Churchgate Station")
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .log().ifValidationFails();
	  }

}
