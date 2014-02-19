package models;

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
    public String location;
    public String description;
    public String url;
    public int followersCount;

    public Date createdAt;
    public int favouritesCount;
    public int utcOffset;
    public String timeZone;
    public String lang;
}
