package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = "mgr", name = "users")
public class User {
    @Id
    public Long id;
    public String name;
    public String screenName;
    @JsonIgnore
    public String location;
    @JsonIgnore
    public String description;
    @JsonIgnore
    public String url;
    @JsonIgnore
    public int followersCount;

    @JsonIgnore
    public Date createdAt;
    @JsonIgnore
    public int favouritesCount;
    @JsonIgnore
    public int utcOffset;
    @JsonIgnore
    public String timeZone;
    @JsonIgnore
    public String lang;
}
