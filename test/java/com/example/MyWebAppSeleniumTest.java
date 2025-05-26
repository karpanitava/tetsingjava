package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyWebAppSeleniumTest {

    private WebDriver driver;
    private final String BASE_URL = "http://localhost:8080/mywebapp/"; // Adjust if your context path is different

    @BeforeAll
    static void setupClass() {
        // Automatically download and set up the ChromeDriver
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        // Optional: Run Chrome in headless mode (no visible browser UI) for CI/CD
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        // options.addArguments("--disable-gpu"); // Needed for headless on some OS

        driver = new ChromeDriver(options);
        driver.manage().window().maximize(); // Maximize the browser window
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser after each test
        }
    }

    @Test
    void testFormSubmissionAndGreeting() {
        driver.get(BASE_URL); // Navigate to the index page

        // Verify the presence of the welcome heading
        WebElement heading = driver.findElement(By.tagName("h1"));
        assertEquals("Welcome to My Java Web App!", heading.getText());

        // Find the input field by its name attribute
        WebElement nameInput = driver.findElement(By.name("userName"));
        nameInput.sendKeys("Selenium Tester"); // Type a name

        // Find and click the submit button
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton.click();

        // After submission, we should be on result.jsp
        // Verify the URL
        assertTrue(driver.getCurrentUrl().contains("result.jsp"));

        // Verify the greeting message on the result page
        WebElement greetingMessage = driver.findElement(By.tagName("h1"));
        assertEquals("Hello, Selenium Tester!", greetingMessage.getText());

        // Test going back to the home page
        WebElement backLink = driver.findElement(By.linkText("Go back to the home page"));
        backLink.click();
        assertTrue(driver.getCurrentUrl().contains("index.jsp"));
    }

    @Test
    void testFormSubmissionWithEmptyName() {
        driver.get(BASE_URL);

        WebElement nameInput = driver.findElement(By.name("userName"));
        nameInput.sendKeys(""); // Submit an empty name

        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        submitButton.click();

        assertTrue(driver.getCurrentUrl().contains("result.jsp"));

        WebElement greetingMessage = driver.findElement(By.tagName("h1"));
        assertEquals("Hello, Guest!", greetingMessage.getText()); // Expect "Guest" due to servlet logic
    }
}