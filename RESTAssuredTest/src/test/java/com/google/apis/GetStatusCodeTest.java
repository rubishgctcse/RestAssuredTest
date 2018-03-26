package com.google.apis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class GetStatusCodeTest {
  private static Logger log = LogManager.getLogger(GetStatusCodeTest.class.getName());
  Properties prop=new Properties();
  @BeforeTest
  public void getData() throws IOException
	{
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
	}

  @Test
  public void testStatusCode () {
	log.info("Host information"+prop.getProperty("HOST"));
	RestAssured.baseURI= prop.getProperty("HOST");
    Response res = 
    given ()
    .param ("query", "restaurants in mumbai")
    .queryParam("key",prop.getProperty("KEY"))
    .when()
    .get ("/maps/api/place/textsearch/json");

    Assert.assertEquals (res.statusCode (), 200);
  }

@Test
public void testStatusCodeRestAssured () {

given ().param ("query", "restaurants in mumbai")
        .queryParam("key",prop.getProperty("KEY"))
        .when()
        .get ("/maps/api/place/textsearch/json")
        .then ()
        .assertThat ().statusCode (200);

}
}