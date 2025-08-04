package base;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.aventstack.extentreports.Status;
import reporting.Extent;
import org.openqa.selenium.TimeoutException;
import screenshot.ScreenShots;


public class base {

	protected static WebDriver driver;
	protected ExtentReports reports;
	protected static ExtentTest test;
	protected Extent headerExtent;
	protected static WebDriverWait wait;
	protected SoftAssert softAssert;
	protected JavascriptExecutor js;


	
	@BeforeMethod
	public void LaunchBrowser() {

		WebDriverManager.chromedriver().setup();
		ChromeOptions options=new ChromeOptions();
		// options.addArguments("headless");
		options.addArguments("--disable-notifications");
		driver=new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.navigate().to("https://recruiter.shine.com/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));
		softAssert = new SoftAssert(); // ✅ SoftAssert instance
		wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		js=((JavascriptExecutor)driver);

	}	


	@BeforeTest
	public void setupReport() {
		cleanScreenshotFolders();
		// Use the class name (e.g., Test_MBA_General) as the program name
	    String programName = this.getClass().getSimpleName();
		reports = Extent.generateReport(programName);
	}

	@AfterTest
	public void flushReport() {
		reports.flush();
	}

	public void cleanScreenshotFolders() {
		String screenshotDir = System.getProperty("user.dir") + "/Reports/Screenshot";
		deleteFilesInFolder(screenshotDir);
	}

	public void deleteFilesInFolder(String folderpath) {
		File folder=new File(folderpath);
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				if (file.isDirectory()) {
					deleteFilesInFolder(file.getAbsolutePath()); // recursive delete


				}else {
					boolean deleted = file.delete();
					if (!deleted) {
						System.out.println("Failed to delete: " + file.getAbsolutePath());
					}
				}
			}
		}

	}

	// Closing popup if appears
	   public void closePopupIfPresent() {
	       try {
	           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(33));
	           WebElement popupCloseButton = wait.until(
	                   ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='bg-white padding-0-imp Modal_callUsModalClass__gzcBP Modal_dialog__e3Pgf']//*[name()='svg']")));
	           popupCloseButton.click();
	           System.out.println("30 second Popup closed.");
	       } catch (Exception e) {
	           System.out.println("30 second No popup found.");
	       }
	   }
    

    @AfterMethod
	public void CloseBrowser(ITestResult result) throws IOException {
		String testname=result.getName();
		String screenshotPath =ScreenShots.takeScreenshot(driver, testname);
		// Log result based on status
		if (screenshotPath != null) {
	        switch (result.getStatus()) {
	            case ITestResult.FAILURE:
	                test.fail("❌ Test Failed: " + result.getThrowable());
	                test.addScreenCaptureFromPath(screenshotPath);
	                break;

	            case ITestResult.SUCCESS:
	                test.pass("✅ Test Passed");
	                test.addScreenCaptureFromPath(screenshotPath);
	                break;

	            case ITestResult.SKIP:
	                test.skip("⚠️ Test Skipped");
	                test.addScreenCaptureFromPath(screenshotPath);
	                break;
	        }
	    } else {
	        test.warning("⚠️ Screenshot could not be captured.");
	    }
	 // Quit the browser instance
	    if (driver != null) {
	        driver.quit();
	    }
	}
    
    public void closescholarshippopup() {
		 try {
			 WebDriverWait wait2=new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement popscholarship = wait2.until(ExpectedConditions.elementToBeClickable(By.cssSelector("path[fill='#fff'][stroke='#fff']")));
		popscholarship.click();
		System.out.println("Scholarship Popup closed.");
 } catch (Exception e) {
     System.out.println("No Scholarship popup found.");
 }
}
    
    public WebElement waitForElementWithTiming(Function<WebDriver, WebElement> condition, String elementName) {
	    WebElement element = null;
	    long startTime = System.currentTimeMillis(); // Start time in milliseconds
	    try {
	        element = new WebDriverWait(driver, Duration.ofSeconds(30)).until(condition); // Try to locate the element
	    } catch (Exception e) {
	        long endTime = System.currentTimeMillis(); // End time in milliseconds
	        long durationInSeconds = (endTime - startTime) / 1000; // Convert to seconds
	        test.log(Status.FAIL, "❌ Failed to load element '" + elementName + "' in " + durationInSeconds + " sec: " + e.getMessage());
	        softAssert.fail("Element '" + elementName + "' failed to load: " + e.getMessage());
	    }
	    return element;
	}

	 // ✅ Add this once here
    protected String formatSeconds(long nanos) {
        return String.format("%.2f", (double) nanos / 1_000_000_000) + " seconds";
    }
    
    public boolean handlePostOTPVerification(WebDriver driver, WebDriverWait wait, ExtentTest test, SoftAssert softAssert, WebElement startApplicationElement) {
        try {
            // Check for the "Thank You" message
            WebElement thankYouMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'Thank You For Your Interest')]")));
            
            if (thankYouMessage.isDisplayed()) {
                test.log(Status.SKIP, "API may have failed. Received Thank You message after OTP verification.");
                test.info("Displayed Message: " + thankYouMessage.getText());
                System.out.println("Flow Stopped: " + thankYouMessage.getText());
                return false; // Stop further execution
            }
        } catch (TimeoutException e) {
            test.info("No Thank You message found – continuing to check for Start Application button.");
        }

        // Check for Start Application button
        try {
            WebElement isSuccess = wait.until(ExpectedConditions.elementToBeClickable(startApplicationElement));
            if (isSuccess.isEnabled()) {
                softAssert.assertTrue(true, "Start Application button enabled check");
                test.log(Status.PASS, "Apply Now Journey Successful");
                System.out.println("Apply Now Journey Successful");
            } else {
                test.log(Status.FAIL, "Start Application button not enabled");
                softAssert.fail("Start Application button not enabled");
                System.out.println("Apply Now Journey Failed");
            }
        } catch (TimeoutException e) {
            test.log(Status.FAIL, "Start Application button did not appear: " + e.getMessage());
            softAssert.fail("Start Application button not found");
            System.out.println("Apply Now Journey Failed - Button not found");
        }

        return true;
    }

    
//--------------check visual error on screen.--------------------------  
    
    public void checkVisualErrorsOnScreen() {
        List<String> errorTexts = Arrays.asList(
            "404 Not Found", "404 - Page Not Found", "Something went wrong!", "500 Internal Server Error", "502 Bad Gateway",
            "503 Service Unavailable", "504 Gateway Timeout",
            "0:[\"live_19_03_24_1\"", "We’re currently experiencing heavy traffic. Please try again later.", "403 Forbidden", "401 Unauthorized",
            "400 Bad Request", "408 Request Timeout", "405 Method Not Allowed",
            "This page isn’t working", "HTTP ERROR 502",
            "{\"statusCode\":502,\"description\":\"Bad Gateway\"}",
            "{\"statusCode\":504,\"description\":\"Gateway Timeout\"}"
        );

        try {
            String fullPageText = driver.findElement(By.tagName("body")).getText();
            for (String error : errorTexts) {
                if (fullPageText.contains(error)) {
                    test.log(Status.FAIL, "Page displayed error text: " + error);
                    System.out.println("❌ Error detected on screen: " + error);
                    softAssert.fail("Visual error message found on screen: " + error);
                    break;
                }
            }
        } catch (Exception e) {
            test.log(Status.WARNING, "Error checking screen content: " + e.getMessage());
        }
    }


}
