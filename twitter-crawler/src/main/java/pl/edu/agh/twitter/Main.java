package pl.edu.agh.twitter;

import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.twitter.crawler.Polygon;
import pl.edu.agh.twitter.crawler.TwitterServiceProvider;
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

public class Main {
	private static final String[] TRACK = new String[] { 
		"mufc",  "manutd", "manchester united", "manchesterunited",
		"saf", "moyes", "De Gea", "ferguson", "alex ferguson",
		"Rafael", "Jones", "Evans", "Evra", "Carrick", "Giggs", "Valencia", "Rooney", "Kagawa", "Hernandez", "van Persie", "rvp",
		"Lindegaard", "Buttner", "Smalling", "Fellaini", "Nani", "Young", "Januzaj"
	};
	
	
	private static final String[] ARSENAL_CHELSEA = new String[] {
		"arsenal", "chelsea", "afc", "cfc", "arsene wenger", "jose mourinho", "special one",
		"emirates stadium", "capitalonecup", "capital one cup", 
		"Lukasz Fabianski", "thomas vermaelen", "nacho monreal", "laurent koscielny", "carl jenkinson",
		"thomas rosicky", "santi cazorla", "jack wilshere", "aaron ramsey", "nicklas bendtner",
		"mark schwarzer", "gary cahill", "david luiz", "cesar azplicueta", "ryan bertrand", 
		"mickael essien", "john obi mikel", "wilian", "kevin de bruyne", "samuel eto", "juan mata",
		"emilio viviano", "chu-young park", "issac hayden", "bakari sagna", "mesut ozil", "olivier giroud",
		"ramires", "fernando torres", "demba ba", "jamal blackman", "branislav ivanovic", "eden hazard",
		"tomas kalas"
	};
	
	private static final String[] LANGUAGES = new String[] { "en" };
	private static TwitterStream twitterStream;

	public static void main(String[] args) {
		twitterStream = TwitterServiceProvider.getTwitterStream();
		consume();
	}

	private static FilterQuery filterQuery() {
		final FilterQuery query = new FilterQuery();
//		query.locations(Polygon.England.getTwitter4jRepresentation());
		query.language(LANGUAGES);
		query.track(ARSENAL_CHELSEA);
		return query;
	}

	private static void consume() {
		twitterStream.addListener(listener);
		twitterStream.filter(filterQuery());
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

	private static StatusListener listener = new StatusListener() {
		public void onStatus(Status status) {
			try {
				if(status.getGeoLocation() != null) {
					Tweet tweet = new Tweet(status, createOrGetUser(status.getUser()));
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
		}
	};
}
