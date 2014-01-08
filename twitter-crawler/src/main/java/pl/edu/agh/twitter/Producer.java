package pl.edu.agh.twitter;


import org.apache.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Producer {

    private final Logger logger = Logger.getLogger(Producer.class);

    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return Persistence.createEntityManagerFactory("mgr").createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        em.close();
        logger.info("EntityManager CLOSED");
    }
}
