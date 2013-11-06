package pl.edu.agh.twitter.model;

import twitter4j.Status;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "mgr", name = "tweets")
public class Tweet {
    @Id
    private Long id;

    @Column(length = 140)
    private String text;

    private Date createdAt;

    private String source;

    private Long inReplyToStatusId;

    private Long inReplyToUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "match_event")
    private MatchEvent matchEvent;

    @Embedded
    private Coordinates coordinates;

    private int retweetCount;

    private int favouriteCount;


    public Tweet(Status status, UserEntity user, MatchEvent matchEvent) {
        this.user = user;
        this.matchEvent = matchEvent;
        coordinates = new Coordinates(status.getGeoLocation());
        createdAt = status.getCreatedAt();
        favouriteCount = status.getFavoriteCount();
        id = status.getId();
        inReplyToStatusId = status.getInReplyToStatusId();
        inReplyToUserId = status.getInReplyToUserId();
        retweetCount = status.getRetweetCount();
        source = status.getSource();
        text = status.getText();
    }

    public Tweet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(Long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(Long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public MatchEvent getMatchEvent() {
        return matchEvent;
    }

    public void setMatchEvent(MatchEvent matchEvent) {
        this.matchEvent = matchEvent;
    }
}
