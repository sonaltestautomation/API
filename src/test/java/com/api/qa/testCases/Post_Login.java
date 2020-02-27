package com.api.qa.testCases;

import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.api.qa.base.BaseClass;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class Post_Login extends BaseClass{
	Post_Login login= new Post_Login();

	@BeforeMethod
	public void setup() {
		
		req = RestAssured.given();
		BasicConfigurator.configure();  
		logger.info("*****Post Request*******");		
		req.header("Content-Type", "application/json");
		req.header("user-type", "manager");
		req.header("locale", "en");
		req.header("api-key", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		req.header("api-secret", "3ca09dc649ac5ec61e1a6c0fc17d5819UNia1nirLrBbcVxwSg52wjzrt6yjeHESIGAPwPID");
		json.put("email", "apiautomationmanager@gmail.com");
		json.put("password", "pass");
		req.body(json.toJSONString());
		response = req.post(apiBasePath + "api/rest/v1.5/auth/login");
	}
	public void response_Parsing() throws ParseException
	{
		JSONObject jsonObject = (JSONObject) jsonParser.parse(responseData);
		JSONObject data= (JSONObject) jsonObject.get("data");
		JSONObject authentication= (JSONObject) data.get("authentication");
		token=(String) authentication.get("onehuddletoken");
		message=(String) jsonObject.get("message");
		success=(Boolean) jsonObject.get("success");
	}
	@Test
	public void checkResponseCode()
	{
		int statuscode = response.getStatusCode();
		Assert.assertEquals(statuscode, 200);		
	}
	@Test
	public void checkResponseBody()
	{	
		ResponseBody body=response.getBody();
		responseData=body.asString();		
		System.out.println("Response Body for Login: "+responseData);	
		Assert.assertTrue(responseData!=null);		
	}
	@Test
	public void checkResponseMessage() throws ParseException
	{
		login.response_Parsing();			
		Assert.assertTrue(message!=null);
		Assert.assertEquals(message, "authenticated.");
	}
	@Test
	public void checkSuccessMessage() throws ParseException
	{
		login.response_Parsing();	
		Assert.assertTrue(success);
	}
	@Test
	public void checkResponseTime()
	{
		long responseTime=response.getTime();  //Returns response time in miliseconds
		System.out.println("Response Time is:" +responseTime);
		Assert.assertTrue(responseTime<2000);		
	}
	
	
	

}
