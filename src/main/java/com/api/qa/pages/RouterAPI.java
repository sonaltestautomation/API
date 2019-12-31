package com.api.qa.pages;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.eclipse.persistence.sessions.serializers.JSONSerializer;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;



public class RouterAPI {
	
	String token;

	@BeforeMethod
	public void setup() {

	}
	

	@Test(priority=1)
	public void Login_Post() throws ParseException {
		
		//Created by sonal	
		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("user-type", "manager");
		req.header("locale", "en");
		req.header("api-key", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		req.header("api-secret", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");

		JSONObject json = new JSONObject();
		json.put("email", "apiautomationmanager@gmail.com");
		json.put("password", "pass");

		req.body(json.toJSONString());

		Response response = req.post("https://dev-api.1huddle.co/api/rest/v1.5/auth/login");
		int statuscode = response.getStatusCode();
		ResponseBody body=response.getBody();
		String responseData=body.asString();
		
		System.out.println("Response Body for Login: "+responseData);		
		
		JSONParser jsonParser = new JSONParser();
		//try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseData);
			JSONObject data= (JSONObject) jsonObject.get("data");
			//JSONObject manager= (JSONObject) data.get("manager");
			JSONObject authentication= (JSONObject) data.get("authentication");
			token=(String) authentication.get("onehuddletoken");
			//System.out.println("Authentication Token: "+token);
			String message=(String) jsonObject.get("message");
			Boolean success=(Boolean) jsonObject.get("success");
			
		//} catch (ParseException e) {
			 //TODO Auto-generated catch block
			//e.printStackTrace();
		//}					
		Assert.assertEquals(statuscode, 200);
		//Assert.assertEquals(success, true);
		Assert.assertTrue(success);
		Assert.assertEquals(message, "authenticated.");
		
	}
	@Test(priority=2)
	public void CreateContest_POST() throws ParseException
	{
		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("accept", "application/json");
		req.header("session-token",token);	
		 LocalDateTime startDate = LocalDateTime.now().plusMinutes(2);
		 LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		
		JSONObject json = new JSONObject();
		Random rand= new Random();
		int no=rand.nextInt(1000);
		
		json.put("company_id", "1201");
		json.put("contest_name", "Test Contest-" + no);
		json.put("contest_start_date", "" + startDate.format(formatter) + "");
		json.put("contest_end_date", "" + endDate.format(formatter) + "");
		
		req.body(json.toJSONString());
		Response response=req.post("https://dev-api.1huddle.co/api/rest/v2.0/contest/add");
		int statuscode= response.getStatusCode();
		ResponseBody body=response.getBody();
		String responseData=body.asString();
		
		System.out.println("Response Body for Create Contest: " + responseData);		
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(responseData);
		String message=(String) jsonObject.get("message");
		Boolean success=(Boolean) jsonObject.get("success");
		
		Assert.assertEquals(statuscode, 200);
		//Assert.assertEquals(success, "true");
		Assert.assertTrue(success);
		Assert.assertEquals(message, "Contest added successfully.");
		
		
	}

	@AfterMethod
	public void TearDown() {

	}

}