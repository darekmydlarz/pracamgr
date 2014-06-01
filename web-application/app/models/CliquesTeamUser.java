package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.db.jpa.JPA;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Entity
@Table(schema = "mgr", name = "cliques_team_user")
public class CliquesTeamUser implements Comparable<CliquesTeamUser> {
    @Id
    public long id;
    public long positives;
    public long negatives;
    public long cliqueId;
    public long userId;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "cliqueId", insertable = false, updatable = false)
//    public CliquesTeam cliquesTeam;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    public User user;

    @Transient
    public double positivness;

    public long occurences;

    public static List<CliquesTeamUser> findForUser(Long userId) {
        final String query = "FROM CliquesTeamUser c WHERE userId = :userId";
        return JPA.em().createQuery(query, CliquesTeamUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public static TreeSet<CliquesTeamUser> findForTeam(List<CliquesTeam> cliquesTeams) {
        final String query = "FROM CliquesTeamUser c " +
                " WHERE cliqueId IN :cliquesIds " +
                "   AND c.occurences > 1" +
                " ORDER BY c.occurences DESC";
        List<Long> cliquesIds = getCliquesIds(cliquesTeams);
        return new TreeSet<>(JPA.em().createQuery(query, CliquesTeamUser.class)
                .setParameter("cliquesIds", cliquesIds)
                .getResultList());
    }

    private static List<Long> getCliquesIds(List<CliquesTeam> cliquesTeams) {
        final ArrayList<Long> list = Lists.newArrayList();
        for(CliquesTeam team : cliquesTeams) {
            list.add(team.id);
        }
        return list;
    }

    @PostLoad
    private void postLoad() {
        positivness = 100.0 * positives / (positives + negatives);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CliquesTeamUser)) return false;

        CliquesTeamUser that = (CliquesTeamUser) o;

        if (negatives != that.negatives) return false;
        if (occurences != that.occurences) return false;
        if (positives != that.positives) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (positives ^ (positives >>> 32));
        result = 31 * result + (int) (negatives ^ (negatives >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (int) (occurences ^ (occurences >>> 32));
        return result;
    }

    @Override
    public int compareTo(CliquesTeamUser o) {
        return Long.compare(o.occurences, occurences);
    }
}
