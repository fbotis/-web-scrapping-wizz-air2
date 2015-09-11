package wz.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteExtractor implements AutoCloseable {
	private Logger LOG = LoggerFactory.getLogger(RouteExtractor.class);

	private ChromeDriver driver;
	private Integer fromIndex;
	private LinkedList<String> visitedToDestinations = new LinkedList<>();
	private String fromCrtSelection;
	private String toCrtSelection;

	private StringBuffer buffer;

	private boolean hasNext = true;

	public RouteExtractor(Integer fromIndex) {
		System.setProperty("webdriver.chrome.driver", "/Users/florinbotis/Downloads/chromedriver");
		ChromeOptions options = new ChromeOptions();
		HashMap<String, String> prefs = new HashMap<>();
		prefs.put("profile.default_content_settings.geolocation", "2");
		options.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(options);
		this.fromIndex = fromIndex;
	}

	public void searchNext() {
		driver.manage().deleteAllCookies();
		driver.get("https://wizzair.com/en-GB/FlightSearch");

		WebElement from = driver.findElement(By.id(
				"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation"));
		WebElement to = driver.findElement(By.id(
				"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation"));
		WebElement submit = driver.findElement(
				By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_ButtonSubmit"));

		selectFrom(from);

		selectNextTo(to);

		LOG.info("Searching from={} to={}", fromCrtSelection, toCrtSelection);

		submit.click();
		waitUntilLoaded(driver);
	}

	public String extractAndGoNext() {

		while (true) {
			try {
				buffer = new StringBuffer();
				for (WebElement el : driver.findElements(By.className("flight-row"))) {
					if (!el.getText().contains("Dept. & Arrival")) {
						buffer.append(el.getText());
					}
				}
				break;
			} catch (StaleElementReferenceException ex) {
				LOG.trace("stale", ex);
			}
		}

		if (buffer.toString().contains("Oct")) {
			searchNext();
			LOG.info("October reached, next search");
			return extractAndGoNext();
		} else {
			LOG.info("Next days...");
			for (WebElement el : driver.findElements(By.cssSelector("#marketColumn0 > div.group > div.show-next"))) {
				try {
					el.click();
					waitUntilNextDatesLoaded(driver);
				} catch (Exception ex) {
					LOG.trace("Not clickable?", ex);
				}

			}

		}

		return buffer.toString();
	}

	private void waitUntilNextDatesLoaded(ChromeDriver driver) {
		LOG.info("Waiting until loaded...");
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				try {
					StringBuffer newbuffer = new StringBuffer();
					for (WebElement el : driver.findElements(By.className("flight-row"))) {
						newbuffer.append(el.getText());
					}
					return !(buffer.toString().equals(newbuffer));
				} catch (Exception ex) {
					LOG.trace("BUFF", ex);
					return false;
				}
			}
		};
		wait.until(condition);

	}

	//
	private void selectNextTo(WebElement to) {
		LinkedList<CharSequence> downPresses = new LinkedList<>();

		downPresses.add(Keys.ENTER);

		LinkedList<String> selections = new LinkedList<>();
		do {
			to.click();
			to.sendKeys(downPresses.toArray(new CharSequence[] {}));
			toCrtSelection = to.getAttribute("value");
			if (selections.contains(toCrtSelection)) {
				hasNext = false;
				break;
			} else {
				selections.add(toCrtSelection);
			}
			downPresses.addFirst(Keys.DOWN);
		} while (visitedToDestinations.contains(toCrtSelection));
		visitedToDestinations.add(toCrtSelection);
		LOG.info("To selected to={}", toCrtSelection);
	}

	private void selectFrom(WebElement from) {
		ArrayList<CharSequence> downPresses = new ArrayList<>();
		for (int i = 0; i < fromIndex; i++) {
			downPresses.add(Keys.DOWN);
		}
		downPresses.add(Keys.ENTER);

		from.click();
		from.sendKeys(downPresses.toArray(new CharSequence[] {}));
		fromCrtSelection = from.getAttribute("value");
		LOG.info("From selected from={}", fromCrtSelection);
	}

	private void waitUntilLoaded(ChromeDriver driver) {
		LOG.info("Waiting until loaded...");
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				return ((ChromeDriver) input).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		wait.until(condition);
	}

	public boolean hasNext() {
		return hasNext;
	}

	public static void main(String[] args) {
		try (RouteExtractor r = new RouteExtractor(41)) {
			r.searchNext();
			while (r.hasNext()) {
				System.out.println(r.extractAndGoNext().trim());
			}
		}
	}

	@Override
	public void close() {
		driver.quit();
	}

}
