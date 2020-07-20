package com.UIAutomation.Demo;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pageObjects.MyStore;

public class MyStore_tests extends Base_Class {
	public static Logger log = LogManager.getLogger(Base_Class.class.getName());

	@BeforeMethod

	public void openBrowser() throws IOException {
		driver = driverInitialize();
		log.info("Driver is initialized...");
		driver.manage().window().maximize();
		driver.get("http://automationpractice.com");
		log.info("Navigated to MyCart Page...");
	}

	@Test(priority = 1)

	public void AddLessPriceItemToCart() throws InterruptedException, IOException {
		MyStore ms = new MyStore(driver);
		ms.ScrollDownToPopularSection();
		log.info("Scrolled down to Popular Section...");
		List<WebElement> priceEls = ms.getPriceElements();
		int minimumPriceIndex = ms.getMinimumPriceIndex(priceEls);
		log.info("Found minimum amount product in a page...");
		ms.AddCart(minimumPriceIndex);
		log.info("Added minimum amount product to the cart...");
		ms.ProductAddedSuccesfullyToCartMSG();
		log.info("Validated Cart added succesufully message in a popup...");
		ms.ProductAddedToCart();
		ms.TakeScreenshot();
		log.info("Added product is displayed in the cart and taken screenshot...");

	}

	@Test(priority = 2)
	public void ValidateDiscountEqualsToOriginal() throws IOException, InterruptedException {
		MyStore ms = new MyStore(driver);
		ms.ScrollDownToPopularSection();
		log.info("Scrolled down to Popular Section...");
		List<WebElement> sec = ms.getPriceSection();
		ms.validatePriceSection(sec);
		log.info("Validated Discount amount with original amount successfully...");

	}

	@AfterMethod

	public void closeBrowser() {
		driver.quit();
	}

}
