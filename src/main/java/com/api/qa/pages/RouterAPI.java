package com.api.qa.pages;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	


	@BeforeMethod
	public void setup() {

	}

	@Test(enabled=true)
	public void Login_Post() throws ParseException {
		
		//Created by sonal	
		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("user-type", "manager");
		req.header("locale", "en");
		req.header("api-key", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		req.header("api-secret", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");

		JSONObject json = new JSONObject();
		json.put("email", "managertest17@gmail.com");
		json.put("password", "Test@123");

		req.body(json.toJSONString());

		Response response = req.post("https://dev-api.1huddle.co/api/rest/v1.5/auth/login");
		int statuscode = response.getStatusCode();
		ResponseBody body=response.getBody();
		String responseData=body.asString();
		
		System.out.println("Response Body: "+responseData);
		System.out.println("Status Code: " + statuscode);
		
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseData);
			JSONObject data= (JSONObject) jsonObject.get("data");
			//JSONObject manager= (JSONObject) data.get("manager");
			JSONObject authentication= (JSONObject) data.get("authentication");
			String token=(String) authentication.get("onehuddletoken");
			System.out.println("Authentication Token: "+token);
			
		} catch (ParseException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}					
		Assert.assertEquals(statuscode, 200);
	}
	@Test(enabled=false)
	public void CreateContest_POST()
	{
		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("accept", "application/json");
		req.header("session-token","5e2961e15a782bc15532274481dfe5b7Hpta8TYCxD3Qr5R34IxheJh2HwbRftoaRL8oBu7H");	
		 LocalDateTime startDate = LocalDateTime.now().plusMinutes(2);
		 LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		
		JSONObject json = new JSONObject();
		
		json.put("company_id", "1183");
		json.put("contest_name", "Test Contest");
		json.put("contest_start_date", "" + startDate.format(formatter) + "");
		json.put("contest_end_date", "" + endDate.format(formatter) + "");
		System.out.println(json);
		
		req.body(json.toJSONString());
		Response response=req.post("https://dev-api.1huddle.co/api/rest/v2.0/contest/add");
		int statuscode= response.getStatusCode();
		ResponseBody body=response.getBody();

		System.out.println("Response Body: "+body.asString());
		System.out.println("Status Code: " + statuscode);
		
	}

	@AfterMethod
	public void TearDown() {

	}

}