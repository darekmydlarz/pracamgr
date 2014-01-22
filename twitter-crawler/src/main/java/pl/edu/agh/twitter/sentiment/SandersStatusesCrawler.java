package pl.edu.agh.twitter.sentiment;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.classifiedtweet.boundary.ClassifiedTweetDAO;
import pl.edu.agh.twitter.business.classifiedtweet.entity.ClassifiedTweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.internal.http.HttpResponseCode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Singleton
public class SandersStatusesCrawler implements Startable {
    private Twitter twitter = new TwitterFactory().getInstance();
    private Logger logger = Logger.getLogger(SandersStatusesCrawler.class);

    @Inject
    private ClassifiedTweetDAO classifiedTweetDAO;

    public void start() {
        getStatuses();
    }

    private void getStatuses() {
        List<String> words = Lists.newArrayList();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("corpus.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            CSVReader reader = new CSVReader(br);
            String[] line;
            while ((line = reader.readNext()) != null) {
                if(!"irrelevant".equals(line[1])) {
                    fetchStatus(line[0], line[1], Long.parseLong(line[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchStatus(String keyword, String sentiment, long id) {
        try {
            persistClassifiedTweet(keyword, sentiment, id);
        } catch (TwitterException e) {
            if (e.getStatusCode() == HttpResponseCode.TOO_MANY_REQUESTS) {
                final int secondsUntilReset = e.getRateLimitStatus().getSecondsUntilReset();
                logger.info("TwitterException: You have to wait for: " + secondsUntilReset / 60 + ":" + secondsUntilReset % 60);
                try {
                    Thread.sleep(secondsUntilReset * 1000);
                    persistClassifiedTweet(keyword, sentiment, id);
                } catch (InterruptedException | TwitterException e1) {
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    private void persistClassifiedTweet(String keyword, String sentiment, long id) throws TwitterException {
        String text = twitter.showStatus(id).getText();
        ClassifiedTweet tweet = new ClassifiedTweet(id, text, sentiment, keyword);
        classifiedTweetDAO.persist(tweet);
    }
}
