package Flipkart;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProductSelect {	
	
    WebDriver driver;
    WebDriverWait wait;

	
    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.flipkart.com/");
    }

    @Test(priority = 1)
    public void searchProduct() {

        // Close login popup if present
        try {
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//span[text()='✕']")));
            closeBtn.click();
        } catch (Exception e) {
            System.out.println("Login popup not displayed");
        }

        // Search for iPhone
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        searchBox.sendKeys("iphone");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test(priority = 2)
    public void applyBrandFilter() {

        WebElement appleFilter = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[text()='Apple']")));

        // Scroll into view
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", appleFilter);

        // JS click to avoid interception
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", appleFilter);
    }

    @Test(priority = 3, dependsOnMethods = "applyBrandFilter")
    public void selectProductAndCaptureDetails() {

        // Wait until at least one product link is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href,'/p/')]")));

        // Get first product link
        WebElement firstProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//a[contains(@href,'/p/')])[1]")));

        // Scroll into view
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", firstProduct);

        // Click product safely
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", firstProduct);

        // Switch to product details tab
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
        }

        // Capture product name
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(@class,'B_NuCI')]")));

        // Capture product price
        WebElement productPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'_30jeq3')]")));

        System.out.println("Product Name : " + productName.getText());
        System.out.println("Product Price: " + productPrice.getText());
    }
        
        
        @Test(priority = 4, dependsOnMethods = "selectProductAndCaptureDetails")
        public void cartAndCheckoutFlow() 
        {
    
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Step 8: Add product to cart
            WebElement addToCartBtn = longWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Add to cart')]")));

            addToCartBtn.click();

            // Step 9: Verify product added to cart
            WebElement cartTitle = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//span[contains(text(),'My Cart')]")));

            Assert.assertTrue(cartTitle.isDisplayed(), "Product not added to cart");
            System.out.println("Product successfully added to cart");

            // Step 10: Click Place Order
            WebElement placeOrderBtn = longWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button/span[text()='Place Order']")));

            placeOrderBtn.click();

            // Step 11: Enter dummy mobile number
            WebElement mobileInput = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[@type='text' or @maxlength='10']")));

            mobileInput.sendKeys("9999999999"); // Dummy number only

            // Step 12: Click Continue
            WebElement continueBtn = longWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button/span[text()='CONTINUE']")));

            continueBtn.click();

            // Step 13: Navigate to payment options page
            WebElement paymentSection = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(text(),'Payment Options')]")));

            Assert.assertTrue(paymentSection.isDisplayed(),
                    "Payment options page not displayed");

            // Step 14: Select one payment option (Cash on Delivery example)
            WebElement codOption = longWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(text(),'Cash on Delivery')]")));

            codOption.click();

            // Step 15: Validate selected payment option
            WebElement selectedCOD = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(@class,'_2YsvKq') or contains(text(),'Cash on Delivery')]")));

            Assert.assertTrue(selectedCOD.isDisplayed(),
                    "Selected payment option not displayed correctly");

            System.out.println("Payment option selected successfully");

        }

    

	
    


}
    
    

