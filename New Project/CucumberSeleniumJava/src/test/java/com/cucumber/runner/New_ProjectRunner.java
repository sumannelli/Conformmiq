package com.cucumber.runner;
import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterClass;
import java.io.File;
@CucumberOptions(
        features = {"src/test/resources/features/New Project.feature"},
        glue = {"com.cucumber.stepdefinitions"},
        monochrome = true,
        plugin = {"com.cucumber.listener.ExtentCucumberFormatter:output/report.html"}
)
public class New_ProjectRunner extends AbstractTestNGCucumberTests {
/*
@AfterClass
    public static void teardown() {
        Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Windows");
        Reporter.setTestRunnerOutput("New Project Runner");
    }
*/
}
