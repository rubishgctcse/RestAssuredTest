package com.google.apiauthentication;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.apis.GetRequestTest;

import TestFramework.basics;

import static io.restassured.config.JsonConfig.jsonConfig;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveAuthProvider;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import static io.restassured.parsing.Parser.JSON;
import io.restassured.authentication.AuthenticationScheme;

public class BasicAuthApiRequest {
	private static Logger log=LogManager.getLogger(BasicAuthApiRequest.class.getName());
	
	@AfterClass 
	public void rest_assured_is_reset_after_each_test() {
        RestAssured.reset();
    }

    @BeforeClass 
    public void given_rest_assured_is_configured_with_big_decimal_as_return_type() {
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        //PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        //authScheme.setUserName("rubishgctcse@gmail.com");
        //authScheme.setPassword("Leader@2016");
        //RestAssured.authentication = authScheme;
        RestAssured.authentication = basic("rubishgctcse@gmail.com","Leader@2016");
        RestAssured.defaultParser = Parser.JSON;
        
    }
	
	private AuthenticationScheme basic(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static RequestSpecification requestSpec;

	@BeforeTest
	public static void createRequestSpecification() {
		
		requestSpec = new RequestSpecBuilder().
			setBaseUri("https://maps.googleapis.com").
			//setPort(3000).
			//setBasePath("/maps/api/place/textsearch/json").
			build().
			log().all();
	}
	/*
	@AfterTest
	public static void createResponseSpecification() {
				RestAssured.responseSpecification = new ResponseSpecBuilder().
		        build().
		        log().all();
	
	}
	*/
	@Rule
    public ExpectedException exception = ExpectedException.none();
	
	  @Test
	  //Extracting a string from the response
	  public void getBasicAuthTestResultViaRequestSpec() throws Exception  {
 		  exception.expect(AssertionError.class);
 		 Map<String,String> cookies = new HashMap<String, String>();

	    Response res  = given ()
	    .auth()
	    	.basic("rubishgctcse@gmail.com", "Leader@2016")
	    	.spec(requestSpec)
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .statusCode(200)
	    .body("isEmpty()", Matchers.is(true))
	    .log().all()
	    .extract()
	    .response();
	    System.out.println("Response details"+res.toString());
	    Integer statusCode = res.statusCode();
	    cookies = res.cookies();
	    System.out.println("Cookies"+cookies.values().toString());
	    System.out.println ("Extracting a string from the response"+statusCode);
	   Assert.assertEquals(statusCode.toString(), "200");
	  }
	  
	
	  @Test
	  //Extracting a string from the response
	  public void getBasicAuthTestResultCookies()  {
 		 //Map<String,String> cookies = new HashMap<String, String>();
 		//final Cookies cookies = get("/multiCookie").detailedCookies();
	    Cookies cookies  = given ()
	    	.spec(requestSpec)
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .detailedCookies();
	    System.out.println("Cookies"+cookies.size());
	    System.out.println("CookiesList"+cookies.getList("cookie1").size());
	    //System.out.println("Cookies"+cookies.values().toString());
	    //Assert.assertEquals(statusCode.toString(), "200");
	  }
	 
	  @Test
	  //Extracting a string from the response
	  public void getTestResultJsonPath()  {
 		
	    final Response response  = given ()
	    	.spec(requestSpec)
	    	.config(RestAssuredConfig.newConfig().with().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)))
	    	.expect()
	    	.statusCode(200)
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .extract()
	    .response();
	    
	   
	    System.out.println("JsonPath: "+response.getBody().asString());
	  }
	  
	  public static class CountingFilter implements Filter {

	        public int counter = 0;

	        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
	            counter++;
	            return ctx.next(requestSpec, responseSpec);
	        }

	    }
	
	  @Test
	    public void defaultFiltersDontAccumluate() {
	        CountingFilter myFilter = new CountingFilter();
	        try {
	            RestAssured.config = RestAssuredConfig.newConfig();
	            RestAssured.filters(myFilter);

	            RequestSpecification spec = new RequestSpecBuilder().build();

	            given()
	            .auth()
	            .basic("rubishgctcse@gmail.com", "Leader@2016")
	            .spec(requestSpec)
	            .when()
	            .get ("/maps/api/place/textsearch/json");
	            assertThat(myFilter.counter, equalTo(1));

	            given().spec(spec).get("/greetJSON?firstName=Johan&lastName=Doe");
	            assertThat(myFilter.counter, equalTo(2));
	        } finally {
	            RestAssured.reset();
	        }
	    } 
	
	Properties prop=new Properties();
	@BeforeTest
	public void getData() throws IOException
	{
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
	}

	  @Test
	  //Extracting a string from the response
	  public void getBasicAuthTestResult ()  {
		  final  SessionFilter sessionFilter = new SessionFilter();
		log.info("Host information"+prop.getProperty("HOST"));
		RestAssured.baseURI= prop.getProperty("HOST");
	    Response res  = given ()
	    .auth()
	    	.basic("rubishgctcse@gmail.com", "Leader@2016")
	    	.filter(sessionFilter)
	    	.queryParam("key",prop.getProperty("KEY"))
	    .when()
	    .get ("/maps/api/place/textsearch/json")
	    .then()
	    .extract()
	    .response();
	    Integer statusCode = res.statusCode();
	    System.out.println ("Extracting a string from the response"+statusCode);
	    System.out.println ("Extracting a session ID from the response"+sessionFilter.getClass());
	   Assert.assertEquals(statusCode.toString(), "200");
	  }

	  @Test
	  public void getDigestAuthTestResult ()  {
			log.info("Host information"+prop.getProperty("HOST"));
			RestAssured.baseURI= prop.getProperty("HOST");
		    Response res  = given ()
		    .auth()
		    	.digest("rubishgctcse@gmail.com", "Leader@2016")
		    	//.queryParam("key",prop.getProperty("KEY"))
		    .when()
		    .get ("/maps/api/place/textsearch/json")
		    .then()
		    .extract()
		    .response();
		    Integer statusCode = res.statusCode();
		    System.out.println ("Extracting a string from the digest auth response"+statusCode);
		   Assert.assertEquals(statusCode.toString(), "200");
		  }
	  
	  
	      
		  
	  }
	  
	  

