package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.twitter.entities.tweet.Tweet;
import pl.edu.agh.twitter.entities.user.UserDAO;
import pl.edu.agh.twitter.entities.user.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It is crawling are gathered tweets. When a retweet is found (its recognized by a regex) then code below is looking up
 * for a user which tweet has been retweeted.
 */
public class RetweetUserIdFiller implements Startable {
    Logger logger = LoggerFactory.getLogger(getClass());

    static Pattern pattern = Pattern.compile("RT @(\\S+):.+");

    Map<String, Long> usersIds = Maps.newHashMap();

    @Inject
    EntityManager em;

    @Inject
    UserDAO userDAO;

    @Override
    public void start() {
        usersIds.put("", -1l);
        int limit = 1_000, max = 2_160_000;
        for(int offset = 0; offset < max; offset += limit) {
            logger.info("Offset {} of {}", offset, max);
            final Iterator<Tweet> it = find(offset, limit);
            List<Tweet> tweets2Persist = Lists.newArrayList();
            logger.info("Found");
            while(it.hasNext()) {
                fillTweets2Persist(it.next(), tweets2Persist);
            }
            logger.info("Persisting...");
            persistAll(tweets2Persist);
        }
    }

    private void persistAll(List<Tweet> tweets2Persist) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();

        for (Tweet tweet : tweets2Persist) {
            em.merge(tweet);
        }

        transaction.commit();
    }

    private void fillTweets2Persist(Tweet tweet, List<Tweet> tweets2Persist) {
        final String username = extractUsername(tweet.getText());
        if(!usersIds.containsKey(username)) {
            final User user = userDAO.findByScreenName(username);
            Long usernameId = user != null ? user.getId() : -1;
            usersIds.put(username, usernameId);
        }
        final Long retweetUserId = usersIds.get(username);
        assert retweetUserId != null;
        tweet.setRetweetUserId(retweetUserId);
        tweets2Persist.add(tweet);
        logger.info("Processed: {}", tweets2Persist.size());
    }

    Iterator<Tweet> find(int offset, int limit) {
        return em.createNamedQuery(Tweet.FIND_RETWEETED_TWEETS, Tweet.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                .iterator();
    }

    public static String extractUsername(String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            return matcher.group(1).toLowerCase();
        }
        return "";
    }
}
