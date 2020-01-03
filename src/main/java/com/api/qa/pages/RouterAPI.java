package com.api.qa.pages;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
	JSONObject json = new JSONObject();
	JSONParser jsonParser = new JSONParser();
	int company_id;
	int contest_id;
	RequestSpecification req;
	int no;

	
	 LocalDateTime startDate = LocalDateTime.now().plusMinutes(2);
	 LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     
	@BeforeMethod
	public void setup() {
		
		req = RestAssured.given();
	}
	

	@Test(priority=1)
	public void Login_Post() throws ParseException {
		
		//Created by sonal	
		//RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("user-type", "manager");
		req.header("locale", "en");
		req.header("api-key", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		req.header("api-secret", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");

		json.put("email", "apiautomationmanager@gmail.com");
		json.put("password", "pass");

		req.body(json.toJSONString());

		Response response = req.post("https://dev-api.1huddle.co/api/rest/v1.5/auth/login");
		int statuscode = response.getStatusCode();
		ResponseBody body=response.getBody();
		String responseData=body.asString();
		
		System.out.println("Response Body for Login: "+responseData);		
		
	
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
		//RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("accept", "application/json");
		req.header("session-token",token);	
		

		Random rand= new Random();
		no=rand.nextInt(1000);
		
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
			
		json=(JSONObject) jsonParser.parse(responseData);	
		JSONObject data= (JSONObject)json.get("data");
		company_id = Integer.parseInt(data.get("company_id").toString());
		contest_id = Integer.parseInt(data.get("contest_id").toString());
			
		String message=(String) json.get("message");
		Boolean success=(Boolean) json.get("success");
		
		Assert.assertEquals(statuscode, 200);
		Assert.assertTrue(success);
		Assert.assertEquals(message, "Contest added successfully.");				
	}
	@Test(priority=3)
	public void ContestDetails_GET() throws ParseException
	{
		
		RestAssured.baseURI="https://dev-api.1huddle.co/api/rest/v2.0/contest";
		req= RestAssured.given();
		req.header("session-token",token);	
		req.header("Content-Type", "application/json");
		Response response = req.queryParam("contest_id",contest_id)
                .queryParam("company_id",company_id)
	   .get("/contest_details");
		
		String responseData= response.asString();
		System.out.println("Response Body for ContestDetails:"+responseData);
		
		json=(JSONObject)jsonParser.parse(responseData);
		String message=(String)json.get("message");
		
		Assert.assertEquals(message, "Contest description fetch successfully");
		
	}
	@Test(priority=4)
	public void UpdateContest_PUT() throws ParseException
	{
		RestAssured.baseURI="https://dev-api.1huddle.co/api/rest/v2.0/contest";
		req=RestAssured.given();
		req.header("session-token",token);	
		req.header("Content-Type", "application/json");
		json.put("company_id", company_id);
		json.put("contest_id", contest_id);
		json.put("contest_name", "Updated Test Contest-" + no);
		json.put("contest_image_url", "www.tempimageurl.com");
		json.put("contest_start_date", "" + startDate.format(formatter) + "");
		json.put("contest_end_date", "" + endDate.format(formatter) + "");
		json.put("trophy_url", "www.troply1.com");
		json.put("contest_rule", "Rules for testing contest");
		
		req.body(json.toJSONString());
		 Response response = req.put("/update");
		 System.out.println("Response Body for Update Contest :"+response.asString());
		 String responseData= response.asString();
		 json=(JSONObject)jsonParser.parse(responseData);
		 String message= (String)json.get("message");
		 Assert.assertEquals(message, "Contest details updated successfully");
	}
	@Test(priority=5)
	public void addGameToContest_POST()
	{
		//int[] gameid = new int[]{2871}; 
		
		JSONArray gameid = new JSONArray();
		gameid.put(2871);
		JSONObject json = new JSONObject();
		
		req.header("Content-Type","application/json");
		req.header("session-token",token);
		json.put("company_id", company_id);
		json.put("contest_id", contest_id);
		json.put("game_ids", gameid);
		json.put("game_start_date", ""+ startDate.format(formatter) +"");
		json.put("game_end_date", ""+ endDate.format(formatter) +"");
		Response response=req.post("https://dev-api.1huddle.co/api/rest/v2.0/contest/add_game_to_contest");
		String responseBody= response.asString();
		System.out.println("Response body for AddGameINContest"+responseBody);
		
		
	}

	@AfterMethod
	public void TearDown() {

	}

}