package wz.rest;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Chrome {

	static LinkedList<String> visitedFrom = new LinkedList<>();
	static LinkedList<String> visitedTo = new LinkedList<>();
	static String crtFrom = "";
	static String crtTo = "";

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "/Users/florinbotis/Downloads/chromedriver");
		ChromeDriver driver = null;
		try {
			driver = new ChromeDriver();

			searchNext(driver);

			waitUntilLoaded(driver);

			for (WebElement el : driver.findElements(By.className("flight-row"))) {
				System.out.println(el.getText());
			}

			driver.findElement(By.cssSelector("#marketColumn0 > div.group > div.show-next.right")).click();
			Thread.sleep(1000);
		} finally

		{

			// Close the browser
			if (driver != null)
				driver.quit();
		}
	}

	private static void searchNext(ChromeDriver driver) {
		driver.get("https://wizzair.com/en-GB/FlightSearch");

		WebElement from = driver.findElement(By.id(
				"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation"));
		WebElement to = driver.findElement(By.id(
				"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation"));
		WebElement submit = driver.findElement(
				By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_ButtonSubmit"));

		String fromCrtSelection = from.getAttribute("value");

		while (!crtFrom.equals(fromCrtSelection)) {
			from.click();
			from.sendKeys(Keys.DOWN, Keys.ENTER);
			fromCrtSelection = from.getAttribute("value");
		}

		String toCrtSelection = to.getAttribute("value");
		while (visitedTo.contains(toCrtSelection)) {
			to.click();
			to.sendKeys(Keys.DOWN, Keys.ENTER);
			if (toCrtSelection.equals(to.getAttribute("value"))) {
				break;
			}
			toCrtSelection = to.getAttribute("value");
		}

		crtFrom = fromCrtSelection;
		crtTo = toCrtSelection;

		submit.click();
	}

	private static void waitUntilLoaded(ChromeDriver driver) {
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				return ((ChromeDriver) input).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		wait.until(condition);
	}
}
