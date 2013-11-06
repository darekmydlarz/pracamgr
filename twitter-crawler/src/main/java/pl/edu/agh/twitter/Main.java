package pl.edu.agh.twitter;

import com.google.common.collect.Iterables;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.twitter.crawler.Polygon;
import pl.edu.agh.twitter.crawler.TwitterServiceProvider;
import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Tweet;
import pl.edu.agh.twitter.model.UserEntity;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.User;

import java.util.List;

public class Main {
	
	private static final String[] LANGUAGES = new String[] { "en" };
	private static TwitterStream twitterStream;

	public static void main(String[] args) {
		twitterStream = TwitterServiceProvider.getTwitterStream();
        MatchEvent barcelonaMilan = findMatchEvent("barcelona", "milan");
        System.out.println(barcelonaMilan.getId());
		consume(barcelonaMilan);
	}

    public static MatchEvent findMatchEvent(String homeTeam, String awayTeam) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        final String queryString = "FROM MatchEvent e WHERE lower(e.homeTeam.name) LIKE :homeTeam AND lower(e.awayTeam.name) LIKE :awayTeam";
        Query query = session.createQuery(queryString);
        final List items = query.setParameter("homeTeam", "%" + homeTeam.toLowerCase() + "%").setParameter("awayTeam", "%" + awayTeam.toLowerCase() + "%").list();
        MatchEvent matchEvent = (MatchEvent) items.get(0);
        session.close();
        return matchEvent;
    }

	private static FilterQuery filterQuery(MatchEvent matchEvent) {
		final FilterQuery query = new FilterQuery();
		query.language(LANGUAGES);
        query.track(Iterables.toArray(matchEvent.getKeywords(), String.class));
		return query;
	}

	private static void consume(MatchEvent matchEvent) {
		twitterStream.addListener(new MyStatusListener(matchEvent));
		twitterStream.filter(filterQuery(matchEvent));
	}

	private static UserEntity createOrGetUser(User user) throws TwitterException {
		UserEntity userEntity = getUser(user);
		if (userEntity != null)
			return userEntity;
		return persistUser(user);
	}

	private static UserEntity persistUser(User user) throws TwitterException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		final UserEntity userEntity = new UserEntity(user);
		session.persist(userEntity);
		transaction.commit();
		session.close();
		return userEntity;
	}

	private static UserEntity getUser(User user) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		final UserEntity userEntity = (UserEntity) session.get(UserEntity.class, user.getId());
		transaction.commit();
		session.close();
		return userEntity;
	}

	private static void persist(Tweet tweet) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(tweet);
		transaction.commit();
		session.close();
		System.out.println("Tweet " + tweet.getId() + " persisted");
	}

    public static class MyStatusListener implements StatusListener {
        private MatchEvent matchEvent;

        MyStatusListener(MatchEvent matchEvent) {
            this.matchEvent = matchEvent;
        }

        public void onStatus(Status status) {
			try {
				if(status.getGeoLocation() != null) {
					Tweet tweet = new Tweet(status, createOrGetUser(status.getUser()), matchEvent);
					persist(tweet);
				} else {
					System.out.println("Geo is NULL");
				}
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		}

		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		}

		public void onException(Exception ex) {
			ex.printStackTrace();
		}

		@Override
		public void onScrubGeo(long arg0, long arg1) {
		}

		@Override
		public void onStallWarning(StallWarning arg0) {
            System.out.println(arg0);
        }
	};
}
