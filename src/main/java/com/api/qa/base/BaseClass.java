package com.api.qa.base;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeClass;

import com.api.qa.pages.RouterAPI;


public class BaseClass {
	
	public Logger logger;
	
	@BeforeClass
	public void setup1()
	{
		  logger = LogManager.getLogger(RouterAPI.class);  
	}
	
}
