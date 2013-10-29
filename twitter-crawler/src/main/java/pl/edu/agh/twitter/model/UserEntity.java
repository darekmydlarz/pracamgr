package pl.edu.agh.twitter.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import twitter4j.User;

@Entity
@Table(schema = "mgr", name = "users")
public class UserEntity {
	@Id
	private Long id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String url;
    private int followersCount;

    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private String lang;
	
	public UserEntity(User user) {
		
		id = user.getId();
		name = user.getName();
		screenName = user.getScreenName();
		location = user.getLocation();
		description = user.getDescription();
		url = user.getURL();
		followersCount = user.getFollowersCount();
		createdAt = user.getCreatedAt();
		favouritesCount = user.getFavouritesCount();
		utcOffset = user.getUtcOffset();
		timeZone = user.getTimeZone();
		lang = user.getLang();
	}

	public UserEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public int getUtcOffset() {
		return utcOffset;
	}

	public void setUtcOffset(int utcOffset) {
		this.utcOffset = utcOffset;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
