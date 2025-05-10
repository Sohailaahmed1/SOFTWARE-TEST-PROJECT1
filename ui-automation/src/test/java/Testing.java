import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;

import utils.ExtentReportManager;

public class Testing {

    WebDriver driver;
    WebDriverWait wait;
    ExtentReports report;

    @BeforeClass
    public void setupReport() {
        // Initialize the ExtentReports instance
        report = ExtentReportManager.getReportInstance();
        System.setProperty("webdriver.chrome.driver", "C:/selenium webdriver/ChromeDriver/chromedriver-win32/chromedriver-win32/chromedriver.exe");
    }

    @BeforeMethod
    public void setup() {

        // Initialize WebDriver and WebDriverWait
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Navigate to the application URL
        try {
            driver.get("https://parabank.parasoft.com");
        } catch (org.openqa.selenium.NoSuchWindowException e) {
            System.out.println("Browser window was closed. Restarting driver...");
            setup(); // Restart the driver
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void closeReport() {
        if (report != null) {
            report.flush();
        }
    }
}
