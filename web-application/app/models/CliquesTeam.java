package models;

import com.google.common.collect.Lists;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "cliques_team")
public class CliquesTeam {
    @Id
    long id;

    long teamId;

    long cliqueSize;

    @Transient
    public double positivness;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliqueId")
    public Set<CliquesTeamUser> users;

    public static List<CliquesTeam> findForTeam(Long teamId) {
        final String query = "FROM CliquesTeam WHERE teamId = :teamId";
        return JPA.em().createQuery(query, CliquesTeam.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }

    public static List<CliquesTeam> findForUser(List<CliquesTeamUser> users) {
        final String query = "FROM CliquesTeam c WHERE c.id IN :cliquesIds";
        final List<Long> cliquesIds = getCliquesIds(users);
        System.out.println("Dario1.0 == " + cliquesIds);
        return JPA.em().createQuery(query, CliquesTeam.class)
                .setParameter("cliquesIds", cliquesIds)
                .getResultList();
    }

    private static List<Long> getCliquesIds(Collection<CliquesTeamUser> users) {
        List<Long> cliquesIds = Lists.newArrayList();
        for(CliquesTeamUser user : users) {
            cliquesIds.add(user.cliqueId);
        }
        return cliquesIds;
    }

    @PostLoad
    private void postLoad() {
        double sum = 0.0;
        for(CliquesTeamUser user : users) {
            sum += user.positivness;
        }
        positivness = sum / users.size();
    }
}
