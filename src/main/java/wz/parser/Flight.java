package wz.parser;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by florinbotis on 05/07/2016.
 */
public class Flight {

    private Date fetchedDate;
    private Date flightDate;
    private String departure;
    private String arrival;
    private String from;
    private String to;
    private BigDecimal price;


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

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fetchedDate", fetchedDate)
                .add("flightDate", flightDate)
                .add("departure", departure)
                .add("arrival", arrival)
                .add("from", from)
                .add("to", to)
                .add("price", price)
                .toString();
    }
}
