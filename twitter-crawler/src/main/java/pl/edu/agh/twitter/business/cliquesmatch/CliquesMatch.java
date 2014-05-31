package pl.edu.agh.twitter.business.cliquesmatch;

import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class CliquesMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long matchId;

    long cliqueSize;

    @OneToOne
    @JoinColumn(name = "matchId", insertable = false, updatable = false)
    MatchEvent match;

    public CliquesMatch(long matchId) {
        this.matchId = matchId;
    }

    public CliquesMatch() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public long getCliqueSize() {
        return cliqueSize;
    }

    public void setCliqueSize(long cliqueSize) {
        this.cliqueSize = cliqueSize;
    }

    public MatchEvent getMatch() {
        return match;
    }

    public void setMatch(MatchEvent match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return "CliquesMatch{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", cliqueSize=" + cliqueSize +
                ", match=" + match +
                '}';
    }
}
