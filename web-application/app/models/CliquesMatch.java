package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "cliques_match")
public class CliquesMatch {
    @Id
    public long id;

    public long matchId;

    public long cliqueSize;

    @Transient
    public double positivness;

    @ManyToOne
    @JoinColumn(name = "matchId", insertable = false, updatable = false)
    @JsonIgnore
    public Match match;

    @OneToMany(mappedBy = "cliquesMatch", fetch = FetchType.EAGER)
    public Set<CliquesMatchUser> users;

    public static List<CliquesMatch> findForMatch(Long matchId) {
        final String query = "FROM CliquesMatch WHERE matchId = :matchId";
        return JPA.em().createQuery(query, CliquesMatch.class)
                .setParameter("matchId", matchId)
                .getResultList();
    }

    @PostLoad
    private void postLoad() {
        double sum = 0.0;
        for(CliquesMatchUser user : users) {
            sum += user.positivness;
        }
        positivness = sum / users.size();
    }
}
