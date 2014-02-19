package models;

import javax.persistence.Embeddable;

@Embeddable
public class Coordinates {
    public Double longitude;
    public Double latitude;
}
