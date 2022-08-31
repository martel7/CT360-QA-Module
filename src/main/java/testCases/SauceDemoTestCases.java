package testCases;

import dev.failsafe.internal.util.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import helpers.helperClass;

public class SauceDemoTestCases {

    public WebDriver driver;

    @BeforeAll
    public static void setDriver(){
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
    }

    @BeforeEach
    public void createDriver(){
        driver = new EdgeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterEach
    public void quitDriver(){
        driver.quit();
    }

    private void standarUserLogin(){

        String username = "standard_user";
        String password = "secret_sauce";

        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();

        //login check:
        String homepageLink = driver.getCurrentUrl();
        assertEquals("https://www.saucedemo.com/inventory.html", homepageLink, "Invalid login");
    }

    private void logout(){

        driver.findElement(By.id("react-burger-menu-btn")).click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("logout_sidebar_link")).click();
    }

    private void checkout(){

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
    public void TestCase1() {

        this.standarUserLogin();

        //"PRODUCTS" header check:
        driver.findElement(By.xpath("//span[contains(text(), 'Products')]"));


        //shopping cart check:
        driver.findElement(By.id("shopping_cart_container"));

        //burger menu check:
        driver.findElement(By.id("react-burger-menu-btn"));

        //socials links checks:
        driver.findElement(By.className("social_twitter"));
        driver.findElement(By.className("social_facebook"));
        driver.findElement(By.className("social_linkedin"));

        //logout
        this.logout();
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
    public void TestCase2() {

        this.standarUserLogin();

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

        //Checkout
        this.checkout();

        //-Logout
        this.logout();
    }

    /*Login
    * Get all the products in an array/list and add them all to the Cart
    * Remove them all from cart
    * Logout*/
    @Test
    public void AddAllItemsToCartAndDeleteThem(){

        //Login:
        this.standarUserLogin();

        //Get all the products in an array/list and add them all to the Cart
        List<WebElement> allProducts = driver.findElements(By.xpath("//div[@class = 'pricebar']/button"));
        for (WebElement product : allProducts)
            product.click();

        //Remove them all from cart
        //Navigate to cart firstly:
        driver.findElement(By.xpath("//a[contains(@class, 'shopping_cart_link')]")).click();
        //wait maybe?
        allProducts = driver.findElements(By.xpath("//div[@class = 'item_pricebar']/button[text() = 'Remove']"));
        for (WebElement product : allProducts)
            product.click();

        //Logout
        this.logout();
    }

    @Test
    public void EmptyCartCheckoutTest(){

        this.standarUserLogin();
        this.checkout();

        Assertions.fail("The site is allowing to checkout with an empty cart");
    }

    @Test
    public void FilterTests() throws InterruptedException {

        this.standarUserLogin();


        /*WebElement filterMenu = driver.findElement(By.xpath("//select[@data-test = 'product_sort_container']"));
        Select filters = new Select(filterMenu);

        filters.selectByIndex(0);

        filters.selectByIndex(1);

        filters.selectByIndex(2); ------------> Not working ????

        filters.selectByIndex(3); ------------> Not working ????

        WebElement x = driver.findElement(By.xpath("//select[@class = 'product_sort_container']"));*/

        List<WebElement> allProducts;

        //Checking A-Z
        driver.findElement(By.xpath("//option[@value = 'az']")).click();
        allProducts = driver.findElements(By.xpath("//div[@class = 'inventory_item_name']"));
        if(helperClass.isSortedAlphanum(allProducts, "a-z") == false)
            Assertions.fail("Items not sorted correctly: a-z");

        //Checking Z-A
        driver.findElement(By.xpath("//option[@value = 'za']")).click();
        allProducts = driver.findElements(By.xpath("//div[@class = 'inventory_item_name']"));
        if(helperClass.isSortedAlphanum(allProducts, "z-a") == false)
            Assertions.fail("Items not sorted correctly: z-a");

        //Checking Price low to high
        driver.findElement(By.xpath("//option[@value = 'lohi']")).click();
        allProducts = driver.findElements(By.xpath("//div[@class = 'inventory_item_price']"));
        if(helperClass.isSortedNum(allProducts, "l-h") == false)
            Assertions.fail("Items not sorted correctly: low to high");

        //Checking Price high to low
        driver.findElement(By.xpath("//option[@value = 'hilo']")).click();
        allProducts = driver.findElements(By.xpath("//div[@class = 'inventory_item_price']"));
        if(helperClass.isSortedNum(allProducts, "h-l") == false)
            Assertions.fail("Items not sorted correctly: high to low");

    }
}
