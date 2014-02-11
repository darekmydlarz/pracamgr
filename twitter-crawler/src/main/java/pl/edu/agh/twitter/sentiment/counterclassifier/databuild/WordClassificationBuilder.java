package pl.edu.agh.twitter.sentiment.counterclassifier.databuild;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.business.wordfrequency.boundary.WordFrequencyDAO;
import pl.edu.agh.twitter.business.wordfrequency.entity.WordFrequency;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class WordClassificationBuilder implements Startable {
    @Inject
    private TweetDAO tweetDAO;
    @Inject
    private WordFrequencyDAO wordFrequencyDAO;
    private static Logger logger = Logger.getLogger(WordClassificationBuilder.class);

    @Override
    public void start() {
        List<Tweet> tweets = tweetDAO.getAllWithEmoticons();
        WordSentimentCounter classifier = new WordSentimentCounter();
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
            final WordFrequency frequency = new WordFrequency(word);
            frequency.setPositive(positives.get(word));
            merged.put(word, frequency);
        }
        for(String word : negatives.keySet()) {
            final WordFrequency frequency = merged.containsKey(word) ? merged.get(word) : new WordFrequency(word);
            frequency.setNegative(negatives.get(word));
            merged.put(word, frequency);
        }
        return merged;
    }
}
