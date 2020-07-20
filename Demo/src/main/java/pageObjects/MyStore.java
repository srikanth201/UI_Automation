package pageObjects;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.UIAutomation.Demo.Synchronization;

public class MyStore {

	public WebDriver driver;

	// Declaration
	By PS = By.xpath("//a[.='Popular']");
	By popularSectionElements = By.xpath("//ul[@id='homefeatured']//div[@class='product-container']");// 7
	By popularSectionPrice = By.xpath(
			"//ul[@id='homefeatured']//div[@class='product-container']//div[@class='right-block']//span[@itemprop='price']");// 7
	By AddToCart = By.xpath(
			"//ul[@id='homefeatured']//div[@class='product-container']//div[@class='right-block']//div[@class='button-container']//a//span[.='Add to cart']");// 7
	By ProdAddedToCartMSG = By.xpath(
			"//div[contains(@class,'layer_cart_product col-xs')]//h2[contains(.,'Product successfully added to your shopping cart')]");// 7
	By PriceSection = By.xpath(
			"//ul[@id='homefeatured']//div[@class='product-container']//div[@class='right-block']//div[@itemprop='offers']");// 7

	By Image = By.xpath("//ul[@id='homefeatured']//li[contains(@class,'ajax_block_product col-xs')]");
	By ProceedToCheckOut = By
			.xpath("//a[@class='btn btn-default button button-medium' and @title='Proceed to checkout']//span");
	By CartProductTitle = By.xpath("//span[@id='layer_cart_product_title']");
	By ProdAddedinShopCartPage = By
			.xpath("//table[@id='cart_summary']//tr[contains(@class,'cart_item last_item first_item addre')]//p//a");

	// Initialization
	public MyStore(WebDriver driver) {
		this.driver = driver;
	}

	// Utilization
	// Scrolling to specific position
	public void ScrollDownToPopularSection() {
		ScrollToSection(PS, 0);
	}

	// Generic Scroll method
	public void ScrollToSection(By val, int index) {
		List<WebElement> Section = driver.findElements(val);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Point p = Section.get(index).getLocation();
		int x = p.getX();
		int y = p.getY();
		js.executeScript("window.scrollBy(" + x + "," + y + ")");
	}

	public List<WebElement> getPriceElements() {

		return driver.findElements(popularSectionPrice); // 7

	}

	// Adding minimum amount product to the cart
	public void AddCart(int index) throws InterruptedException {
		List<WebElement> ele1 = driver.findElements(Image);

		Actions ac = new Actions(driver);
		ScrollToSection(Image, index);
		ac.moveToElement(ele1.get(index)).build().perform();

		Thread.sleep(4000);
		List<WebElement> AddCartButton = driver.findElements(AddToCart);
		AddCartButton.get(index).click();
		Thread.sleep(4000);

	}

	// Validating Cart succesfull message
	public void ProductAddedSuccesfullyToCartMSG() {

		WebElement SuccessMsg = driver.findElement(ProdAddedToCartMSG);
		String ActualMessage = SuccessMsg.getText();
		String ExpectedMessage = "Product successfully added to your shopping cart";
		Assert.assertEquals(ActualMessage, ExpectedMessage);

	}

	public void ProductAddedToCart() throws InterruptedException {
		WebElement prodTitle = driver.findElement(CartProductTitle);
		String ProductAddedToCart = prodTitle.getText();
		// System.out.println(ProductAddedToCart);
		driver.findElement(ProceedToCheckOut).click();
		Thread.sleep(4000);

		WebElement prodTitleinCartSummary = driver.findElement(ProdAddedinShopCartPage);
		String CartSummaryProdTitle = prodTitleinCartSummary.getText();

		Assert.assertEquals(CartSummaryProdTitle, ProductAddedToCart);
	}

	// Getting a product index with minimum price
	public int getMinimumPriceIndex(List<WebElement> eles) {
		int index = 0;
		double minPrice = 0;
		for (int i = 0; i < eles.size(); i++) {

			String text = eles.get(i).getText();
			text = text.replace("$", "");// 32.00
			double currPrice = (Double.parseDouble(text));
			if (currPrice < minPrice || i == 0) {
				minPrice = currPrice;
				index = i;

			}

		}

		return index;
	}

	public List<WebElement> getPriceSection() {
		return driver.findElements(PriceSection);
	}

	public double roundOff(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	public void clickProceedToCheckout() {
		driver.findElement(ProceedToCheckOut).click();

	}

	public void TakeScreenshot() throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File dst = new File("C:\\cart.png");
		FileUtils.copyDirectory(src, dst);

	}

	// Validating after discount price with Original price and discount
	public void validatePriceSection(List<WebElement> eles) {
		for (int i = 0; i < eles.size(); i++) {

			String offersHTML = eles.get(i).getAttribute("innerHTML");

			// parse HTML
			Document document = Jsoup.parse(offersHTML);
			// select all elements
			Elements actualPriceEle = document.select("span.old-price");
			Elements discountPriceEle = document.select("span.price");
			Elements discountEle = document.select("span.price-percent-reduction");
			try {
				// Validating Discount only for products with discount
				if (discountEle.size() > 0) {
					String actualPrice_str = actualPriceEle.get(0).text().replace("$", "");
					String discountPrice_str = discountPriceEle.get(0).text().replace("$", "");
					String discount_str = discountEle.get(0).text().replace("-", "").replace("%", "");

					double actualPrice = roundOff(Double.parseDouble(actualPrice_str));
					double discountPrice = roundOff(Double.parseDouble(discountPrice_str));
					double discount = Double.parseDouble(discount_str);

					// PriceAfterDiscount=Actual Price-Discount Amount
					double expectedDiscPrice = roundOff(actualPrice * (1 - (discount / 100)));
					Assert.assertEquals(discountPrice, expectedDiscPrice);
				}
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}

		}

	}
}
