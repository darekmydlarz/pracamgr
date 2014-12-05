package pl.edu.agh.twitter.entities.wordfrequency.boundary;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.wordfrequency.CountStrategy;
import pl.edu.agh.twitter.entities.wordfrequency.entity.WordFrequency;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

public class WordFrequencyDAO {
    private Logger logger = Logger.getLogger(getClass());

    @Inject
    private EntityManager em;

    public void persist(WordFrequency wordFrequency) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(wordFrequency);
        transaction.commit();
        logger.info(wordFrequency.getWord() + " PERSISTED");
    }

    public void persistAll(Collection<WordFrequency> frequencies) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        int i = 0;
        int size = frequencies.size();
        for(WordFrequency wordFrequency : frequencies) {
            logger.info("PERSIST " + ++i + "/" + size);
            em.persist(wordFrequency);
        }
        transaction.commit();
        logger.info(frequencies.size() + " words PERSISTED");
    }

    @Deprecated
    public Map<String, WordFrequency> fetchAll(long minimumFrequency) {
        final String query = "FROM WordFrequency WHERE positive > :min OR negative > :min";
        final Iterator<WordFrequency> iterator = em.createQuery(query, WordFrequency.class)
                .setParameter("min", minimumFrequency)
                .getResultList().iterator();

        Map<String, WordFrequency> frequencyMap = new HashMap<>();
        while(iterator.hasNext()) {
            WordFrequency wordFrequency = iterator.next();
            frequencyMap.put(wordFrequency.getWord(), wordFrequency);
        }
        return frequencyMap;
    }

    public Map<String, WordFrequency> fetchAll(long minimumFrequency, int wordLength, CountStrategy countStrategy) {
        final String query = "FROM WordFrequency " +
                " WHERE countStrategy = :countStrategy AND " +
                " positive + negative >= :min AND (" +
                " (word LIKE 'not_%' AND LENGTH(word) >= :notWordLength) OR " +
                " (word not LIKE 'not_%' AND LENGTH(word) >= :wordLength)" +
                ")";
        final Iterator<WordFrequency> iterator = em.createQuery(query, WordFrequency.class)
                .setParameter("min", minimumFrequency)
                .setParameter("countStrategy", countStrategy)
                .setParameter("notWordLength", wordLength + 4)
                .setParameter("wordLength", wordLength)
                .getResultList().iterator();

        Map<String, WordFrequency> frequencyMap = new HashMap<>();
        while(iterator.hasNext()) {
            WordFrequency wordFrequency = iterator.next();
            frequencyMap.put(wordFrequency.getWord(), wordFrequency);
        }
        return frequencyMap;
    }
}
