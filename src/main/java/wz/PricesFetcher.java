package wz;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class PricesFetcher implements Callable<Void> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String today;
    private final String folderLocation;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
    private SimpleDateFormat fileSimpleDateFormat = new SimpleDateFormat("ddMMyyyy-hh-mm");
    //how many times we press down arrow for From field
    private final int from;
    //how many times we press down arrow for To field
    private final int to;

    private WebDriver driver;
    private WebDriverWait waitdriver;
    private String toDest;
    private String fromDest;
    private int fileIndex;


    public PricesFetcher(WebDriver driver, int from, int to, String folderLocation) {
        this.from = from;
        this.to = to;
        this.today = simpleDateFormat.format(new Date());
        this.folderLocation = folderLocation;
    }


    @Override
    public Void call() {
        try {
            ChromeOptions options = new ChromeOptions();
            JsonObject prefs = new JsonObject();
            prefs.addProperty("profile.default_content_settings.geolocation", 2);
            options.setExperimentalOption("prefs", prefs);
            driver = new ChromeDriver(options);
            waitdriver = new WebDriverWait(driver, 30);

            log.info("Start from={} to={} ", from, to);
            driver.get("https://wizzair.com//en-GB/FlightSearch");


            clickElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation"));
            waitVisible(By.className("inHeader"));


            selectFrom(from);
            selectTo(to);


            waitVisible(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation"));
            waitVisible(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation"));
            fromDest = driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).getAttribute("value");
            toDest = driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation")).getAttribute("value");
            log.info("Searching for fromDest={} toDest={}", fromDest, toDest);

            //TODO only CLJ
            if (!fromDest.contains("CLJ") && !toDest.contains("CLJ")) {
                return null;
            }

            log.info("cjcombinations.put({},{})", from, to);


            clickElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_ButtonSubmit"));

            waitVisible(By.className("flights-header"));

            fileIndex = 0;
            saveFile(driver);
            while (true) {

                if (!clickNext(driver)) {
                    log.error("Not clickable ");
                    return null;
                }

                waitVisible(By.className("flights-body"));

                if (driver.findElement(By.className("flights-body")).getText().contains("No flight found for your search.")) {
                    log.info("No results, finishing");
                    return null;
                }

                saveFile(driver);


            }
        } catch (Exception ex) {
            log.error("Error while processing from={} to={} today={} fromDest={} toDest={} fileIndex={}", from, to, today, fromDest, toDest, fileIndex, ex);
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ex) {
                    log.error("Error closing driver", ex);
                }
            }
        }
        return null;
    }

    private void waitVisible(By by) {
        waitdriver.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private void clickElement(By by) {
        int iterations = 0;
        while (iterations < 3) {
            try {
                waitdriver.until(ExpectedConditions.elementToBeClickable(by));
                driver.findElement(by).click();
                return;
            } catch (StaleElementReferenceException stale) {
                log.warn("Stale ...");
                iterations++;
            }
        }

    }

    private void selectTo(int to) {

        Keys[] keys = new Keys[to + 2];

        for (int i = 0; i < to; i++) {
            keys[i] = Keys.ARROW_DOWN;
        }
        keys[to] = Keys.ENTER;
        keys[to + 1] = Keys.TAB;

        clickElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation"));
        sendKeys(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation"), Keys.BACK_SPACE);
        sendKeys(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation"), keys);

    }

    private void sendKeys(By by, Keys... keys) {
        int iterations = 0;
        while (iterations < 3) {
            try {
                driver.findElement(by).sendKeys(keys);
                return;
            } catch (StaleElementReferenceException stale) {
                log.warn("Stale ...");
                iterations++;
            }
        }

    }

    private void selectFrom(int from) {
        Keys[] keys = new Keys[from + 2];

        for (int i = 0; i < from; i++) {
            keys[i] = Keys.ARROW_DOWN;
        }
        keys[from] = Keys.ENTER;
        keys[from + 1] = Keys.TAB;

        clickElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation"));
        driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).sendKeys(keys);
    }

    private boolean clickNext(WebDriver driver) {
        while (true) {
            boolean clicked = false;
            int iteration = 0;
            for (WebElement el : driver.findElements(By.className("show-next"))) {
                try {
                    el.click();
                    clicked = true;
                    return true;
                } catch (Exception ex) {

                }
            }
            if (!clicked) {
                iteration++;
                continue;
            }
            if (!clicked && iteration > 3) {
                log.error("Element not clickable");
                return false;
            }


        }
    }

    private void saveFile(WebDriver driver) {
        int iteration = 0;
        while (iteration < 3) {
            try {
                for (WebElement el : driver.findElements(By.className("flights-container"))) {
                    if (el.getText().contains("outbound")) {
                        log.info("Saving file date={} fromDest={} toDest={} fileIndex={}", today, fromDest, toDest, fileIndex);
                        try {
                            Path folder = Paths.get(folderLocation + "/" + today);
                            if (!Files.exists(folder)) {
                                Files.createDirectory(folder);
                            }
                            Files.write(Paths.get(folderLocation + "/" + today + "/" + fileSimpleDateFormat.format(new Date()) + "_" + fromDest.replaceAll("/", "-") + "_" + toDest.replaceAll("/", "-") + "_" + fileIndex + ".txt"), Lists.newArrayList(el.getText()));
                            fileIndex++;
                            return;
                        } catch (IOException e) {
                            log.error("Error saving file", e);
                        }

                    }
                }
            } catch (StaleElementReferenceException ex) {
                log.warn("Stale...");
                iteration++;
            }
        }
    }

}
