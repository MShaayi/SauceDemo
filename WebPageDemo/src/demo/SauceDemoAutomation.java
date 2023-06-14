package demo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SauceDemoAutomation
{
    private WebDriver driver;

    @BeforeMethod
    public void setUp()
    {
    	//System.setProperty("webdriver.chrome.driver", "E:\\Shahid\\Drivers\\chromedriver.exe");
    	//driver = new ChromeDriver(); 
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\DELL\\Downloads\\geckodriver-v0.33.0-win64\\geckodriver.exe");
    	driver = new FirefoxDriver();
        System.out.println("Opening Browser");
        driver.manage().window().maximize();
    }

    @Test
    public void saucedemoAutomationTest() throws Exception
    {
    	driver.get("https://www.saucedemo.com/");
        System.out.println("Web page loaded");
        
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        usernameField.sendKeys("User");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();
        String errorMessage = driver.findElement(By.cssSelector("div.error-message-container")).getText();
        System.out.println("Error :" + errorMessage);
        
        usernameField.clear();
        passwordField.clear();
        
        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("123");
        loginButton.click();
        errorMessage = driver.findElement(By.cssSelector("div.error-message-container")).getText();
        System.out.println("Error :" + errorMessage);
        
        usernameField.clear();
        passwordField.clear();
        
        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();;
        WebElement inventoryContainer = driver.findElement(By.xpath(".//*[@id=\"inventory_container\"]"));
        Assert.assertTrue(inventoryContainer.isDisplayed(), "Login was unsuccessful.");
        System.out.println("Log in Succesfull");
        
        // ... Sorting
        WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
        sortDropdown.sendKeys("lohi");
        System.out.println("All items sorted");

        // ... adding to the cart
        driver.findElements(By.className("btn_inventory")).forEach(WebElement::click);
        System.out.println("Items Added");

        // ... items removal <$15
        WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
        cartLink.click();
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        for (WebElement item : cartItems)
        {
            WebElement priceElement = item.findElement(By.className("inventory_item_price"));
            String priceText = priceElement.getText();
            double price = Double.parseDouble(priceText.replace("$", ""));

            if (price < 15)
            {
                WebElement removeButton = item.findElement(By.className("cart_button"));
                removeButton.click();
            }
        }
        System.out.println("Items Below 15 Removed");

        // ... check out
        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();
        
        WebElement firstNameInput = driver.findElement(By.id("first-name"));
        WebElement lastNameInput = driver.findElement(By.id("last-name"));
        WebElement postalCodeInput = driver.findElement(By.id("postal-code"));
        WebElement continueButton = driver.findElement(By.id("continue"));
        
        continueButton.click();
        errorMessage = driver.findElement(By.cssSelector("div.error-message-container")).getText();
        System.out.println(errorMessage);

        firstNameInput.sendKeys("Shahid");
        continueButton.click();
        errorMessage = driver.findElement(By.cssSelector("div.error-message-container")).getText();
        System.out.println(errorMessage);
        
        lastNameInput.sendKeys("S");
        continueButton.click();
        errorMessage = driver.findElement(By.cssSelector("div.error-message-container")).getText();
        System.out.println(errorMessage);
        
        postalCodeInput.sendKeys("12345");
        continueButton.click();

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();
        Assert.assertTrue(driver.findElement(By.className("complete-header")).isDisplayed(), "Checkout failed");
        System.out.println("Checkout Complete");
        
        // ... home
        driver.findElement(By.id("back-to-products")).click();

        // ... logout
        WebElement menuButton = driver.findElement(By.xpath("//*[@id=\"react-burger-menu-btn\"]"));
        menuButton.click();
        WebElement logoutLink = driver.findElement(By.xpath("//*[@id=\"logout_sidebar_link\"]"));
        logoutLink.click();
        Assert.assertTrue(driver.findElement(By.id("login-button")).isDisplayed(), "Logout failed");
        System.out.println("Logged Out");
    }

    @AfterMethod
    public void tearDown() {
        // Quit the WebDriver instance
        driver.quit();
    }
}
