//package wz.rest;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriverService;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//public class Application {
//	public static void main(String[] args) {
//		PhantomJSDriver driver = null;
//		try {
//			System.setProperty("phantomjs.binary.path", "/usr/local/bin/phantomjs");
//			DesiredCapabilities dcap = new DesiredCapabilities();
//			String[] phantomArgs = new String[] { "--webdriver-loglevel=NONE" };
//			dcap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
//			driver = new PhantomJSDriver(dcap);
//			// And now use this to visit Google
//			driver.get("https://wizzair.com/en-GB/FlightSearch");
//			// Alternatively the same thing can be done like this
//			// driver.navigate().to("http://www.google.com");
//
//			// Find the text input element by its name
//			Select dropdown = new Select(driver.findElement(By.id(
//					"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")));
//			dropdown.selectByIndex(0);
//
//			Select dropdown2 = new Select(driver.findElement(By.id(
//					"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation")));
//			dropdown2.selectByIndex(0);
//
//			driver.findElement(
//					By.id("ControlGroupRibbonAnonNewHomeView$AvailabilitySearchInputRibbonAnonNewHomeView$DepartureDate"))
//					.click(); // click field
//			driver.findElement(By.linkText("22")).click(); // click day
//
//			driver.findElement(
//					By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_ButtonSubmit"))
//					.submit();
//
//			// Check the title of the page
//			System.out.println("Page title is: " + driver.getTitle());
//
//			// Google's search is rendered dynamically with JavaScript.
//			// Wait for the page to load, timeout after 10 seconds
//			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//				public Boolean apply(WebDriver d) {
//					return d.getTitle().toLowerCase().startsWith("cheese!");
//				}
//			});
//
//			// Should see: "cheese! - Google Search"
//			System.out.println("Page title is: " + driver.getTitle());
//		} finally {
//
//			// Close the browser
//			driver.quit();
//		}
//	}
//}
