package lotto.examples;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

//npm install -g json-server
//json-server --watch /Users/srubish/Desktop/JavaPractice/RESTAssuredTest/lotto.json


public class GetLottoExamples {
	private static Logger log = LogManager.getLogger(GetLottoExamples.class.getName());
	  Properties prop=new Properties();
	 
	// RestAssured.config = RestAssuredConfig.newConfig().decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8"));
/*
	@BeforeMethod
	public void setUp() throws Exception {
	      try {
	          //RestAssured.port = 9000;
	          RestAssured.useRelaxedHTTPSValidation();
	          RestAssured.config().getSSLConfig().with().keyStore("classpath:keystore.p12", "password");
	      } catch (Exception ex) {
	          System.out.println("Error while loading keystore >>>>>>>>>");
	          ex.printStackTrace();
	      }
	  }
	  */
	  @BeforeTest
	  public void getData() throws IOException
		{
			FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
			prop.load(fis);
		}
	  
	  public static String GenerateStringFromResource(String path) throws IOException {
		    return new String(Files.readAllBytes(Paths.get(path)));
	  }
	  @Test
	  public void getLottoId() throws IOException {
		//String json=GenerateStringFromResource(System.getProperty("user.dir")+"/lotto.json"); 
		
		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		given ()
	    .when()
	    		.get ("/lotto")
	    .then()
	    		.body("lottoId", is(5));
	    		
	    System.out.println ("Testing Lotto.lottoID to be 5");
	  }
	 
	  @Test
	    public void specifyingRootPathInExpectationAddsTheRootPathForEachSubsequentBodyExpectation() throws Exception {
	    		RestAssured.baseURI= prop.getProperty("LOCALHOST");
			String json = given().
	        when().
	                 get("/store").asString();
	        //String json = response.asString();
			System.out.println("specifyingRootPathInExpectationAddsTheRootPathForEachSubsequentBodyExpectation"+json);
			final JsonPath jsonPath = new JsonPath(json).setRoot("store.book");
			assertThat(jsonPath.getString("[0].author"), equalTo("Nigel Rees"));
			
	        //body("store.book.category.size()", equalTo(4)).
	        //body("store.book.author.size()", equalTo(4));
			
	    } 
	  
	@Test
	  public void getLottoHasItems() throws IOException {
		//String json=GenerateStringFromResource(System.getProperty("user.dir")+"/lotto.json"); 
		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		given ()
	    .when()
	    		.get ("/lotto")
	    .then()
	    		.body("winners.winnerId", hasItems(23, 54));
	    System.out.println ("Testing winners.winnerId hasItems(23, 54)");
	  }
	  
	  @Test
	  public void getLottoDecimal() throws IOException {
		//String json=GenerateStringFromResource(System.getProperty("user.dir")+"/lotto.json"); 
		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		given ()
	   // 		.config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
	    .when()
	    .get ("/lotto")
	    .then()
	    .body("winning-numbers[0]", is(2.12f));
	    System.out.println ("Testing winning-numbers is 2.12");
	  }
	  
	  /*
	  @Test
	  public void getLottoDecimalUsingConfig() throws IOException {
		//String json=GenerateStringFromResource(System.getProperty("user.dir")+"/lotto.json"); 
		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		given ()
 		.config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
	    .when()
	    .get ("/lotto")
	    .then()
	    .body("winning-numbers[0]", is(new BigDecimal(2.12));
	    System.out.println ("Testing winning-numbers is 2.12");
	  }
	  */
	  @Test
	  //Matching JSON Schema using validator 
	  public void getProductSchema() throws IOException {
		String schema = GenerateStringFromResource(System.getProperty("user.dir")+"/product-schema.json");
		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		given ()
	    .when()
	    		.get ("/items")
	    .then()
	    		.assertThat()
	    		.body(JsonSchemaValidator.matchesJsonSchema(schema));
	    System.out.println ("Testing matchesJsonSchemaInClasspath in product-schema.json");
	  }
	  /*
	  @Test
	  public void getProductSchemaInClassPath() throws IOException {
			//String schema = GenerateStringFromResource(System.getProperty("user.dir")+"/product-schema.json");
			RestAssured.baseURI= prop.getProperty("LOCALHOST");
			given ()
		    .when()
		    		.get ("/items")
		    .then()
		    		.assertThat()
		    		.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(System.getProperty("user.dir")+"/product-schema.json"));
		    System.out.println ("Testing matchesJsonSchemaInClasspath in product-schema.json");
		  }
	  
	  @Test
	  public void getProductResource() throws IOException {
			//String schema = GenerateStringFromResource(System.getProperty("user.dir")+"/product-schema.json");
			RestAssured.baseURI= prop.getProperty("LOCALHOST");
			given ()
		    .when()
		    		.get ("/lotto")
		    .then()
		    		.assertThat()
		    		.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(System.getProperty("user.dir")+"/lotto.json")
		    		.using(settings().with().checkedValidation(false)));
		    System.out.println ("Testing matchesJsonSchemaInClasspath in product-schema.json");
		  }

	  @Test 
	  public void  json_schema_validator_supports_disabling_checked_validation_statically() {
	        // Given
	        JsonSchemaValidator.settings = settings().with().checkedValidation(false);
	        RestAssured.baseURI= prop.getProperty("LOCALHOST");
	        // When
	        try {
	        	given()
	        	.when()
	            .get("/items")
	        .then().assertThat()
	        //.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(System.getProperty("user.dir")+"product-schema.json"));
	        	.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("/Users/srubish/Desktop/JavaPractice/RESTAssuredTest/product-schema.json"));
	        } finally {
	            JsonSchemaValidator.reset();
	        }
	    }
	  */
	  
	  @Test
		public void testGetSingleLotto() {
		  RestAssured.baseURI= prop.getProperty("LOCALHOST");
			Response response = given ()
		    .when()
		    		.get("/lotto");
			//Response res = get("/service/single-user");
			AssertJUnit.assertEquals(200, response.getStatusCode());
			String json = response.asString();
			JsonPath jsonPath = new JsonPath(json).setRoot("lotto");
            int lottoId = jsonPath.getInt("lottoId");
            List winnderIds = jsonPath.get("winnders.winnderId");
			//JsonPath jp = new JsonPath(json);
			AssertJUnit.assertEquals(5, lottoId);
			//assertEquals(45, jp.get("winning-number[1]"));
		}
	  
	  @Test
	  //Fetch the Books having price less than 10.
	  public void getBooksPriceLessThanTen() {
		  RestAssured.baseURI= prop.getProperty("LOCALHOST");
			given ()
		    .when()
		    		.get("/store")
		    	.then()
		    .body("book.findAll { it.price < 10 }.title", hasItems("Sayings of the Century", "Moby Dick"));
			//System.out.print(responses);
		}
	  
	  @Test
	  //Fetch the Books having price less than 10.
	  public void getFictionCategoryBooks() {
		  RestAssured.baseURI= prop.getProperty("LOCALHOST");
			List<String> groceries = given ()
		    .when()
		    		.get("/store")
			.path("book.findAll { it.category == 'fiction'}.item");
			for(int i=0;i<groceries.size();i++){
			    System.out.println(groceries.get(i));
			} 
		   
		}
	  
	 
	  @Test
	  public void getGetLottoJsonPath() {
		  RestAssured.baseURI= prop.getProperty("LOCALHOST");
			Response response = given ()
		    .when()
		    	.get("/lotto");
			String json = response.asString();
		  JsonPath jsonPath = new JsonPath(json);
		// Extract the lotto id
		  int lottoId = jsonPath.getInt("lotto.lottoId");
		  System.out.println(lottoId);
		  // Get the list of winning numbers,
		  // note that you don't need to escape 'winning-numbers'.
		  List<String> winningNumbers = jsonPath.get("lotto.winning-numbers");
		  for(int i=0;i<winningNumbers.size();i++){
			    System.out.println(winningNumbers.get(i));
			} 
		  // Returns a list containing 23, 54
		  List<Integer> winningIds = jsonPath.get("lotto.winners.winnerId");
		  for(int i=0;i<winningIds.size();i++){
			    System.out.println(winningIds.get(i));
			} 
	  }	
	  
	  /*
	  @Test
	  public void putRequestJsonPath()  {
		 //Create a Request pointing to the Service Endpoint
		  RestAssured.baseURI= prop.getProperty("LOCALHOST");
		  //RequestSpecification request = RestAssured.given();
		 
		  JSONObject requestParams = new JSONObject();
		  requestParams.put("lottoId", 10); 
		  
		  
		  //Add JSON body in the request and send the Request
		  //request.header("Content-Type", "application/json");
		  //request.body(requestParams.toString());
		  
		  
		  Response response = (Response) given()
				  .body(requestParams.toString())
				  .when()
				  .contentType (ContentType.JSON)
				  .put("/lotto");
				  
				 
		  System.out.println("putRequestJsonPath"+response);
		  //Validate the response
		  int statusCode = response.getStatusCode();
		  Assert.assertEquals(statusCode, "201");
		  String successCode = response.jsonPath().get("SuccessCode");
		  Assert.assertEquals( "Correct Success code was returned", successCode, "OPERATION_SUCCESS");

	  }
	
	  public static String createPlaceData()
		{;
			String b="{\n  \"location\": {\n    \"lat\": -33.8669710,\n    \"lng\": 151.1958750\n  },\n  \"accuracy\": 50,\n  \"name\": \"Google Shoes!\",\n  \"phone_number\": \"(02) 9374 4000\",\n  \"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\",\n  \"types\": [\"shoe_store\"],\n  \"website\": \"http://www.google.com.au/\",\n  \"language\": \"en-AU\"\n}";
		return b;
		}
	  @Test
	  public void createPutRequest()
		{
			RestAssured.baseURI=prop.getProperty("LOCALHOST");
			given()
			.body(createPlaceData())
			.when()
			.put("/lotto")
			.then()
			.assertThat()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON);
			//	.and()
			//.body("status",equalTo("OK"));
		}
		*/
	  
	  @Test
	  public void getJsonPathTest() {
		  RestAssured.defaultParser = Parser.JSON;
		  Response response = given()
				  .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
				  .when()
				  .get("https://jsonplaceholder.typicode.com/users/")
				  .then()
				  .contentType(ContentType.JSON)
				  .extract()
				  .response();
		  List<String> jsonResponse = response.jsonPath().getList("$");
		  System.out.println("Size of Json Response: "+ jsonResponse.size());
		  
		  String usernames = response.jsonPath().getString("username");
		  System.out.println("Usernames: "+usernames);
		  
		  List<String> jsonResponseList = response.jsonPath().getList("username");
		  System.out.println("First Element in Json response:"+jsonResponseList.get(0));
		  
		  Map<String, String> companyList = response.jsonPath().getMap("company[0]");
	      System.out.println("Company List names:"+companyList.get("name"));
	  }
	  
}
