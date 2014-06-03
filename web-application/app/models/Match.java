package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "match_events")
public class Match {
    @JsonIgnore
    @Id
    public Long id;
    public Date startDate;
    @OneToOne(cascade = CascadeType.ALL)
    public Team homeTeam;
    @OneToOne(cascade = CascadeType.ALL)
    public Team awayTeam;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    public Competition competition;

    public Long tweetsNumber;

    public Long geotagged;

    public String info;

    public String goalResult;

    public String infoResult;

    @OneToMany(mappedBy = "match")
    public Set<CliquesMatch> cliques;

    public static List<Match> all() {
        return JPA.em().createQuery("FROM Match ORDER BY startDate DESC", Match.class).getResultList();
    }

    public static void save(Match match) {
        JPA.em().merge(match);
    }

    public static Match findById(Long id) {
        return JPA.em().find(Match.class, id);
    }

    public static List<Match> find(Team team) {
        final String query = "FROM Match " +
                " WHERE homeTeam = :team OR awayTeam = :team " +
                " ORDER BY startDate ";
        return JPA.em().createQuery(query, Match.class)
                .setParameter("team", team)
                .getResultList();
    }

    @Override
    public String toString() {
//        return homeTeam + " vs. " + awayTeam  + " (" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate) + ")";
        return homeTeam + " vs. " + awayTeam  + " (" + goalResult + " " + infoResult + ")";
    }
}
