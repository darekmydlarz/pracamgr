package pl.edu.agh.twitter.business.classifiedtweet.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "mgr", name = "classified_tweets")
public class ClassifiedTweet {
    @Id
    private long id;

    @Column(length = 140)
    private String text;

    private String sentiment;

    private String keyword;

    public ClassifiedTweet() {
    }

    public ClassifiedTweet(long id, String text, String sentiment, String keyword) {
        this.id = id;
        this.text = text;
        this.sentiment = sentiment;
        this.keyword = keyword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "ClassifiedTweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", sentiment='" + sentiment + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}