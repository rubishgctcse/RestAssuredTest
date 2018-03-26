package com.gradle;

import org.junit.Test;

import com.gradle.util.Person;

import static org.junit.Assert.*;

public class PersonTest {
	
    @Test
    public void canConstructAPersonWithAName() {
    		Person person = new Person("Larry");
    		//Person person = new Person("Larry");
        assertEquals( "Larry", person.getString());
    }
    
}