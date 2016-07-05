package wz.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by florinbotis on 05/07/2016.
 */
public class Parser {

    public static void main(String[] args) {
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


        String[] lines = data.split("\n");
        String[] fromTo = lines[1].split("\\→");
        String from = fromTo[0].trim();
        String to = fromTo[1].trim();
        Flight f = new Flight();
        f.setFrom(from);
        f.setTo(to);

        for (String line : lines) {
            if (!line.contains("No flight") && startWithWeekDay(line)) {
                try {
                    Date date = getFlightDate(line);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


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
