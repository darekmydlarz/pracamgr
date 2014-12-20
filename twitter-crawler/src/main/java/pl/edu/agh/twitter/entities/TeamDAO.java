package pl.edu.agh.twitter.entities;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO for entity
 */
public class TeamDAO {
    @Inject
    private EntityManager em;

    public Team createOrGet(String name) {
        Team team = get(name);
        if (team == null) {
            final EntityTransaction transaction = em.getTransaction();
            team = new Team(name);
            em.persist(team);
            transaction.commit();
        }
        return team;
    }

    public Team get(String name) {
        final String query = "FROM Team WHERE lower(name) LIKE :name";
        List<Team> teams = em.createQuery(query, Team.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
        return teams.isEmpty() ? null : teams.get(0);
    }
}
