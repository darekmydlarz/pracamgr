package pl.edu.agh.twitter.sentiment.crawler;

import au.com.bytecode.opencsv.CSVReader;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.classifiedtweet.boundary.ClassifiedTweetDAO;
import pl.edu.agh.twitter.business.classifiedtweet.entity.ClassifiedTweet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Singleton
public class Sentiment140Crawler implements Startable {

    @Inject
    private ClassifiedTweetDAO classifiedTweetDAO;

    private String getSentiment(int number) {
        switch (number) {
            case 4:
                return "positive";
            case 2:
                return "neutral";
            default:
                return "negative";
        }
    }

    @Override
    public void start() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("sentiment140.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            CSVReader reader = new CSVReader(br);
            String[] line;
            while ((line = reader.readNext()) != null) {
                persistClassifiedTweet(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void persistClassifiedTweet(String[] line) {
        final ClassifiedTweet tweet = new ClassifiedTweet();
        tweet.setSentiment(getSentiment(Integer.parseInt(line[0])));
        tweet.setKeyword(line[3]);
        tweet.setText(line[5]);
        classifiedTweetDAO.persist(tweet);
    }
}
