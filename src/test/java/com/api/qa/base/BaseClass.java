package com.api.qa.base;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeClass;


public class BaseClass {
	
	public Logger logger;
	
	@BeforeClass
	public void setup1()
	{
		logger=Logger.getLogger("APITest");
		PropertyConfigurator.configure("Log4j.properties");
		logger.setLevel(Level.DEBUG);
	}
	
}
