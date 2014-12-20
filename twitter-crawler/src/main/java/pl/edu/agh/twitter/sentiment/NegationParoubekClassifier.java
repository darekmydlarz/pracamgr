package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.entities.ParoubekTweetDAO;
import pl.edu.agh.twitter.entities.ParoubekTweet;
import pl.edu.agh.twitter.entities.TweetDAO;
import pl.edu.agh.twitter.entities.Tweet;
import pl.edu.agh.twitter.entities.WordFrequencyDAO;
import pl.edu.agh.twitter.entities.WordFrequency;
import pl.edu.agh.twitter.sentiment.cleaner.NegationIrrelevantRemovingCleaner;
import pl.edu.agh.twitter.sentiment.cleaner.TextCleaner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Sentiment classifier. Build on Pak&Paroubek algorithm with my negation detecting change.
 * It crawls all gathered tweets, counts #valence value for them and store data in database.
 */
public class NegationParoubekClassifier implements Startable {
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
        final Map<String, Double> sentenceWordsValence;
        private final String tweetText;

        private ParoubekSentence(Tweet tweet, String text, Double valence, Map<String, Double> sentenceWordsValence) {
            this.tweetId = tweet.getId();
            this.tweetText = tweet.getText();
            this.text = text;
            this.valence = valence;
            this.sentenceWordsValence = sentenceWordsValence;
        }

        @Override
        public String toString() {
            String sentiment = getSentiment(valence);
            return valence + "\t" + sentiment + "\t" + tweetText + "\t" + sentenceWordsValence;
        }

        static class Builder {
            final String text;
            final Map<String, WordFrequency> frequencyMap;
            private Map<String, Double> sentenceWordsValence = Maps.newHashMap();
            private Tweet tweet;

            Builder(Tweet tweet, Map<String, WordFrequency> frequencyMap) {
                this.tweet = tweet;
                text = TEXT_CLEANER.clean(tweet.getText()).getText();
                this.frequencyMap = frequencyMap;
            }

            public ParoubekSentence build() {
                final Double valence = countSentenceValence(getWordsValences());
                return new ParoubekSentence(tweet, text, valence, sentenceWordsValence);
            }

            private List<Double> getWordsValences() {
                List<Double> wordValences = Lists.newArrayList();
                for(String word : text.split("\\s")) {
                    if(frequencyMap.containsKey(word)) {
                        Double currentWordValence = frequencyMap.get(word).getValence();
                        wordValences.add(currentWordValence);
                        sentenceWordsValence.put(word, currentWordValence);
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

    static String getSentiment(double valence) {
        return valence > ParoubekTweet.VALENCE_AVG ? "POS" : "NEG";
    }

    @Override
    public void start() {
        logger.info("Started...");
//        int offset = 0;

        int offset = new Random().nextInt(7_000_000);
        final int limit = 100;
        while(offset + limit < TWEETS_NUMBER) {
            logger.info("In progress...\t" + offset + "/" + TWEETS_NUMBER);
            final List<Tweet> tweets = tweetDAO.getTweetsLimitOffset(limit, offset);
            logger.info("Tweets fetched...");
//            processTweets(tweets);
            showInfo(tweets);
            offset += limit;
        }
        logger.info("END!");
    }

    private void showInfo(List<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            ParoubekSentence sentence = new ParoubekSentence.Builder(tweet, frequencyMap).build();
            System.out.println(sentence);
        }

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