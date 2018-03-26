package io.restassured.itest;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.gherkin.model.Then;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RootPathITest  {
    
	private static Logger log = LogManager.getLogger(RootPathITest.class.getName());
	  Properties prop=new Properties();
	  
	  @BeforeTest
	  public void getData() throws IOException
		{
		  System.out.println(System.getProperty("user.dir"));
			FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/env.properties");
			prop.load(fis);
		}
	  
	//@Rule
    //public ExpectedException exception = ExpectedException.none();

    @Test
    public void specifyingRootPathInExpectationAddsTheRootPathForEachSubsequentBodyExpectation() {
    		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		String response = given().
        when().
                 get("/store").asString();
		System.out.println("specifyingRootPathInExpectationAddsTheRootPathForEachSubsequentBodyExpectation"+response);
		final JsonPath jsonPath = new JsonPath(response).setRoot("store.book");
		//assertThat(jsonPath.getString("[0].author"), equalTo("Nigel Rees"));
		AssertJUnit.assertEquals(jsonPath.getString("[0].author"), "Nigel Rees");
        //body("store.book.category.size()", equalTo(4)).
        //body("store.book.author.size()", equalTo(4));
		
    }
    
    @Test
	public void getRequestFindAuthor() throws JSONException  {
    		RestAssured.baseURI= prop.getProperty("LOCALHOST");
		//make get request to fetch capital of norway
		Response resp = given().when().get("/store");
		
		//Fetching response in JSON
		JSONArray jsonResponse = new JSONArray(resp.asString());
		
		System.out.println("GetrequestFindAuthor:"+jsonResponse);
		//Fetching value of capital parameter
		String author = jsonResponse.getJSONObject(0).getString("book[0].author");
		
		//Asserting that Author is Nigel Rees
		AssertJUnit.assertEquals(author, "Nigel Rees");
	}
	
}
/*
    @Test
    public void specifyingRootPathThatEndsWithDotAndBodyThatEndsWithDotWorks() throws Exception {
        expect().
                 root("store.book.").
                 body(".category.size()", equalTo(4)).
                 body(".author.size()", equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void specifyingRootPathThatEndsWithDotAndBodyThatDoesntEndWithDotWorks() throws Exception {
        expect().
                 root("store.book.").
                 body("category.size()", equalTo(4)).
                 body("author.size()", equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void specifyingRootPathThatDoesntEndWithDotAndBodyThatEndsWithDotWorks() throws Exception {
        expect().
                 root("store.book").
                 body(".category.size()", equalTo(4)).
                 body(".author.size()", equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void specifyingRootPathAndBodyThatStartsWithArrayIndexingWorks() throws Exception {
        expect().
                 root("store.book").
                 body("[0].category", either(equalTo("reference")).or(equalTo("fiction"))).
        when().
                get("/store");
    }

    @Test
    public void specifyingRootPathThatAndEmptyPathWorks() throws Exception {
        expect().
                 root("store.book.category.size()").
                 body("", equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void specifyingEmptyRootPathResetsToDefaultRootObject() throws Exception {
        expect().
                 rootPath("store.book").
                 body("category.size()", equalTo(4)).
                 body("author.size()", equalTo(4)).
                 root("").
                 body("store.book.category.size()", equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void whenNotSpecifyingExplicitRootPathThenDefaultRootPathIsUsed() throws Exception {
        rootPath = "store.book";
        try {
            expect().
                     body("category.size()", equalTo(4)).
                     body("author.size()", equalTo(4)).
            when().
                     get("/store");
        } finally {
            RestAssured.reset();
        }
    }

    @Test
    public void resetSetsRootPathToEmptyString() throws Exception {
        rootPath = "store.book";

        RestAssured.reset();

        assertThat(rootPath, equalTo(""));
    }

    @Test
    public void specifyingRootPathWithBodyArgs() throws Exception {
        expect().
                rootPath("store.book.category[%d]").
                body(withArgs(0), equalTo("reference")).
                body(withArgs(1), equalTo("fiction")).
        when().
                get("/store");
    }

    @Test
    public void specifyingRootPathWithMultipleBodyArgs() throws Exception {
        final String category = "category";
        expect().
                rootPath("store.book.%s[%d]").
                body(withArgs(category, 0), equalTo("reference")).
                body(withArgs(category, 1), equalTo("fiction")).
        when().
                get("/store");
    }

    @Test
    public void specifyingRootPathWithMultipleContentArguments() throws Exception {
        final String category = "category";
        expect().
                rootPath("store.book.%s[%d]").
                content(withArguments(category, 0), equalTo("reference")).
                content(withArguments(category, 1), equalTo("fiction")).
        when().
                get("/store");
    }

    @Test
    public void specifyingRootPathInMultiBodyAddsTheRootPathForEachExpectation() throws Exception {
        expect().
                 root("store.book").
                 body(
                    "category.size()", equalTo(4),
                    "author.size()", equalTo(4)
                 ).
        when().
                 get("/store");
    }

    @Test
    public void specifyingRootPathInMultiContentAddsTheRootPathForEachExpectation() throws Exception {
        expect().
                 root("store.book").
                 content(
                    "category.size()", equalTo(4),
                    "author.size()", equalTo(4)
                 ).
        when().
                 get("/store");
    }

    @Test
    public void specifyingRootPathWithArguments() throws Exception {
        expect().
                 root("store.%s", withArgs("book")).
                 content(
                    "category.size()", equalTo(4),
                    "author.size()", equalTo(4)
                 ).
        when().
                 get("/store");
    }

    @Test
    public void appendingRootPathWithoutArgumentsWorks() throws Exception {
        expect().
                 root("store.%s", withArgs("book")).
                 body("category.size()", equalTo(4)).
                 appendRoot("author").
                 body("size()", equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void appendingRootPathWithArgumentsWorks() throws Exception {
        expect().
                 root("store.%s", withArgs("book")).
                 body("category.size()", equalTo(4)).
                 appendRoot("%s.%s", withArgs("author", "size()")).
                 body(withNoArgs(), equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void canAppendRootPathToEmptyRootPath() throws Exception {
        expect().
                appendRoot("store.%s").
                body(withArgs("book.category.size()"), equalTo(4)).
        when().
                get("/store");
    }

    @Test
    public void usingBodyExpectationWithoutPath() throws Exception {
        expect().
                 root("store.%s").
                 body(withArgs("book.category.size()"), equalTo(4)).
        when().
                 get("/store");
    }

    @Test
    public void cannotUseBodyExpectationWithNoPathWhenRootPathIsEmpty() throws Exception {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Cannot specify arguments when root path is empty");

        expect().body(withArgs("author", "size()"), equalTo("Something"));
    }

    @Test
    public void cannotDetachRootPathToFromRootPath() throws Exception {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Cannot detach path when root path is empty");

        expect().detachRoot("path");
    }

    @Test
    public void detachingRootPathWorksWithOldSyntax() throws Exception {
        expect().
                root("store.%s", withArgs("book")).
                body("category.size()", equalTo(4)).
                detachRoot("book").
                body("size()", equalTo(2)).
        when().
                get("/store");
    }

    @Test
    public void detachingRootPathWorksWithNewSyntax() throws Exception {
        when().
                get("/store").
        then().
                root("store.%s", withArgs("book")).
                body("category.size()", equalTo(4)).
                detachRoot("book").
                body("size()", equalTo(2));
    }

    @Test
    public void detachingRootPathWorksWhenSpecifyingDot() throws Exception {
        when().
                get("/store").
        then().
                root("store.%s", withArgs("book")).
                body("category.size()", equalTo(4)).
                detachRoot(".book").
                body("size()", equalTo(2));
    }

    @Test
    public void detachingRootPathThrowsISERootPathDoesntEndWithPathToDetach() throws Exception {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Cannot detach path 'another' since root path 'store.book' doesn't end with 'another'.");

        when().
                get("/store").
        then().
                root("store.%s", withArgs("book")).
                body("category.size()", equalTo(4)).
                detachRoot("another").
                body("size()", equalTo(2));
    }

    @Test
    public void supportsAppendingArgumentsDefinedInAppendRootAtALaterStage() throws Exception {
        when().
                 get("/store").
        then().
                 root("store.%s", withArgs("book")).
                 body("category.size()", equalTo(4)).
                 appendRoot("%s.%s", withArgs("author")).
                 body(withArgs("size()"), equalTo(4));
    }

    @Test
    public void supportsAppendingArgumentsDefinedInRootAtALaterStage() throws Exception {
        when().
                 get("/store").
        then().
                 root("store.%s.%s", withArgs("book")).
                 body("size()", withArgs("category"), equalTo(4));
    }
}
    */