package test.java.com.cucumber.stepdefinitions;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import com.cucumber.listener.Reporter;
import com.thoughtworks.selenium.Selenium;
import Utilities.Selenium_Functions;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import src.test.resources.Data.*;

public class StepDefinitions {

		@When("^url$")
		public void url() throws Throwable {
		//  Logger.log( "Starting");
		//  Logger.log( "When url");
		//  Logger.log( "completed");

		}

		@And("^login$")
		public void login() throws Throwable {
		//  Logger.log( "Starting");
		//  Logger.log( "And login");
		//  Logger.log( "completed");

		}
}