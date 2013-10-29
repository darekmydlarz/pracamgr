package pl.edu.agh.twitter.model;

import javax.persistence.Embeddable;

import twitter4j.GeoLocation;

@Embeddable
public class Coordinates {
	private Double longitude;
	private Double latitude;

	public Coordinates() {
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

}
