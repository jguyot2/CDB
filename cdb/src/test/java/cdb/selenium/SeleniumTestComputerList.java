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
    public void driverInit() {
        System.setProperty("webdriver.gecko.driver", "/opt/WebDriver/bin/geckodriver");
    }

    @Before
    public void resetDriver() {
        driver = new FirefoxDriver();
    }

    @After
    public void closeDriver() {
        driver.close();
    }

    @Test
    public void dashboardTest() {
        driver.get(BASE_PATH + "page");
        WebElement buttonAddComputer = driver.findElement(By.id("addComputer"));
        buttonAddComputer.click();
        assertTrue(true);
    }

    @Test
    public void addComputerTest() {
        driver.get(BASE_PATH + "addComputer");
        WebElement form = driver.findElement(By.name("addComputer"));
        WebElement nameField = driver.findElement(By.id("computerName"));
        WebElement introducedField = driver.findElement(By.id("introduced"));
        WebElement discontinuedField = driver.findElement(By.id("discontinued"));
        WebElement companyIdField = driver.findElement(By.id("companyId"));

        nameField.sendKeys("nouveauOrdinateur");

    }
}
