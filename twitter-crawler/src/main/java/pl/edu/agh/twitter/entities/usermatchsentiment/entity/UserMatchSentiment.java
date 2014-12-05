package pl.edu.agh.twitter.entities.usermatchsentiment.entity;


import javax.persistence.*;

@Entity
@Table(schema = "mgr", name = "users_match_sentiment")
public class UserMatchSentiment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userId;
    private Long matchEventId;
    private Long positives;
    private Long negatives;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMatchEventId() {
        return matchEventId;
    }

    public void setMatchEventId(Long matchEventId) {
        this.matchEventId = matchEventId;
    }

    public Long getPositives() {
        return positives;
    }

    public void setPositives(Long positives) {
        this.positives = positives;
    }

    public Long getNegatives() {
        return negatives;
    }

    public void setNegatives(Long negatives) {
        this.negatives = negatives;
    }

    @Override
    public String toString() {
        return "UserMatchSentiment{" +
                "id=" + id +
                ", userId=" + userId +
                ", matchEventId=" + matchEventId +
                ", positives=" + positives +
                ", negatives=" + negatives +
                '}';
    }
}
