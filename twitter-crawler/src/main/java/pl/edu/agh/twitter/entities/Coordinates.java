package pl.edu.agh.twitter.entities;

import twitter4j.GeoLocation;

import javax.persistence.Embeddable;

/**
 * Class for cooridnates: latitude and longitude
 */
@Embeddable
public class Coordinates {
	private Double latitude;
	private Double longitude;

	public Coordinates() {
	}

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(GeoLocation geoLocation) {
		if (geoLocation != null) {
			longitude = geoLocation.getLongitude();
			latitude = geoLocation.getLatitude();
		}
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
