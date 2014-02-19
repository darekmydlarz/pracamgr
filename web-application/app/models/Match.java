package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "mgr", name = "match_events")
public class Match {
    @Id
    public Long id;
    public Date startDate;
    @OneToOne(cascade = CascadeType.ALL)
    public Team homeTeam;
    @OneToOne(cascade = CascadeType.ALL)
    public Team awayTeam;
    @OneToOne(cascade = CascadeType.ALL)
    public Competition competition;

    public Long tweetsNumber;

    public static List<Match> all() {
        return JPA.em().createQuery("FROM Match ORDER BY startDate DESC", Match.class).getResultList();
    }

    public static void save(Match match) {
        JPA.em().merge(match);
    }

    public static Match findById(Long id) {
        return JPA.em().find(Match.class, id);
    }

    @Override
    public String toString() {
        return homeTeam + " vs. " + awayTeam  + " (" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate) + ")";
    }
}
