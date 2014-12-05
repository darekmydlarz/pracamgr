package pl.edu.agh.twitter.entities.cliquesteam.entity;

import pl.edu.agh.twitter.entities.team.entity.Team;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class CliquesTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="cliquesTeamSequence", initialValue = 1)
    long id;

    long teamId;

    long cliqueSize;

    @OneToOne
    @JoinColumn(name = "teamId", insertable = false, updatable = false)
    Team team;

    public CliquesTeam(long teamId) {
        this.teamId = teamId;
    }

    public CliquesTeam() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getCliqueSize() {
        return cliqueSize;
    }

    public void setCliqueSize(long cliqueSize) {
        this.cliqueSize = cliqueSize;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "CliquesTeam{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", cliqueSize=" + cliqueSize +
                '}';
    }
}
