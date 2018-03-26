package com.google.apis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import TestFramework.basics3;

import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetJsonResponseTest {

	private static Logger log=LogManager.getLogger(GetJsonResponseTest.class.getName());
	Properties prop=new Properties();
	@BeforeTest
	public void getData() throws IOException
	{
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
	}
  @Test
  public void test01()  {
	log.info("Host information"+prop.getProperty("HOST"));
	RestAssured.baseURI= prop.getProperty("HOST");
    Response res  =given ().param ("query", "Churchgate Station")
    	.queryParam("key",prop.getProperty("KEY"))
    .when()
    .get ("/maps/api/place/textsearch/xml")
    .then()
    .contentType(ContentType.XML)
    .extract().response();
    System.out.println (res.asString ());
  }

}