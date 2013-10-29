package pl.edu.agh.twitter.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import twitter4j.Place;

@Entity
@Table(schema = "mgr", name = "place")
public class PlaceEntity {
	@Id
	private String id;
	private String name;
	private String streetAddress;
	private String countryCode;
	private String country;
	private String placeType;
	private String url;
	private String fullName;
	private String boundingBoxType;

	@AttributeOverrides({ @AttributeOverride(name = "longitude", column = @Column(name = "left_longitude")),
			@AttributeOverride(name = "latitude", column = @Column(name = "bottom_latitude")) })
	private Coordinates leftBottomCoordinates;

	@AttributeOverrides({ @AttributeOverride(name = "longitude", column = @Column(name = "right_longitude")),
			@AttributeOverride(name = "latitude", column = @Column(name = "top_latitude")) })
	private Coordinates rightTopCoordinates;

	public PlaceEntity() {
	}

	public PlaceEntity(Place place) {
		id = place.getId();
		name = place.getName();
		streetAddress = place.getStreetAddress();
		countryCode = place.getCountryCode();
		country = place.getCountry();
		placeType = place.getPlaceType();
		url = place.getURL();
		fullName = place.getFullName();
		boundingBoxType = place.getBoundingBoxType();
		leftBottomCoordinates = new Coordinates(place.getBoundingBoxCoordinates()[0][0]);
		rightTopCoordinates = new Coordinates(place.getBoundingBoxCoordinates()[0][2]);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBoundingBoxType() {
		return boundingBoxType;
	}

	public void setBoundingBoxType(String boundingBoxType) {
		this.boundingBoxType = boundingBoxType;
	}

	public Coordinates getLeftBottomCoordinates() {
		return leftBottomCoordinates;
	}

	public void setLeftBottomCoordinates(Coordinates leftBottomCoordinates) {
		this.leftBottomCoordinates = leftBottomCoordinates;
	}

	public Coordinates getRightTopCoordinates() {
		return rightTopCoordinates;
	}

	public void setRightTopCoordinates(Coordinates rightTopCoordinates) {
		this.rightTopCoordinates = rightTopCoordinates;
	}

}
