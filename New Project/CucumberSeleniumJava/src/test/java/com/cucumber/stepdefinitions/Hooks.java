package com.cucumber.stepdefinitions;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

import com.cucumber.listener.Reporter;

import Utilities.Selenium_Functions;
import cucumber.api.Scenario;
import cucumber.api.java.After;

public class Hooks extends Selenium_Functions {
	
//	@After
//	public static void tearDown(Scenario scenario,ITestResult result) throws Exception {
//		String screenshotPath=null;
//		if (scenario.isFailed()) {
//			
//			Selenium_Functions.extentReportScreenShot();
//			Thread.sleep(1000);
//			Selenium_Functions.driver.close();
//			Selenium_Functions.driver.quit();
//		}
//		
//			
//	}
}
