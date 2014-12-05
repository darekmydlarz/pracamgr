package pl.edu.agh.twitter.entities.userteamstats;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class UserTeamStats {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private long userId;

    private long teamId;

    private long matches;

    private long positives;

    private long negatives;

    public UserTeamStats() {
    }

    public UserTeamStats(long userId, long teamId, long matches) {
        this.userId = userId;
        this.teamId = teamId;
        this.matches = matches;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getMatches() {
        return matches;
    }

    public void setMatches(long matches) {
        this.matches = matches;
    }

    public long getPositives() {
        return positives;
    }

    public void setPositives(long positives) {
        this.positives = positives;
    }

    public long getNegatives() {
        return negatives;
    }

    public void setNegatives(long negatives) {
        this.negatives = negatives;
    }

    @Override
    public String toString() {
        return "UserTeamStats{" +
                "id=" + id +
                ", userId=" + userId +
                ", teamId=" + teamId +
                ", matches=" + matches +
                ", positives=" + positives +
                ", negatives=" + negatives +
                '}';
    }
}
