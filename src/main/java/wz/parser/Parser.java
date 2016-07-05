package wz.parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florinbotis on 05/07/2016.
 */
public class Parser {

    static SimpleDateFormat folderSimpleDateFormat = new SimpleDateFormat("ddMMyyyy");

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("/Users/florinbotis/Downloads");
        List<Path> dataFolders = getDataFolders(dir);

        for (Path path : dataFolders) {
            processDataFolder(path);
        }

        String data = "outbound\n" +
                "Alicante → Cluj-Napoca\n" +
                "Dept. & Arrival\n" +
                "BASIC\n" +
                "normal price WIZZ Discount Club\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "PLUS\n" +
                "normal price WIZZ Discount Club\n" +
                "Sun, 07 Aug\n" +
                "08:35 → 12:55 €159.99 €139.99 €213.49 €193.49\n" +
                "Mon, 08 Aug No flight found for this date. No flight found for this date.\n" +
                "Wed, 10 Aug\n" +
                "08:35 → 12:55 €189.99 €169.99 €243.49 €223.49\n";


        List<Flight> flighs = getFlights(new Date(), data);


    }

    private static void processDataFolder(Path path) {

    }

    private static List<Path> getDataFolders(Path dir) {
        List<Path> dataFolders = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (file.getFileName().toString().length() != 8) {
                    continue;

                }
                try {
                    Date date = folderSimpleDateFormat.parse(file.getFileName().toString());
                    dataFolders.add(file);
                } catch (ParseException e) {
                }

            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        return dataFolders;
    }

    private static List<Flight> getFlights(Date fetchedDate, String data) {
        List<Flight> flights = new ArrayList<>();

        //FROM -> TO
        String[] lines = data.split("\n");
        String[] fromTo = lines[1].split("\\→");
        String from = fromTo[0].trim();
        String to = fromTo[1].trim();


        Flight f = null;

        Date flightDate = null;
        boolean dateFound = false;
        for (String line : lines) {
            if (dateFound) {
                String[] timeAndPrices = line.split(" ");
                String departure = timeAndPrices[0];
                String arrival = timeAndPrices[2];
                String price = timeAndPrices[3];
                f.setDeparture(departure);
                f.setArrival(arrival);
                f.setPrice(getPriceInRON(price));
                System.out.println(f);
                flights.add(f);
                f = null;
                dateFound = false;
            }


            if (line.contains("No flight")) {
                dateFound = false;
            } else if (startWithWeekDay(line)) {
                try {
                    flightDate = getFlightDate(line);
                    f = new Flight();
                    f.setFetchedDate(fetchedDate);
                    f.setFrom(from);
                    f.setTo(to);
                    f.setFlightDate(flightDate);
                    dateFound = true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return flights;

    }

    private static BigDecimal getPriceInRON(String price) {
        //TODO add conversion rate
        return BigDecimal.valueOf(Double.valueOf(price.replaceAll("[^\\d.]", "")));
    }

    static SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");

    private static Date getFlightDate(String line) throws ParseException {
        String[] dayDate = line.split(",");
        String date = dayDate[1].trim();
        date = date + " " + LocalDate.now().getYear();
        System.out.println(date);
        System.out.println(df.parse(date));
        return df.parse(date);
    }

    private static boolean startWithWeekDay(String line) {
        return line.startsWith("Mon") || line.startsWith("Tue") || line.startsWith("Wed") || line.startsWith("Thu")
                || line.startsWith("Fri") || line.startsWith("Sat") || line.startsWith("Sun");
    }
}
