package pl.edu.agh.twitter.business.team.boundary;

import pl.edu.agh.twitter.business.team.entity.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

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
        final EntityTransaction transaction = em.getTransaction();
        final String query = "FROM Team WHERE lower(name) LIKE :name";
        List<Team> competitionList = em.createQuery(query, Team.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
        transaction.commit();
        return competitionList.isEmpty() ? null : competitionList.get(0);
    }
}
