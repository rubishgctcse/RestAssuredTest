package TestFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class basics4 {
	private static Logger log =LogManager.getLogger(basics4.class.getName());
	Properties prop=new Properties();
	@BeforeTest
	public void getData() throws IOException
	{
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
		prop.load(fis);
	}
	@Test
	public void postDataXML() throws IOException
	{
		String postdata=GenerateStringFromResource(System.getProperty("user.dir")+"/postdata.xml");
		RestAssured.baseURI=prop.getProperty("HOST");
		Response resp=given().
		queryParam("key",prop.getProperty("KEY")).
		body(postdata).
		when().
		post("/maps/api/place/add/xml").
		then().assertThat().statusCode(200).and().contentType(ContentType.XML).
		extract().response();
		
		XmlPath x=ReusableMethods.rawToXML(resp);
		System.out.println(x.get("PlaceAddResponse.place_id"));
		
		
		
	// Create a place =response (place id)
		
		// delete Place = (Request - Place id)	
		

	}
	
	public static String GenerateStringFromResource(String path) throws IOException {

	    return new String(Files.readAllBytes(Paths.get(path)));

	}
}
