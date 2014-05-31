package pl.edu.agh.twitter.business.CliquesMatchUser;

import pl.edu.agh.twitter.business.cliquesmatch.CliquesMatch;
import pl.edu.agh.twitter.business.cliquesteam.entity.CliquesTeam;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class CliquesMatchUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long cliqueId;
    long userId;
    long positives;
    long negatives;

    @OneToOne
    @JoinColumn(name = "cliqueId", insertable = false, updatable = false)
    CliquesMatch cliquesMatch;

    public CliquesMatchUser(long cliqueId, long userId) {
        this.cliqueId = cliqueId;
        this.userId = userId;
    }

    public CliquesMatchUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCliqueId() {
        return cliqueId;
    }

    public void setCliqueId(long cliqueId) {
        this.cliqueId = cliqueId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public CliquesMatch getCliquesMatch() {
        return cliquesMatch;
    }

    public void setCliquesMatch(CliquesMatch cliquesMatch) {
        this.cliquesMatch = cliquesMatch;
    }

    @Override
    public String toString() {
        return "CliquesMatchUser{" +
                "id=" + id +
                ", cliqueId=" + cliqueId +
                ", userId=" + userId +
                ", positives=" + positives +
                ", negatives=" + negatives +
                ", cliquesMatch=" + cliquesMatch +
                '}';
    }
}
