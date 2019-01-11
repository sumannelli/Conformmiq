package Utilities;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import com.cucumber.listener.Reporter;

public class Selenium_Functions {

	private static Object FinalDay = null;
	private static Object FinalMonth = null;
	private static Object FinalYear = null;
	static Logger log = Logger.getLogger(Selenium_Functions.class.getName());
	public static WebDriver driver;
	public static String driverPath;
	public static Properties prop;
	static String starttimeStamp;
	static String endtimeStamp;

	public static void navigateToURL(String browser) throws Throwable {
		prop = new Properties();
		String filename = System.getProperty("user.dir") + "\\src\\test\\resources\\Data\\data.properties";
		FileInputStream file = new FileInputStream(filename);
		prop.load(file);
		// Reporter.addStepLog("Property file loaded successfully...");
		driverPath = System.getProperty("user.dir") + prop.getProperty("driverPath");
		log.info("Launching chrome browser...");
		
		switch(browser){
		case "ie":
			 System.setProperty("webdriver.ie.driver",driverPath +
			 "IEDriverServer.exe");
			 driver = new InternetExplorerDriver();
			break;
		case "firefox":
			ProfilesIni listProfiles = new ProfilesIni();
			 System.setProperty("webdriver.gecko.driver",driverPath +
					 "geckodriver.exe");
			FirefoxProfile profile = listProfiles.getProfile("ProfileSeleniumAutomation");
			driver = new FirefoxDriver(profile);
			 
			 
			
//			 driver = new FirefoxDriver();
			break;
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--incognito");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-infobars");
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			System.setProperty("webdriver.chrome.driver", driverPath + "\\chromedriver.exe");	
			driver = new ChromeDriver(cap);
			break;
		default:
		}
		driver.manage().window().maximize();
		log.info("Browser Maximised...");
		driver.navigate().to(prop.getProperty("URL"));
		log.info("Loaded Web Application...");
		Sleep(30);
	}

	public static void executionStartTime() {
		starttimeStamp = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(Calendar.getInstance().getTime());
		log.info("\n\n\n\n\nNew Test Started @ " + starttimeStamp + " .................................!!");
		// Reporter.addStepLog("\n\n\n\n\nNew Test Started @ " + starttimeStamp
		// + " .................................!!");
	}

	public static void executionEndTime() throws Exception {
		endtimeStamp = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(Calendar.getInstance().getTime());

		calculateTimeDifference(starttimeStamp, endtimeStamp);
		Thread.sleep(2000);
		try {

			driver.quit();
			// Reporter.addStepLog("The Execution Complited And Application
			// Terminated!");
			log.info("The Execution Complited And Application Terminated!");

		} catch (Throwable e) {
			log.error("Sorry! The Application is not Terminating, Please Terminate manually!");
			// Reporter.addStepLog("Sorry! The Application is not Terminating,
			// Please Terminate manually!");

		}
	}

	public static void calculateTimeDifference(String start, String end) throws Exception {
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(start);
			d2 = format.parse(end);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			log.info(diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds
					+ " seconds.");
			Reporter.addStepLog(diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds
					+ " seconds.");
		} catch (Throwable e) {

		}
	}

	// Find Element Method
	public static WebElement FindElementSafe(By locator) throws InterruptedException {

		try {

			HighlightMe(driver.findElement(locator));
			return driver.findElement(locator);

		} catch (NoSuchElementException e) {
			waitFor();
			FindElementSafe(locator);
		}
		return null;
	}

	// Find Element without HighlightMe Method
	public static WebElement FindElementWithoutHighlightMe(By locator) throws InterruptedException {

		try {

			return driver.findElement(locator);

		} catch (NoSuchElementException e) {
			waitFor();
			FindElementSafe(locator);
		}
		return null;
	}

	// Implicit Wait Method
	public static void waitFor() {
		try {
			// Reporter.addStepLog("Implicit Wait for 20 seconds");
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Page Load Timeout Method
	public static void PageLoadTimeout() {
		try {
			// Reporter.addStepLog("page Load Timeout for 300 seconds");
			driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Set Script Timeout Method
	public static void SetScriptTimeout() {
		try {
			// Reporter.addStepLog("Set Script Timeout for 100 seconds");
			driver.manage().timeouts().setScriptTimeout(100, TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Sleep Method
	public static void Sleep(int value) {
		try {
			// Reporter.addStepLog("script sleep for "+value+" mili seconds");
			Thread.sleep(value);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Get Current URL method
	public static String getCurrentUrl() {
		try {
			// Reporter.addStepLog("Current URL : " + driver.getCurrentUrl());
			return driver.getCurrentUrl();
		} catch (Exception e) {
			Sleep(300);
			getCurrentUrl();
		}
		return null;
	}

	// Get Page Source method
	public static String getPageSource() {
		try {
			return driver.getPageSource();
		} catch (Exception e) {
			Sleep(300);
			getPageSource();
		}
		return null;
	}

	// Get Title method
	public static String getTitle() {
		try {
			// Reporter.addStepLog("Page Title : " + driver.getTitle());
			return driver.getTitle();
		} catch (Exception e) {
			Sleep(300);
			getTitle();
		}
		return null;
	}

	// HighlightMe Method
	public static void HighlightMe(WebElement element) throws InterruptedException {
		// Creating JavaScriptExecuter Interface

		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int iCnt = 0; iCnt < 3; iCnt++) {
			js.executeScript("arguments[0].style.border='5px groove pink'", element);
			// chartreuse
			Sleep(100);
			js.executeScript("arguments[0].style.border=''", element);
		}
	}

	public static void clicktab(String locator, String type) throws Throwable {
		try {
			WebElement tab = driver.findElement(By.xpath(prop.getProperty(locator)));
			tab.sendKeys(Keys.TAB);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// Radio Button Click Method
	public static void radioButtonClick(String locator, String type) throws Throwable {
		try {
			// Reporter.addStepLog("Clicked on Radio button...");
			WebElement radioBtn = locatorIdentifier(locator, type);
			radioBtn.click();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// prop.getProperty(locator) Identifier Method
	public static WebElement locatorIdentifier(String locator, String type) throws Throwable {

		switch (type) {
		case "xpath":
			if (FindElementSafe(By.xpath(locator)) != null) {
				// Reporter.addStepLog("Element identified with XPath : " +
				// locator);

				return driver.findElement(By.xpath(locator));
			}
			break;
		case "id":
			if (FindElementSafe(By.id(locator)) != null) {
				// Reporter.addStepLog("Element identified with ID : " +
				// locator);
				return driver.findElement(By.id(locator));
			}
			break;
		case "name":
			if (FindElementSafe(By.name(locator)) != null) {
				// Reporter.addStepLog("Element identified with Name : " +
				// locator);
				return driver.findElement(By.name(locator));
			}
			break;
		case "className":
			if (FindElementSafe(By.className(locator)) != null) {
				// Reporter.addStepLog("Element identified with ClassName : " +
				// locator);
				return driver.findElement(By.className(locator));
			}
			break;
		case "cssSelector":
			if (FindElementSafe(By.cssSelector(locator)) != null) {
				// Reporter.addStepLog("Element identified with cssSelector : "
				// + locator);
				return driver.findElement(By.cssSelector(locator));
			}
			break;
		case "linktext":
			waitFor();
			if (FindElementSafe(By.linkText(locator)) != null) {
				// Reporter.addStepLog("Element identified with LinkText : " +
				// locator);
				return driver.findElement(By.linkText(locator));
			}
			break;
		case "tagName":
			if (FindElementSafe(By.tagName(locator)) != null) {
				// Reporter.addStepLog("Element identified with TagName : " +
				// locator);
				return driver.findElement(By.tagName(locator));
			}
			break;
		case "partialLinkText":
			if (FindElementSafe(By.partialLinkText(locator)) != null) {
				// Reporter.addStepLog("Element identified with Partial LinkText
				// : " + locator);
				return driver.findElement(By.partialLinkText(locator));
			}
			break;
		default:
			// Reporter.addStepLog("Element not available for do action....");
			System.out.println("Element not available....");
		}
		return null;
	}

	// Click Operation Method
	public static void clickElement(String locator, String type) throws Throwable {
		try {

			WebElement clickAnElement = locatorIdentifier(prop.getProperty(locator), type);
			// Reporter.addStepLog(prop.getProperty(locator)+" Element clicked
			// ...");
			clickAnElement.click();
		} catch (Exception e) {
			System.out.println(prop.getProperty(locator) + " Element not available for click operation...");
		}
	}

	// Click Radio Button Method
	public static void clickRadioElement(String locator, String type) throws Throwable {
		try {
			if (FindElementWithoutHighlightMe(By.xpath(prop.getProperty(locator))) != null) {
				WebElement clickAnElement = driver.findElement(By.xpath(prop.getProperty(locator)));
				// Reporter.addStepLog("Radio Button clicked ...");
				clickAnElement.click();
			}

		} catch (Exception e) {
			System.out.println("Radio Button not available for click operation...");
		}
	}

	// Insert Operation Method
	public static void insertValueToElement(String locator, String type, String value) throws Throwable {
		try {
			WebElement insertValueToElement = locatorIdentifier(prop.getProperty(locator), type);
			// Reporter.addStepLog(value + " is inserted to TextBox...");
			insertValueToElement.clear();
			insertValueToElement.sendKeys(prop.getProperty(value));
		} catch (Exception e) {
			System.out.println(value + " Element not available for Insert operation...");
		}
	}

	// Insert Operation Method
		public static void insertValueToElementwithoutprop(String locator, String type, String value) throws Throwable {
			try {
				WebElement insertValueToElement = locatorIdentifier(prop.getProperty(locator), type);
				// Reporter.addStepLog(value + " is inserted to TextBox...");
				insertValueToElement.clear();
				insertValueToElement.sendKeys(value);
			} catch (Exception e) {
				System.out.println(value + " Element not available for Insert operation...");
			}
		}
		
	// dropdown Without Select Operation Method
	public static void dropdownWithoutSelect(String locator, String type, String value) throws Throwable {
		try {

			WebElement dropdownWithSelect = locatorIdentifier(prop.getProperty(locator), type);
			Select sl = new Select(dropdownWithSelect);
			System.out.println("Selected Option : " + sl.getFirstSelectedOption());
			java.util.List<WebElement> options = dropdownWithSelect.findElements(By.tagName("option"));

			for (WebElement option : options) {
				if (option.equals(prop.getProperty(value))) {
					// Reporter.addStepLog(prop.getProperty(value) + " is
					// clicked from dropdown box...");
					option.click();
				}
			}
		} catch (Exception e) {
			System.out.println("Element not available for Insert operation...");
		}
	}

	// dropdown With Select Operation Method
	public static void dropdownWithSelect(String locator, String type, String selecttype, String selectvalue)
			throws Throwable {
		try {

			WebElement dropdownWithSelect = locatorIdentifier(prop.getProperty(locator), type);
			Select select = new Select(dropdownWithSelect);

			switch (selecttype) {
			case "ByIndex":
				// Reporter.addStepLog(Integer.parseInt(selectvalue) + " is
				// clicked from dropdown box...");
				select.selectByIndex(Integer.parseInt(selectvalue));
				break;
			case "ByVisibleText":
				// Reporter.addStepLog(selectvalue + " is clicked from dropdown
				// box...");
				select.selectByVisibleText(selectvalue);
				break;
			case "ByValue":
				// Reporter.addStepLog(selectvalue + " is clicked from dropdown
				// box...");
				select.selectByValue(selectvalue);
				break;

			default:
				System.out.println("Element not available....");
			}

		} catch (Exception e) {
			System.out.println("Element not available for Insert operation...");
		}
	}

	// screenshot method
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		// below line is just to append the date format with the screenshot name
		// to avoid duplicate names
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = System.getProperty("user.dir") + "\\Screenshots\\" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		// Returns the captured file path
		return destination;
	}

	// hover To method
	public static String hoverTo(String locator, String type, String locator1, String type1) throws Throwable {
		try {
			Sleep(1000);
			Actions act = new Actions(driver);
			// Reporter.addStepLog("Mouse hovered on : " +
			// prop.getProperty(locator));
			act.moveToElement(locatorIdentifier(prop.getProperty(locator), type)).build().perform();
			Sleep(200);
			clickElement(locator1, type1);
			return "PASSED";
		} catch (Exception e) {
			// driver.quit();
			// Reporter.addScreenCaptureFromPath("absolute screenshot path");
			// Reporter.addScreenCast("absolute screen cast path");
			return "FAILED";
		}
	}

	// checkbox method
	public static void CheckboxSelected(String locator, String type) throws Throwable {
		// WebElement checkbox = locatorIdentifier(locator, type);
		List<WebElement> checkbox = driver.findElements(By.xpath(prop.getProperty(locator)));

		if (!checkbox.get(2).isSelected())
			checkbox.get(2).click();
		// Reporter.addStepLog("Clicked On Checkbox... ");

	}

	public static void clickOnCheckbox(String locator, String type) throws Throwable {
		// WebElement checkbox = locatorIdentifier(locator, type);
		WebElement checkbox = driver.findElement(By.xpath(prop.getProperty(locator)));
		checkbox.click();
		// Reporter.addStepLog("Clicked On Checkbox... ");

	}
	// Close Browser
	public static String closeBrowser() {
		try {
			executionEndTime();
			// Reporter.addStepLog("Application closed successfully");
			driver.quit();
			return "PASSED";
		} catch (Exception e) {
			driver.quit();
			return "FAILED";
		}
	}

	// Switch to iFrame Method
	public static void switchToiFrame(String locator, String type, String selecttype, String selectvalue)
			throws Throwable {
		try {

			switch (selecttype) {
			case "Index":
				// Switch by Index
				driver.switchTo().frame(Integer.parseInt(selectvalue));
				break;
			case "Name":
				// Switch by frame name

				driver.switchTo().frame(selectvalue);
				break;
			case "Id":
				// Switch by frame name
				driver.switchTo().frame(selectvalue);
				break;
			case "WebElement":
				waitFor();
				// First find the element using any of prop.getProperty(locator)
				WebElement iframeElement = locatorIdentifier(prop.getProperty(locator), type);
				// now use the switch command
				driver.switchTo().frame(iframeElement);
				break;
			case "defaultContent":
				// Switch back to the main window
				driver.switchTo().defaultContent();
				break;
			default:
				System.out.println("Element not available....");
			}
		} catch (Exception e) {
			System.out.println("Element not available for Insert operation...");
		}
	}
	static String takeScreenshot() throws IOException {
		String dateName = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss").format(new Date());
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		String destFile = System.getProperty("user.dir") + "\\Screenshots\\screenshot_" + dateName + ".png";
		FileUtils.copyFile(scrFile, new File(destFile));
		i++;
		return destFile;
	}
	
	
	public static String findFutureDate(String arg3){
		SimpleDateFormat date=new SimpleDateFormat("dd/MM/yyyy");
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, Integer.parseInt(arg3));
		String output=date.format(c.getTime());
		return output;
		
	}
	
}
