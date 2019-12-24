package com.api.qa.pages;

import static org.testng.Assert.assertEquals;
import java.io.IOException;

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

	@Test
	public void Login_Post() throws ParseException {
		
		
		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.header("user-type", "manager");
		req.header("locale", "en");
		req.header("api-key", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		req.header("api-secret", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");

		JSONObject json = new JSONObject();
		json.put("email", "managertest17@gmail.com");
		json.put("password", "Test@123");

//		json.put("company_id", 1183);
//		json.put("contest_name", "Testdfsdf123");
//		json.put("contest_start_date", "2019-12-09");
//		json.put("contest_end_date", "2019-12-15");

		req.body(json.toJSONString());

		Response response = req.post("https://dev-api.1huddle.co/api/rest/v1.5/auth/login");
		int statuscode = response.getStatusCode();
		ResponseBody body=response.getBody();
		
		System.out.println("Response Body: "+body.asString());
		System.out.println("Status Code: " + statuscode);
		
		JSONParser jsonParser = new JSONParser();
		
			JSONObject jsonObject = (JSONObject) jsonParser.parse(body.asString());
		
		String token = (String) jsonObject.get("onehuddletoken");	
		
		System.out.println("Authentication Token: "+token);
		Assert.assertEquals(statuscode, 200);
	}
	@Test
	public void CreateContest_POST()
	{
		
	}

	@AfterMethod
	public void TearDown() {

	}

//	@Test
//	void testCreateContestEndpoint() throws IOException {
//
//		String expectedMessage = "Contest added successfully.";
//		Integer validCompanyId = 1;
//		Integer validManagerId = 1;
//		boolean status = true;
//
//		if (status) {
//			try {
//				HttpRequestWithBody requestWithBody = Unirest.post("https://dev-api.1huddle.co/api/rest/v2.0/contest/add").header("Content-Type",
//						"application/json")
//	                       .header("session-token", "71e8512853cb4e5654f527a1192b27detjKd5xPlVlmM1c9U6uC7Q3lqqG6JCbIi9Q3YmxzK");
//				// Set body
//				JSONObject requestJson = new JSONObject();
//				requestJson.put("company_id", validCompanyId);
//				requestJson.put("created_by", validManagerId);
//				RequestBodyEntity requestBodyEntity = requestWithBody.body(requestJson.toString());
//
//				HttpResponse<JsonNode> jsonNodeHttpResponse;
//				try {
//					jsonNodeHttpResponse = requestBodyEntity.asJson();
//				} catch (UnirestException e) {
//					throw new RuntimeException(e);
//				}
//
//				String jsonString = jsonNodeHttpResponse.getBody().toString();
//				JSONObject jsonObj = new JSONObject();
//
////				String responseMessage = ((Object) jsonObj).getString("message");
////				assertEquals(expectedMessage, responseMessage);
//				System.out.println("Test case 1 execute successfully - Test to create contest using Endpoint");
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			assert (false) : "Application server is not running";
//		}
//	}

}
