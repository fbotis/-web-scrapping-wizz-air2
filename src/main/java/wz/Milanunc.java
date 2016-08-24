package wz;

import com.google.gson.JsonObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by florinbotis on 24/08/2016.
 */
public class Milanunc {
    private static final Logger log = LoggerFactory.getLogger(Milanunc.class);

    public static void main(String[] args) throws InterruptedException {
        String chromedriverlocation = "/Users/florinbotis/Downloads/chromedriver";
        long start = System.currentTimeMillis();
        System.setProperty("webdriver.chrome.driver", chromedriverlocation);


        ChromeOptions options = new ChromeOptions();
        JsonObject prefs = new JsonObject();
        prefs.addProperty("profile.default_content_settings.geolocation", 2);
        options.setExperimentalOption("prefs", prefs);
        ChromeDriver driver = null;


        // Configure our WebDriver to support JavaScript and be able to find the PhantomJS binary

        String[] provinces = new String[]{"Badajoz", "Cáceres", "Ciudad Real", "Zaragoza", "Cuenca", "Huesca", "León", "Toledo", "Albacete", "Teruel", "Burgos", "Sevilla", "Córdoba", "Jaén", "Granada", "Salamanca", "Guadalajara", "Lleida", "Murcia", "Valencia", "Asturias", "Zamora", "Navarra", "Soria", "Huelva", "Lugo", "Almería", "Valladolid", "Palencia", "Ávila", "Madrid", "A Coruña", "Barcelona", "Cádiz", "Málaga", "Ourense", "Segovia", "Castellón", "Tarragona", "Girona", "Alicante", "Cantabria", "La Rioja", "Balearic Islands", "Pontevedra", "Las Palmas", "Álava", "Biscay", "Gipuzkoa", "Melilla", "Ceuta"};




        for (String prov : provinces) {
            try {
                driver = new ChromeDriver(options);
                driver.get("https://www.milanuncios.com/textos-del-anuncio/?c=290&m=1");
                driver.findElement(By.id("titulo")).

                        sendKeys("TRANSPORT MASINI ROMANIA 642434438");

                driver.findElement(By.id("mapPlaceBox")).clear();
                driver.findElement(By.id("mapPlaceBox")).sendKeys(prov);

                driver.findElement(By.id("texto")).clear();
                driver.findElement(By.id("texto")).sendKeys("EVITATI ACEST OM, E TZEAPA!!! danytransspania,Transport Logistic Express, Viziteu Dumitru Daniel, Detalii pe danytransspania.wordpress.com \n" +
                        "Transport international din/spre romania, austria, germania, franta, italia, spania, Oferim transport cu platforma specializata pentru: transport autoturisme, masini noi, transport coches, transport rulote, transport autorulote, transport microbuze, transport remorci transport atv-uri, transport barci, iahturi transport transport persoane, masini, transport colete, marfa, mobila Plecari zilnic  ");

                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("email")).sendKeys("teapa@yopmail.com");

                driver.findElement(By.id("repemail")).clear();
                driver.findElement(By.id("repemail")).sendKeys("teapa@yopmail.com");

                driver.findElement(By.id("telefono1")).clear();
                driver.findElement(By.id("telefono1")).sendKeys("642434438");

                Thread.sleep(1000);
                driver.findElement(By.className("btnSiguiente")).click();

                Thread.sleep(15000);
            }finally {
                driver.quit();
            }
        }
    }
}
