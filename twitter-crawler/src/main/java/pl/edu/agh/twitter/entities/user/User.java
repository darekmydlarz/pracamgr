package pl.edu.agh.twitter.entities.user;

import com.google.common.collect.Maps;
import pl.edu.agh.twitter.entities.team.Team;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * Mapped DB table
 */
@Entity
@Table(schema = "mgr", name = "users")
@NamedQuery(name = User.FIND_BY_SCREEN_NAME, query = User.FIND_BY_SCREEN_NAME_QUERY)
public class User {
    public static final String FIND_BY_SCREEN_NAME = "FIND_BY_SCREEN_NAME";
    public static final String FIND_BY_SCREEN_NAME_QUERY = "FROM User WHERE screenName = :screenName";
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

    @Transient
    private Long tweetsNumber;

    @Transient
    private Map<Team, Long> teamMap = Maps.newHashMap();
    private String topTeam;

    public User(twitter4j.User user) {
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

    public User() {
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

    public Long getTweetsNumber() {
        return tweetsNumber;
    }

    public void setTweetsNumber(Long tweetsNumber) {
        this.tweetsNumber = tweetsNumber;
    }

    public void addTeam(Team team) {
        long count = teamMap.containsKey(team) ? teamMap.get(team) : 0;
        teamMap.put(team, count + 1);
    }

    public Team getMostOftenTeam() {
        Team mostOftenTeam = null;
        long highestCount = 0;
        for(Team team : teamMap.keySet()) {
            if(teamMap.get(team) > highestCount) {
                highestCount = teamMap.get(team);
                mostOftenTeam = team;
            }
        }
        return mostOftenTeam;
    }

    @Override
    public String toString() {
        return id + "\t" + screenName + "\t" + tweetsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (!id.equals(that.id)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (screenName != null ? !screenName.equals(that.screenName) : that.screenName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (screenName != null ? screenName.hashCode() : 0);
        return result;
    }

    public void setTopTeam(String topTeam) {
        this.topTeam = topTeam;
    }

    public String getTopTeam() {
        return topTeam;
    }
}
