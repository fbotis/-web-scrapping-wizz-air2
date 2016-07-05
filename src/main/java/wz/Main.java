package wz;

import com.google.gson.JsonObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by florinbotis on 01/07/2016.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        String chromedriverlocation = args[0];
        String whereToSave = args[1];


        //chromedriverlocation="/Users/florinbotis/Downloads/chromedriver";
        long start = System.currentTimeMillis();
        System.setProperty("webdriver.chrome.driver", chromedriverlocation);

        ChromeOptions options = new ChromeOptions();
        JsonObject prefs = new JsonObject();
        prefs.addProperty("profile.default_content_settings.geolocation", 2);
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = null;


        // Configure our WebDriver to support JavaScript and be able to find the PhantomJS binary

        ExecutorService pool = Executors.newFixedThreadPool(1);
        List<Future<Void>> results = new ArrayList<>();

        log.info("Hour is {}. Starting ... ", LocalTime.now());


        try {
            driver = new ChromeDriver(options);

            HashMap<Integer, Integer> combinations = null;
            if ((combinations = readFromFile()) == null) {
                combinations = getCOmbinations(driver);
                writeCombinationsToFile(combinations);
            }

            driver.quit();
            for (Map.Entry<Integer, Integer> entry : combinations.entrySet()) {
                for (int to = 1; to <= entry.getValue(); to++) {

                    try {
                        PricesFetcher fetcher = new PricesFetcher(null, entry.getKey(), to, whereToSave);
                        Future<Void> future = pool.submit(fetcher);
                        future.get();
                    } catch (Exception ex) {
                        log.error("Failed for from{} to{} doing it again...", entry.getValue(), to, ex);
                        PricesFetcher fetcher = new PricesFetcher(null, entry.getKey(), to, whereToSave);
                        Future<Void> future = pool.submit(fetcher);
                        future.get();
                    }
                }
            }

        } catch (Exception ex) {
            log.error("Big error", ex);
        } finally {
            log.info("END, total time: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        }
    }

    private static HashMap<Integer, Integer> readFromFile() {
        HashMap<Integer, Integer> combinations = null;
        try {
            FileInputStream fin = new FileInputStream("combinations.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            combinations = (HashMap<Integer, Integer>) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return combinations;
    }

    private static void writeCombinationsToFile(HashMap<Integer, Integer> combinations) {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream("combinations.ser");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(combinations);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
                oos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    private static HashMap<Integer, Integer> getCOmbinations(WebDriver driver) throws InterruptedException {
        HashMap<Integer, Integer> combinations = new HashMap<>();
        driver.get("https://wizzair.com/en-GB/Select");
        WebDriverWait waitdriver = new WebDriverWait(driver, 15);
        driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).click();

        int total = driver.findElement(By.className("box-autocomplete")).getText().split("\n").length;
        for (WebElement element : driver.findElements(By.className("box-autocomplete"))) {
            if (element.getAttribute("style").contains("block")) {
                System.out.println("*****");
                total = element.getText().split("\n").length;
            } else {
                System.out.println("NOELEMENT");
            }

        }


        System.out.println(total);
        driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).click();
        waitdriver.until(ExpectedConditions.visibilityOfElementLocated(By.className("inHeader")));


        for (int x = 0; x <= total; x++) {
            Keys[] keys = new Keys[x + 1];
            for (int l = 0; l < keys.length; l++) {
                keys[l] = Keys.ARROW_DOWN;
            }
            keys[keys.length - 1] = Keys.ENTER;

            Keys[] upArrows = new Keys[200];
            for (int i = 0; i < 50; i++) {
                upArrows[i] = Keys.BACK_SPACE;
            }

            driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).click();
            driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).sendKeys(Keys.BACK_SPACE);
            driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).sendKeys(keys);

            driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation")).click();
            driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteDestinationStation")).sendKeys(Keys.ARROW_DOWN);


            for (WebElement element : driver.findElements(By.className("box-autocomplete"))) {
                if (element.getAttribute("style").contains("block")) {
                    combinations.put(x, element.getText().split("\n").length);
                } else {
                }

            }
            log.info("Combinations for from={} fromDest={} no={} ", x,
                    driver.findElement(By.id("ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_AutocompleteOriginStation")).getAttribute("value"),
                    combinations.get(x));

        }
        combinations.remove(0);
        return combinations;
    }
}
