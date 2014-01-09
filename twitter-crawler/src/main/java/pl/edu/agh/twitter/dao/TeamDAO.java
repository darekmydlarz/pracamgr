package pl.edu.agh.twitter.dao;

import pl.edu.agh.twitter.model.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class TeamDAO {
    @Inject
    private EntityManager em;

    public Team find(String name) {
        final String query = "FROM Team WHERE lower(name) LIKE :name";

        return em.createQuery(query, Team.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getSingleResult();
    }
}
