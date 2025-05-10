import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

public class ParabankUITests extends Testing {

    ExtentTest test;
    

    public ParabankUITests() {
    }

    // ------------------------ LOGIN TESTS ------------------------
    @Test(dataProvider = "validCredentials")
    public void TC01_validLogin(String username, String password) {
        test = report.createTest("Login Test - username password");
        login(username, password);
        Assert.assertTrue(driver.getTitle().contains("ParaBank | Accounts Overview"));
        test.pass("Login Test Passed");
    }

    @Test(dataProvider = "invalidCredentials")
    public void TC02_invalidLogin(String username, String password) {
        login(username, password);
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='rightPanel']/h1")));
        Assert.assertTrue(error.getText().toLowerCase().contains("error"));
    }

    @Test
    public void TC03_blankCredentials() {
        login("", "");
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='rightPanel']/h1")));
        Assert.assertTrue(error.isDisplayed());
    }

    @Test
    public void TC04_validateErrorMessage() {
        login("invalid", "invalid");
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='rightPanel']/h1")));
        
        Assert.assertEquals(error.getText(), "The username and password could not be verified.");
        test.pass("Error Message Validation Test Passed");

    }

    // ------------------------ REGISTRATION TESTS ------------------------
    @Test
    public void TC05_navigateToRegistrationPage() {
        driver.findElement(By.linkText("Register")).click();
        Assert.assertTrue(driver.getTitle().contains("Register"));
    }

    @Test
    public void TC06_submitValidRegistration() {
        driver.findElement(By.linkText("Register")).click();
        fillRegistrationForm("John", "Doe", "123 St", "City", "State", "12345", "1234567890", "ssn123", "john123", "password", "password");
        Assert.assertTrue(driver.getPageSource().contains("Your account was created successfully"));
    }

    @Test
    public void TC07_submitWithMissingFields() {
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("customer.firstName")).sendKeys("John");
        driver.findElement(By.cssSelector("input[value='Register']")).click();
        Assert.assertTrue(driver.getPageSource().contains("is required"));
    }

    @Test
    public void TC08_registerWithExistingUsername() {
        driver.findElement(By.linkText("Register")).click();
        fillRegistrationForm("John", "Doe", "123 St", "City", "State", "12345", "1234567890", "ssn123", "john", "password", "password");
        Assert.assertTrue(driver.getPageSource().contains("This username already exists"));
    }

    // ------------------------ ACCOUNT OVERVIEW ------------------------
    @Test
    public void TC09_accountOverviewAfterLogin() {
        login("john", "demo");
        Assert.assertTrue(driver.getTitle().contains("Accounts Overview"));
    }

    @Test
    public void TC10_clickAccountNumber() {
        login("john", "demo");
        driver.findElement(By.cssSelector("a[href*='activity']")).click();
        Assert.assertTrue(driver.getTitle().contains("Account Details"));
    }

    @Test
    public void TC11_verifyBalanceAndTransactions() {
        login("john", "demo");
        driver.findElement(By.cssSelector("a[href*='activity']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Balance"));
        Assert.assertTrue(driver.getPageSource().contains("Transaction"));
    }

    // ------------------------ OPEN NEW ACCOUNT ------------------------
    @Test
    public void TC12_navigateToOpenAccount() {
        login("john", "demo");
        driver.findElement(By.linkText("Open New Account")).click();
        Assert.assertTrue(driver.getTitle().contains("Open New Account"));
    }

    @Test
    public void TC13_openNewAccount() {
        login("john", "demo");
        driver.findElement(By.linkText("Open New Account")).click();
        new Select(driver.findElement(By.id("type"))).selectByVisibleText("SAVINGS");
        driver.findElement(By.cssSelector("input.button"))
              .click();
        Assert.assertTrue(driver.getPageSource().contains("Account Opened"));
    }

    @Test
    public void TC14_verifyAccountInOverview() {
        login("john", "demo");
        driver.findElement(By.linkText("Open New Account")).click();
        driver.findElement(By.cssSelector("input.button")).click();
        driver.findElement(By.linkText("Accounts Overview")).click();
        Assert.assertTrue(driver.getPageSource().contains("Account"));
    }

    // ------------------------ TRANSFER FUNDS ------------------------
    @Test
    public void TC15_navigateToTransferFunds() {
        login("john", "demo");
        driver.findElement(By.linkText("Transfer Funds")).click();
        Assert.assertTrue(driver.getTitle().contains("Transfer Funds"));
    }

    @Test
    public void TC16_validTransferFunds() {
        login("john", "demo");
        driver.findElement(By.linkText("Transfer Funds")).click();
        driver.findElement(By.id("amount")).sendKeys("100");
        driver.findElement(By.cssSelector("input.button"))
            .click();
        Assert.assertTrue(driver.getPageSource().contains("Transfer Complete!"));
    }

    @Test
    public void TC17_transferFundsMissingData() {
        login("john", "demo");
        driver.findElement(By.linkText("Transfer Funds")).click();
        driver.findElement(By.cssSelector("input.button")).click();
        Assert.assertTrue(driver.getPageSource().contains("must be greater than zero"));
        test.pass("Transfer Funds Missing Data Test Passed");
    }

    // ------------------------ BILL PAY ------------------------
    @Test
    public void TC18_navigateToBillPay() {
        login("john", "demo");
        driver.findElement(By.linkText("Bill Pay")).click();
        Assert.assertTrue(driver.getTitle().contains("Bill Payment Service"));
    }

    @Test
    public void TC19_submitValidBillPay() {
        login("john", "demo");
        driver.findElement(By.linkText("Bill Pay")).click();
        driver.findElement(By.name("payee.name")).sendKeys("Electric Company");
        driver.findElement(By.name("payee.address.street")).sendKeys("123 Elm St");
        driver.findElement(By.name("payee.address.city")).sendKeys("City");
        driver.findElement(By.name("payee.address.state")).sendKeys("State");
        driver.findElement(By.name("payee.address.zipCode")).sendKeys("12345");
        driver.findElement(By.name("payee.phoneNumber")).sendKeys("1234567890");
        driver.findElement(By.name("payee.accountNumber")).sendKeys("111111");
        driver.findElement(By.name("verifyAccount")).sendKeys("111111");
        driver.findElement(By.name("amount")).sendKeys("50");
        driver.findElement(By.cssSelector("input[type='submit']"))
          .click();
        Assert.assertTrue(driver.getPageSource().contains("Bill Payment Complete"));
    }

    @Test
    public void TC20_billPayWithMissingFields() {
        login("john", "demo");
        driver.findElement(By.linkText("Bill Pay")).click();
        driver.findElement(By.cssSelector("input[type='submit']"))
         .click();
        Assert.assertTrue(driver.getPageSource().contains("is required"));
    }

    // ------------------------ UPDATE CONTACT INFO ------------------------
    @Test
    public void TC21_navigateToUpdateContactInfo() {
        login("john", "demo");
        driver.findElement(By.linkText("Update Contact Info")).click();
        Assert.assertTrue(driver.getTitle().contains("Update Profile"));
    }

    @Test
    public void TC22_updateAddressPhone() {
        login("john", "demo");
        driver.findElement(By.linkText("Update Contact Info")).click();
        driver.findElement(By.id("customer.address.street")).clear();
        driver.findElement(By.id("customer.address.street")).sendKeys("456 New St");
        driver.findElement(By.id("customer.phoneNumber")).clear();
        driver.findElement(By.id("customer.phoneNumber")).sendKeys("9876543210");
        driver.findElement(By.cssSelector("input[type='submit']"))
         .click();
        Assert.assertTrue(driver.getPageSource().contains("updated successfully"));
    }

    @Test
    public void TC23_submitContactInfoWithMissingFields() {
        login("john", "demo");
        driver.findElement(By.linkText("Update Contact Info")).click();
        driver.findElement(By.id("customer.firstName")).clear();
        driver.findElement(By.cssSelector("input[type='submit']"))
         .click();
        Assert.assertTrue(driver.getPageSource().contains("is required"));
    }

    // ------------------------ LOGOUT ------------------------
    @Test
    public void TC24_logoutFunctionality() {
        login("john", "demo");
        driver.findElement(By.linkText("Log Out")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login.htm"));
    }

    @Test
    public void TC25_accessAfterLogout() {
        login("john", "demo");
        driver.findElement(By.linkText("Log Out")).click();
        driver.get("https://parabank.parasoft.com/parabank/overview.htm");
        Assert.assertTrue(driver.getCurrentUrl().contains("login.htm"));
    }

    // ------------------------ HELPERS ------------------------
    private void login(String username, String password) {
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='submit']"))
        .click();
    }

    private void fillRegistrationForm(String firstName, String lastName, String address, String city, String state,
     String zip, String phone, String ssn, String username, String pwd, String confirmPwd) {
        driver.findElement(By.id("customer.firstName")).sendKeys(firstName);
        driver.findElement(By.id("customer.lastName")).sendKeys(lastName);
        driver.findElement(By.id("customer.address.street")).sendKeys(address);
        driver.findElement(By.id("customer.address.city")).sendKeys(city);
        driver.findElement(By.id("customer.address.state")).sendKeys(state);
        driver.findElement(By.id("customer.address.zipCode")).sendKeys(zip);
        driver.findElement(By.id("customer.phoneNumber")).sendKeys(phone);
        driver.findElement(By.id("customer.ssn")).sendKeys(ssn);
        driver.findElement(By.id("customer.username")).sendKeys(username);
        driver.findElement(By.id("customer.password")).sendKeys(pwd);
        driver.findElement(By.id("repeatedPassword")).sendKeys(confirmPwd);
        driver.findElement(By.cssSelector("input[value='Register']"))
        .click();
    }

    @DataProvider(name = "validCredentials")
    public Object[][] validData() {
        return new Object[][] {
            {"john", "demo"},
        };
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidData() {
        return new Object[][] {
            {"wrong", "wrong"},
            {"", "wrong"},
            {"john", ""},
        };
    }

    @DataProvider(name = "registrationData")
    public Object[][] registrationData() {
        return new Object[][] {
            {"John", "Doe", "123 St", "City", "State", "12345", "1234567890", "ssn123", "john123", "password", "password"},
            {"Jane", "Smith", "456 St", "City2", "State2", "67890", "0987654321", "ssn456", "jane456", "password2", "password2"},
        };
    }
}

