package TestFramework;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jiraAPI.resources1;


public class basics7 {
	Properties prop=new Properties();
	@BeforeTest
	public void getData() throws IOException
	{
		
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
		
		//prop.get("HOST");
	}
	@Test
	public void JiraAPICreateComment()
	{
		//Creating Issue/Defect
		
		RestAssured.baseURI= "http://localhost:8080";
		Response res=given().header("Content-Type", "application/json").
		header("Cookie","JSESSIONID="+ReusableMethods.getSessionKEY()).
		body("{ \"body\": \"Inserting comment from the automation code\","+
    "\"visibility\": {"+
        "\"type\": \"role\","+
        "\"value\": \"Administrators\" }"+
"}").when().
		post(resources1.placePostData()).then().statusCode(201).extract().response();
		   JsonPath js= ReusableMethods.rawToJson(res);
		   String id=js.get("id");
		   System.out.println(id);
		
		
		
		
		
		
		
		
		
		
		
		  
		
				
		
		
		
		}
	
}
