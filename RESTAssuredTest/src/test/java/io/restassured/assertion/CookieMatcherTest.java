package io.restassured.assertion;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.hamcrest.Matchers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThat;
import org.testng.Assert.*;

public class CookieMatcherTest {
    @Test
    public void testSetVersion() throws ParseException {
        String[] cookies = new String[]{
                "DEVICE_ID=123; Domain=.test.com; Expires=Thu, 12-Oct-2023 09:34:31 GMT; Path=/; Secure; HttpOnly;",
                "SPRING_SECURITY_REMEMBER_ME_COOKIE=12345;Version=0;Domain=.test.com;Path=/;Max-Age=1209600",
                "COOKIE_WITH_ZERO_MAX_AGE=1234;Version=0;Domain=.test.com;Path=/;Max-Age=0",
                "COOKIE_WITH_NEGATIVE_MAX_AGE=123456;Version=0;Domain=.test.com;Path=/;Max-Age=-1"};

        Cookies result = CookieMatcher.getCookies(cookies);
        AssertJUnit.assertEquals(4, result.size());

        Cookie sprintCookie = result.get("SPRING_SECURITY_REMEMBER_ME_COOKIE");
        AssertJUnit.assertEquals(0, sprintCookie.getVersion());
        AssertJUnit.assertEquals("12345", sprintCookie.getValue());
        AssertJUnit.assertEquals(".test.com", sprintCookie.getDomain());
        AssertJUnit.assertEquals("/", sprintCookie.getPath());
        AssertJUnit.assertEquals(1209600, sprintCookie.getMaxAge());
        AssertJUnit.assertEquals(false, sprintCookie.isSecured());
        AssertJUnit.assertEquals(false, sprintCookie.isHttpOnly());

        Cookie cookieWithZeroMaxAge = result.get("COOKIE_WITH_ZERO_MAX_AGE");
        AssertJUnit.assertEquals(0, cookieWithZeroMaxAge.getVersion());
        AssertJUnit.assertEquals("1234", cookieWithZeroMaxAge.getValue());
        AssertJUnit.assertEquals(".test.com", cookieWithZeroMaxAge.getDomain());
        AssertJUnit.assertEquals("/", cookieWithZeroMaxAge.getPath());
        AssertJUnit.assertEquals(0, cookieWithZeroMaxAge.getMaxAge());
        AssertJUnit.assertEquals(false, cookieWithZeroMaxAge.isSecured());
        AssertJUnit.assertEquals(false, cookieWithZeroMaxAge.isHttpOnly());

        Cookie cookieWithNegativeMaxAge = result.get("COOKIE_WITH_NEGATIVE_MAX_AGE");
        AssertJUnit.assertEquals(0, cookieWithNegativeMaxAge.getVersion());
        AssertJUnit.assertEquals("123456", cookieWithNegativeMaxAge.getValue());
        AssertJUnit.assertEquals(".test.com", cookieWithNegativeMaxAge.getDomain());
        AssertJUnit.assertEquals("/", cookieWithNegativeMaxAge.getPath());
        AssertJUnit.assertEquals(-1, cookieWithNegativeMaxAge.getMaxAge());
        AssertJUnit.assertEquals(false, cookieWithNegativeMaxAge.isSecured());
        AssertJUnit.assertEquals(false, cookieWithNegativeMaxAge.isHttpOnly());

        Cookie deviceCookie = result.get("DEVICE_ID");
        AssertJUnit.assertEquals(-1, deviceCookie.getVersion());
        AssertJUnit.assertEquals("123", deviceCookie.getValue());
        AssertJUnit.assertEquals(".test.com", deviceCookie.getDomain());
        AssertJUnit.assertEquals("/", deviceCookie.getPath());
        AssertJUnit.assertEquals(new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss Z", Locale.ENGLISH).parse("Thu, 12-Oct-2023 09:34:31 GMT"), deviceCookie.getExpiryDate());
        AssertJUnit.assertEquals(true, deviceCookie.isSecured());
        AssertJUnit.assertEquals(true, deviceCookie.isHttpOnly());

    }

    @Test 
    public void deals_with_empty_cookie_values() {
        // Given
        String[] cookiesAsString = new String[]{
                "un=bob; domain=bob.com; path=/", "", "_session_id=asdfwerwersdfwere; domain=bob.com; path=/; HttpOnly"};

        // When
        Cookies cookies = CookieMatcher.getCookies(cookiesAsString);

        // Then
        assertThat(cookies.size(), is(3));
        assertThat(cookies, Matchers.<Cookie>hasItem(nullValue()));
    }
}