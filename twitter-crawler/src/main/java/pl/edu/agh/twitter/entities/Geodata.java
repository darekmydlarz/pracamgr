package pl.edu.agh.twitter.entities;

import pl.edu.agh.twitter.GeoReversing;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Mapped DB table
 */
@Entity
@Table(schema = "mgr", name = "geodata")
public class Geodata {
    @Id
    private Long tweetId;

    @Embedded
    private Coordinates coordinates;

    private String city;
    private String county;
    private String state;
    private String country;
    private String countryCode;

    public Geodata() {
    }

    public Geodata(Tweet tweet, GeoReversing.Address address) {
        this.tweetId = tweet.getId();
        this.coordinates = tweet.getCoordinates();
        city = address.city;
        county = address.county;
        state = address.state;
        country = address.country;
        countryCode = address.country_code;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "Geodata{" +
                "tweetId=" + tweetId +
                ", coordinates=" + coordinates +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
