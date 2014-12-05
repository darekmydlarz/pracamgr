package pl.edu.agh.twitter.entities.cliquesteamuser.entity;

import pl.edu.agh.twitter.entities.cliquesteam.entity.CliquesTeam;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class CliquesTeamUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long cliqueId;
    long userId;
    long positives;
    long negatives;

    @OneToOne
    @JoinColumn(name = "cliqueId", insertable = false, updatable = false)
    CliquesTeam cliquesTeam;

    public CliquesTeamUser(long cliqueId, long userId) {
        this.cliqueId = cliqueId;
        this.userId = userId;
    }

    public CliquesTeamUser() {
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

    public CliquesTeam getCliquesTeam() {
        return cliquesTeam;
    }

    public void setCliquesTeam(CliquesTeam cliquesTeam) {
        this.cliquesTeam = cliquesTeam;
    }

    @Override
    public String toString() {
        return "CliquesTeamUser{" +
                "id=" + id +
                ", cliqueId=" + cliqueId +
                ", userId=" + userId +
                ", positives=" + positives +
                ", negatives=" + negatives +
                '}';
    }
}
