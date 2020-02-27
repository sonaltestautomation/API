package com.api.qa.base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.BeforeClass;

import com.api.qa.pages.RouterAPI;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class BaseClass {
	
	public Logger logger;

	public String token;
	public JSONObject json = new JSONObject();
	public JSONParser jsonParser = new JSONParser();
	public Integer company_id;
	public Integer contest_id;
	public RequestSpecification req;
	public Response response;
	public String contest_start_date;
	public String contest_end_date;
	public int no;
	public String apiBasePath="https://dev-api.1huddle.co/";
	public String responseData;
	public String message;
	public Boolean success;
	
	public LocalDateTime startDate = LocalDateTime.now().plusMinutes(5);
	public LocalDateTime endDate = LocalDateTime.now().plusMinutes(10);
	public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     
	
	@BeforeClass
	public void setup1()
	{
		  logger = LogManager.getLogger(RouterAPI.class);  
	}
	
}
