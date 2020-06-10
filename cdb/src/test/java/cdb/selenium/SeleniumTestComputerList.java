package cdb.selenium;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTestComputerList {
    private WebDriver driver;
   
    @BeforeClass
    public void driverInit() {
        System.setProperty("webdriver.gecko.driver", "/opt/WebDriver/bin/geckodriver");
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
    }
    @Before
    public void testInit() {
        
    }
    
    @Test
    public void dashboardTest() {
        driver.get("http://localhost:8080/cdb/list");
    }
}
