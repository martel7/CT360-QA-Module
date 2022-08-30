package testCases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SauceDemoTestCases {

    public WebDriver driver;

    @BeforeAll
    public static void setDriver(){
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
    }

    @BeforeEach
    public void createDriver(){
        driver = new EdgeDriver();
    }

    @AfterEach
    public void quitDriver(){
        driver.quit();
    }


    /*
    Test case 1 from the assignment:
        - open url https://www.saucedemo.com/
        - Login with valid user and password (standard_user/secret_sauce)
        - Verify following web elements are present on the products home page:
        - "PRODUCTS" header
        - shopping cart
        - burger menu in the uper left corner
        - Twitter, Facebook, Linkedin links
        - Logout
    */
    @Test
    public void testCase1() {

        driver.get("https://www.saucedemo.com/");

        String username = "standard_user";
        String password = "secret_sauce";

        WebElement usernameField = driver.findElement(By.id("user-name"));
        usernameField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);

        driver.findElement(By.id("login-button")).click();

        //login check:
        String homepageLink = driver.getCurrentUrl();
        assertEquals("https://www.saucedemo.com/inventory.html", homepageLink, "Invalid login");

        //"PRODUCTS" header check:
        driver.findElement(By.xpath("//span[contains(text(), 'Products')]"));


        //shopping cart check:
        driver.findElement(By.id("shopping_cart_container"));

        //burger menu check:
        WebElement burgerMenu = driver.findElement(By.id("react-burger-menu-btn"));

        //socials links checks:
        driver.findElement(By.className("social_twitter"));
        driver.findElement(By.className("social_facebook"));
        driver.findElement(By.className("social_linkedin"));

        //logout
        WebElement logoutButton = driver.findElement(By.id("logout_sidebar_link"));
        burgerMenu.click(); //!!!!!!!!!!!
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        logoutButton.click();
    }


    /*Test case 1 from the assignment:
        - open url https://www.saucedemo.com/
        - Login with valid user and password (standard_user/secret_sauce)
        - Click on the "Sauce Labs Backpack" item
        - verify title, description and price of this item
        - click on the button "ADD TO CART"
        - Click on the button "BACK TO PRODUCTS"
        - From product home page click "ADD TO CART" button for "Sauce Labs Fleece Jacket" item
        - Click on the "Shopping Cart" button to open Shopping Cart page
        - Click on the "Chechout" button to continue with order
        - Enter Firstname, Lastname, Zipcode and click on Finish button
        - Verify "THANK YOU FOR YOUR ORDER" is displayed
        - Logout
    */

    @Test
    public void testCase2() {

        driver.get("https://www.saucedemo.com/");

        String username = "standard_user";
        String password = "secret_sauce";

        WebElement usernameField = driver.findElement(By.id("user-name"));
        usernameField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);

        WebElement submitButton = driver.findElement(By.id("login-button"));
        submitButton.click();

        //login check:
        String homepageLink = driver.getCurrentUrl();
        assertEquals("https://www.saucedemo.com/inventory.html", homepageLink, "Invalid login");

        //checking whether image of the item exits (Image is also clickable and leads to item page)
        WebElement backpackImg = driver.findElement(By.xpath("//img[contains(@alt, 'Sauce Labs Backpack')]"));
        backpackImg.click();
        driver.navigate().back();

        //checking whether text of the item exits (Image is also clickable and leads to item page)
        WebElement backpackText = driver.findElement(By.xpath("//a[contains(@id, 'item_4_title_link')]"));
        /*https://stackoverflow.com/questions/27208398/xpath-divcontainstext-string-fails-to-select-divs-containing-string*/
        backpackText.click();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(1));

        //checking the title:
        driver.findElement(By.xpath("//div[contains(., 'Sauce Labs Backpack')]"));

        //checking the desc.
        WebElement description = driver.findElement(By.xpath("//div[contains(@class, 'inventory_details_desc large_size')]"));
        //xpath can also be: "//div[@class = 'inventory_details_desc large_size']"
        String rightDescription = "carry.allTheThings() with the sleek, streamlined Sly Pack that" +
                " melds uncompromising style with unequaled laptop and tablet protection.";
        assertEquals(rightDescription, description.getText());

        //checking the price
        WebElement price = driver.findElement(By.xpath("//div[@class = 'inventory_details_price']"));
        String rightPrice = "$29.99";
        assertEquals(rightPrice, price.getText());

        //- From product home page click "ADD TO CART" button for "Sauce Labs Fleece Jacket" item:
        driver.navigate().back();
        driver.findElement(By.xpath("//button[@id = 'add-to-cart-sauce-labs-backpack']")).click();

        //- Click on the "Shopping Cart" button to open Shopping Cart page
        driver.findElement(By.xpath("//a[contains(@class, 'shopping_cart_link')]")).click();

        //- Click on the "Chechout" button to continue with order
        driver.findElement(By.xpath("//button[@id = 'checkout']")).click();

        //- Enter Firstname, Lastname, Zipcode and click on Finish button
        String firstname = "Dusan";
        String lastname = "Markovic";
        String zipcode = "34000";

        driver.findElement(By.xpath("//input[@id = 'first-name']")).sendKeys(firstname);
        driver.findElement(By.xpath("//input[@id = 'last-name']")).sendKeys(lastname);
        driver.findElement(By.xpath("//input[@id = 'postal-code']")).sendKeys(zipcode);

        driver.findElement(By.xpath("//input[@id = 'continue']")).click();

        driver.findElement(By.xpath("//button[@id = 'finish']")).click();


        //- Verify "THANK YOU FOR YOUR ORDER" is displayed
        String msg = driver.findElement(By.xpath("//h2[@class = 'complete-header']")).getText();
        assertEquals("THANK YOU FOR YOUR ORDER", msg, "Wrong message. Checkout not completed");

        //-Logout
        driver.findElement(By.id("react-burger-menu-btn")).click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("logout_sidebar_link")).click();
    }

    /*Login
    * Get all the products in an array/list and add them all to the Cart
    * Checkout
    * Logout*/
    @Test
    public void testCase3(){

        driver.get("https://www.saucedemo.com/");

        String username = "standard_user";
        String password = "secret_sauce";

        driver.findElement(By.xpath("//input[@id = 'user-name']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id = 'password']")).sendKeys(password);
    }
}
