package com.api.qa.pages;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;



public class RouterAPI {
	
	String token;
	JSONObject json = new JSONObject();
	JSONParser jsonParser = new JSONParser();
	Integer company_id;
	Integer contest_id;
	RequestSpecification req;
	String contest_start_date;
	String contest_end_date;
	int no;
	String apiBasePath="https://dev-api.1huddle.co/";
	
	 LocalDateTime startDate = LocalDateTime.now().plusMinutes(5);
	 LocalDateTime endDate = LocalDateTime.now().plusMinutes(10);
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     
	@BeforeMethod
	public void setup() {
		
		req = RestAssured.given();
	}
	

	@Test(priority=1)
	public void login_Post() throws ParseException {
		
		req.header("Content-Type", "application/json");
		req.header("user-type", "manager");
		req.header("locale", "en");
		req.header("api-key", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		req.header("api-secret", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");

		json.put("email", "apiautomationmanager@gmail.com");
		json.put("password", "pass");
		req.body(json.toJSONString());
		Response response = req.post(apiBasePath + "api/rest/v1.5/auth/login");
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
		Assert.assertTrue(success);
		Assert.assertEquals(message, "authenticated.");
		
	}
	@Test(priority=2)
	public void createContest_POST() throws ParseException
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
		Response response=req.post(apiBasePath + "api/rest/v2.0/contest/add");
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
	public void updateContestAddRewardToContest_PUT() throws ParseException
	{
		RestAssured.baseURI= apiBasePath + "api/rest/v2.0/contest";
		req= RestAssured.given();
		req.header("Content-Type","application/json");
		req.header("session-token",token);
		
		json.put("company_id",company_id);
		json.put("contest_id",contest_id);
		
		JSONObject rewards = new JSONObject();
		rewards.put("category_id",1);
		rewards.put("reward_id",5);
		rewards.put("reward_desc","Reward is added to the contest");
		json.put("rewards", rewards);
		
		req.body(json.toJSONString());
		Response response= req.put("/update");
		System.out.println("Response for adding rewards to contest:" +response.asString());
		
		String responseData= response.asString();
		json=(JSONObject)jsonParser.parse(responseData);
		 String message= (String)json.get("message");
		 Assert.assertEquals(message, "Contest details updated successfully");	
		
	}
	@Test(priority=4)
	public void contestDetails_GET() throws ParseException
	{
		
		RestAssured.baseURI=apiBasePath + "api/rest/v2.0/contest";
		req= RestAssured.given();
		req.header("session-token",token);	
		req.header("Content-Type", "application/json");
		Response response = req.queryParam("contest_id",contest_id)
                .queryParam("company_id",company_id)
	   .get("/contest_details");
		
		String responseData= response.asString();
		System.out.println("Response Body for ContestDetails:"+responseData);
		
		json=(JSONObject)jsonParser.parse(responseData);
		JSONObject data= (JSONObject)json.get("data");
		JSONObject contest_description= (JSONObject)data.get("contest_description");
		String message=(String)json.get("message");
		contest_start_date= (String)contest_description.get("contest_start_date");
		contest_end_date= (String)contest_description.get("contest_end_date");
		Assert.assertEquals(message, "Contest description fetch successfully");
		
	}
	@Test(priority=5)
	public void updateContest_PUT() throws ParseException
	{
		RestAssured.baseURI=apiBasePath + "api/rest/v2.0/contest";
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
	@Test(priority=6)
	public void addGameToContest_POST() throws ParseException
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
		json.put("limit_type", "DAILY");
		json.put("game_start_date",contest_start_date);
		json.put("game_end_date", contest_end_date);
		req.body(json.toJSONString());
		Response response=req.post(apiBasePath + "api/rest/v2.0/contest/add_game_to_contest");
		String responseBody= response.asString();
		System.out.println("Response body for AddGameINContest"+responseBody);
		
		 json=(JSONObject)jsonParser.parse(responseBody);
		 String message= (String)json.get("message");
		 Assert.assertEquals(message, "Game added in contest successfully.");		
	}
	@Test(priority=7)
	public void updateGameToContest_PUT() throws ParseException
	{
		RestAssured.baseURI=apiBasePath + "api/rest/v2.0/contest";
		req=RestAssured.given();
		JSONArray gameid = new JSONArray();
		gameid.put(2871);
		JSONObject json = new JSONObject();
		
		req.header("Content-Type","application/json");
		req.header("session-token",token);		
		json.put("company_id", company_id);
		json.put("contest_id", contest_id);
		json.put("game_ids", gameid);
		json.put("attempt_type", "DAILY");
		json.put("attempt_count",5);
		json.put("game_start_date",contest_start_date);
		json.put("game_end_date", contest_end_date);
		
		req.body(json.toJSONString());
		Response response= req.put("/update_games_in_contest");
		String responseBody=response.asString();
		System.out.println("Response Body for Update Game In Contest:" +responseBody);
		 json=(JSONObject)jsonParser.parse(responseBody);
		 String message= (String)json.get("message");
		Assert.assertEquals(message, "Game in contest updated successfully.");
		
	}
	@Test(priority=8)
	public void addAssignment_POST() throws ParseException
	{
		req.header("Content-Type","application/json");
		req.header("session-token",token);	
	
		JSONArray deptArray= new JSONArray();
		deptArray.put(8153);
		
		JSONObject jsonLocation = new JSONObject();
		jsonLocation.put("location_id", 5078);
		jsonLocation.put("location_name", "Test Location1");
		jsonLocation.put("is_all", false);
		jsonLocation.put("departments", deptArray);
		
		JSONArray recipientsArray= new JSONArray();
		recipientsArray.put(jsonLocation);
		
		JSONObject json = new JSONObject();
		json.put("company_id", company_id);
		json.put("contest_id", contest_id);
		json.put("is_all", false);
		json.put("recipients", recipientsArray);
		
		req.body(json.toJSONString());
		Response response= req.post(apiBasePath + "api/rest/v2.0/contest/add_assignment");
		String responseBody=response.asString();
		System.out.println("Response Body for add assignment:" +responseBody);
		json=(JSONObject)jsonParser.parse(responseBody);
		String message= (String)json.get("message");
		Assert.assertEquals(message, "Contest assigned successfully.");
		
	}
	@Test(priority=9)
	public void publish_POST() throws ParseException
	{
		req.header("Content-Type","application/json");
		req.header("session-token",token);
		
		json.put("company_id", company_id);
		json.put("contest_id",contest_id);
		
		req.body(json.toJSONString());
		Response response= req.post(apiBasePath + "api/rest/v2.0/contest/publish");
		String responseBody= response.asString();
		System.out.println("Response Body for Publish Contest :" +responseBody);
		json=(JSONObject)jsonParser.parse(responseBody);
		String message=(String)json.get("message");
		Assert.assertEquals(message, "Contest published successfully");
	}

	@AfterMethod
	public void TearDown() {

	}

}