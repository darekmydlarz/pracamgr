package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(schema = "mgr", name = "geodata")
public class Geodata implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "tweetId")
    @JsonIgnore
    public Tweet tweet;

    @Transient
    public long positives;
    @Transient
    public long count;
    @Transient
    public double percentage;
    @Transient
    public String teritory;

    public static List<Geodata> topCountries(Match match) {
        return topColumn(match, "country");
    }

    private static List<Geodata> topColumn(Match match, String column) {
        final String query = " SELECT  " +
                " 	a."+column+",  " +
                " 	count(*),  " +
                " 	sum(CAST(a.positivness AS int))  " +
                " FROM ( " +
                "   SELECT " +
                "     "+column+", " +
                "     pt.valence > 0.4786984978198536 AS positivness " +
                "   FROM  " +
                "   	mgr.tweets t JOIN  " +
                "   	mgr.geodata g ON t.id = g.tweet_id JOIN " +
                "   	mgr.paroubek_tweets pt ON t.id = pt.tweet_id " +
                "   WHERE match_event = :matchId AND " +
                "     "+column+" IS NOT NULL " +
                " ) a " +
                " GROUP BY a."+column+" " +
                " ORDER BY count(*) DESC  ";
        final Iterator<Object[]> iterator = JPA.em().createNativeQuery(query)
                .setParameter("matchId", match.id)
                .setMaxResults(30)
                .getResultList().iterator();
        final List<Geodata> geodataList = Lists.newArrayList();
        while(iterator.hasNext()) {
            Object[] row = iterator.next();
            Geodata geodata = new Geodata();
            geodata.teritory = (String) row[0];
            geodata.count = ((Number) row[1]).longValue();
            geodata.positives = ((Number) row[2]).longValue();
            geodata.percentage = 100.0 * geodata.count / match.geotagged;
            geodataList.add(geodata);
        }
        return geodataList;
    }

    public static List<Geodata> topState(Match match) {
        return topColumn(match, "state");
    }

    public static List<Geodata> topCounty(Match match) {
        return topColumn(match, "county");
    }

    public static List<Geodata> topCity(Match match) {
        return topColumn(match, "city");
    }
}
