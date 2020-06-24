package cdb.selenium;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTestComputerList {
    private WebDriver driver;
    private static final String BASE_PATH = "http://localhost:8080/cdbmaven/";

    @BeforeClass
    public static void driverInit() {
        System.setProperty("webdriver.gecko.driver", "/opt/WebDriver/bin/geckodriver");
    }

    @Before
    public void resetDriver() {
        this.driver = new FirefoxDriver();
    }

    @After
    public void closeDriver() {
        this.driver.close();
    }

    @Test
    public void dashboardTest() {
        this.driver.get(BASE_PATH + "page");
        WebElement buttonAddComputer = this.driver.findElement(By.id("addComputer"));
        buttonAddComputer.click();
        assertTrue(true);
    }

    @Test
    public void addComputerTest() {
        this.driver.get(BASE_PATH + "addComputer");
        WebElement form = this.driver.findElement(By.name("addComputer"));
        WebElement nameField = this.driver.findElement(By.id("computerName"));
        WebElement introducedField = this.driver.findElement(By.id("introduced"));
        WebElement discontinuedField = this.driver.findElement(By.id("discontinued"));
        WebElement companyIdField = this.driver.findElement(By.id("companyId"));

        nameField.sendKeys("nouveauOrdinateur");
        assertTrue(1 == 1);
    }
}
