package com.pnc.test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;

public class CountryAPITest {
	@DataProvider(name="ValidCountryName")
	public String[] [] getValidCountryNameData () {
		String cName [] [] = new String [4] [2];
		
		cName [0] [0] = "Peru";
		cName [0] [1] = "Lima";
		cName [1] [0] = "India";
		cName [1] [1] = "New Delhi";
		cName [2] [0] = "Australia";
		cName [2] [1] = "Canberra";
		cName [3] [0] = "Japan";
		cName [3] [1] = "Tokyo";
		
		return cName;
	}
	
	@DataProvider(name="SpecialCharacterCountryName")
	public String[] getSpecialCharacterCountryNameData () {
		String cName [] = new String [4];
		
		cName [0] = "/name/P&";
		cName [1] = "/name/I*";
		cName [2] = "/name/A@";
		cName [3] = "/name/J#";
		
		return cName;
	}
	
	@Test(dataProvider = "ValidCountryName")
	public void testCountryAPIValidCountryNames (String inputCountryName, String validateCapitalName) { 
		baseURI = "https://restcountries.com/v3.1";
		String additionalURL = "/name/" + inputCountryName;
		
		given().
			get(additionalURL).
		then().
			statusCode(200);
		
		Response resp = get (additionalURL);
		ArrayList respCapitalList = resp.jsonPath().get("capital[0]");
		String respCapital = (String) respCapitalList.get(0);
				
		Assert.assertEquals(validateCapitalName, respCapital.toString());

	}
	
	@Test
	public void testCountryAPIInvalidCountryName () {
		baseURI = "https://restcountries.com/v3.1";
		String additionalURL = "/name/tstCountry";	
		
		given().
			get(additionalURL).
		then().
			statusCode(404);
		
	}
	
	@Test
	public void testCountryAPINULLCountryName () {
		baseURI = "https://restcountries.com/v3.1";
		String additionalURL = "/name/NULL";	
		
		given().
			get(additionalURL).
		then().
			statusCode(404);
		
	}
	
	@Test(dataProvider = "SpecialCharacterCountryName")
	public void testCountryAPISpecialCharacterCountryName (String nm) {
		baseURI = "https://restcountries.com/v3.1";
		String additionalURL = nm;	
		
		given().
			get(additionalURL).
		then().
			statusCode(404);
		
	}

}
