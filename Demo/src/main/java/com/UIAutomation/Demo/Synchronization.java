package com.UIAutomation.Demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public abstract class Synchronization {

	public WebDriver driver;

	public Synchronization(WebDriver driver) {
		this.driver = driver;
	}

	public void verify_Title(String title) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.titleContains(title));
			Reporter.log("title is matching", true);
		} catch (Exception e) {
			Reporter.log("title is not matching", true);
			Assert.fail();
		}
	}

	public void verifyElement(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(element));
			Reporter.log("element is present", true);
		} catch (Exception e) {
			Reporter.log("element is not present", true);
			Assert.fail();
		}

	}

}
