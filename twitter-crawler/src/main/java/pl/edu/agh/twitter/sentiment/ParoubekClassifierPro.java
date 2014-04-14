package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.paroubektweet.boundary.ParoubekTweetDAO;
import pl.edu.agh.twitter.business.paroubektweet.entity.ParoubekTweet;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.business.wordfrequency.boundary.WordFrequencyDAO;
import pl.edu.agh.twitter.business.wordfrequency.entity.WordFrequency;
import pl.edu.agh.twitter.sentiment.counterclassifier.NegationIrrelevantRemovingCleaner;
import pl.edu.agh.twitter.sentiment.counterclassifier.TextCleaner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class ParoubekClassifierPro implements Startable {
    private final Logger logger = Logger.getLogger(getClass());
    public static final TextCleaner TEXT_CLEANER = new NegationIrrelevantRemovingCleaner();
    public static final int TWEETS_NUMBER = 7_773_105;
    public static final int MIN_WORD_LENGTH = 3;
    public static final int MIN_FREQUENCY = 1;
    private Map<String, WordFrequency> frequencyMap;

    static class ParoubekSentence {
        final Long tweetId;
        final String text;
        final Double valence;

        private ParoubekSentence(Long tweetId, String text, Double valence) {
            this.tweetId = tweetId;
            this.text = text;
            this.valence = valence;
        }

        static class Builder {
            final Long tweetId;
            final String text;
            final Map<String, WordFrequency> frequencyMap;

            Builder(Tweet tweet, Map<String, WordFrequency> frequencyMap) {
                tweetId = tweet.getId();
                text = TEXT_CLEANER.clean(tweet.getText()).getText();
                this.frequencyMap = frequencyMap;
            }

            public ParoubekSentence build() {
                final Double valence = countSentenceValence(getWordsValences());
                return new ParoubekSentence(tweetId, text, valence);
            }

            private List<Double> getWordsValences() {
                List<Double> wordValences = Lists.newArrayList();
                for(String word : text.split("\\s")) {
                    if(frequencyMap.containsKey(word)) {
                        wordValences.add(frequencyMap.get(word).getValence());
                    }
                }
                return wordValences;
            }

            private Double countSentenceValence(List<Double> wordValences) {
                if(wordValences.isEmpty())
                    return Double.NaN;
                Double valence = 0.0;
                for(Double value : wordValences) {
                    valence += value;
                }
                return valence / wordValences.size();
            }
        }
    }

    @Inject
    TweetDAO tweetDAO;

    @Inject
    WordFrequencyDAO wordFrequencyDAO;

    @Inject
    ParoubekTweetDAO paroubekTweetDAO;

    @PostConstruct
    private void init() {
        logger.info("Fetch frequency map...");
        frequencyMap = wordFrequencyDAO.fetchAll(MIN_FREQUENCY, MIN_WORD_LENGTH, TEXT_CLEANER.getCountStrategy());
        logger.info("OK!");
    }

    @Override
    public void start() {
        logger.info("Started...");
//        int offset = 0;

        int offset = 4_327_000;
        final int limit = 1_000;
        while(offset + limit < TWEETS_NUMBER) {
            logger.info("In progress...\t" + offset + "/" + TWEETS_NUMBER);
            final List<Tweet> tweets = tweetDAO.getTweetsLimitOffset(limit, offset);
            logger.info("Tweets fetched...");
            processTweets(tweets);
            offset += limit;
        }
        logger.info("END!");
    }

    private void processTweets(List<Tweet> tweets) {
        List<ParoubekTweet> paroubekTweets = Lists.newArrayList();
        for(Tweet tweet : tweets) {
            ParoubekSentence sentence = new ParoubekSentence.Builder(tweet, frequencyMap).build();
            paroubekTweets.add(new ParoubekTweet(sentence.tweetId, sentence.text, sentence.valence));
        }
        paroubekTweetDAO.persistAll(paroubekTweets);
        logger.info("Persisted!");
    }
}
