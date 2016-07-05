package wz.parser;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by florinbotis on 05/07/2016.
 */
public class Flight {

    private Date fetchedDate;
    private Date flightDate;
    private Date departure;
    private Date arrival;
    private String from;
    private String to;
    private BigDecimal price;

    public Flight(Date fetchedDate, Date flightDate, Date departure, Date arrival, String from, String to, BigDecimal price) {
        this.fetchedDate = fetchedDate;
        this.flightDate = flightDate;
        this.departure = departure;
        this.arrival = arrival;
        this.from = from;
        this.to = to;
        this.price = price;
    }

    public Flight() {

    }

    public Date getFetchedDate() {
        return fetchedDate;
    }

    public void setFetchedDate(Date fetchedDate) {
        this.fetchedDate = fetchedDate;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
