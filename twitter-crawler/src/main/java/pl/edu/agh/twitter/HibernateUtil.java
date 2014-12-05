package pl.edu.agh.twitter;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Class needed to use Hibernate, to instantiate it, and shutdown after application shutdown.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
        try {
    	    Configuration configuration = new Configuration().setNamingStrategy(ImprovedNamingStrategy.INSTANCE).configure();
    	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
    	    	.applySettings(configuration.getProperties()).buildServiceRegistry();
            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void shutdown() {
    	getSessionFactory().close();
    }
 
}
