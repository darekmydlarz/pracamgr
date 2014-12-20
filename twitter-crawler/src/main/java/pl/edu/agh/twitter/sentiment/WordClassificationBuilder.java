package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.entities.TweetDAO;
import pl.edu.agh.twitter.entities.Tweet;
import pl.edu.agh.twitter.entities.WordFrequencyDAO;
import pl.edu.agh.twitter.entities.WordFrequency;
import pl.edu.agh.twitter.sentiment.cleaner.NegationIrrelevantRemovingCleaner;
import pl.edu.agh.twitter.sentiment.cleaner.TextCleaner;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * It count how many times each word where used in positive and negative context.
 * It is crawling all tweets with emoticons and detects in which context where used.
 * After that it stores into database number of occurences in each context.
 */
public class WordClassificationBuilder implements Startable {
    public static final int OFFSET = 0;
    private static Logger logger = Logger.getLogger(WordClassificationBuilder.class);
    private final TextCleaner irrelevantRemover = new NegationIrrelevantRemovingCleaner();

    @Inject
    private TweetDAO tweetDAO;
    @Inject
    private WordFrequencyDAO wordFrequencyDAO;

    @Override
    public void start() {
        logger.info("Started!");
        // I'm learning with 80% of tweets with emots (0 - 82940 rownum())
//        List<Tweet> tweets = tweetDAO.getWithEmoticons(OFFSET, ParoubekClassifier.TRAIN_LENGTH);
        // I'm learning with 100% of tweets with emots
        List<Tweet> tweets = tweetDAO.getAllWithEmoticons();
        logger.info("Found " + tweets.size() + " tweets witch emoticon");
        WordSentimentCounter classifier = new WordSentimentCounter(irrelevantRemover);
        for (Tweet tweet : tweets) {
            logger.info("consume: " + tweet.getText());
            classifier.consume(tweet.getText());
        }
        final Map<String, WordFrequency> frequencyMap = mergeEntries(classifier.getPositivesSorted(), classifier.getNegativesSorted());
        wordFrequencyDAO.persistAll(frequencyMap.values());
    }

    private Map<String, WordFrequency> mergeEntries(Map<String, Integer> positives, Map<String, Integer> negatives) {
        Map<String, WordFrequency> merged = Maps.newHashMap();
        for(String word : positives.keySet()) {
            final WordFrequency frequency = new WordFrequency(word, irrelevantRemover.getCountStrategy());
            frequency.setPositive(positives.get(word));
            merged.put(word, frequency);
        }
        for(String word : negatives.keySet()) {
            final WordFrequency frequency = merged.containsKey(word) ? merged.get(word) : new WordFrequency(word, irrelevantRemover.getCountStrategy());
            frequency.setNegative(negatives.get(word));
            merged.put(word, frequency);
        }
        return merged;
    }
}
