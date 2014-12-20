package pl.edu.agh.twitter.sentiment;

import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.entities.TweetDAO;
import pl.edu.agh.twitter.entities.Tweet;
import pl.edu.agh.twitter.entities.WordFrequencyDAO;
import pl.edu.agh.twitter.entities.WordFrequency;
import pl.edu.agh.twitter.sentiment.cleaner.StopListCleaner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class performs classification of tweets sentiment and prints results to output.
 * It was created for testing of cleaners.
 */
@Singleton
public class WordSentimentClassification implements Startable {
    private static final DecimalFormat decimalFormat = new DecimalFormat("00.00%");
    private final StopListCleaner cleaner = new StopListCleaner();
    private WordSentimentClassifier wordSentimentClassifier;

    @Inject
    private WordFrequencyDAO wordFrequencyDAO;

    @Inject
    private TweetDAO tweetDAO;

    @Override
    public void start() {
        performClassification();
    }

    private void performClassification() {
        final Map<String, WordFrequency> frequencyMap = wordFrequencyDAO.fetchAll(5l, 0, cleaner.getCountStrategy());
        wordSentimentClassifier = new WordSentimentClassifier(frequencyMap, cleaner);
        List<Tweet> tweets = tweetDAO.getWithoutRetweets(new Random().nextInt(100000), 10);
        System.out.println("Actual\tActual %\tText\tDetails");
        for (Tweet tweet : tweets) {
            classifyByHand(tweet.getText());
        }
    }

    private void classifyByHand(String sentence) {
        final WordSentimentClassifier.Classification classification = wordSentimentClassifier.classify(sentence);
        Sentiment actual = classification.getSentiment();
        double percentage = classification.getSentimentPercentage();
        System.out.println(
                actual.name() + "\t" +
                        decimalFormat.format(percentage) + "\t\t" +
                        sentence.replaceAll("\n", " ") + "\t" +
                        classification.detailsMap().values()
        );
    }

    private void classify(String sentence) {
        Sentiment emoticonSentiment = EmoticonClassifier.getSentimentByEmoticon(sentence);
        final WordSentimentClassifier.Classification classification = wordSentimentClassifier.classify(sentence);
        Sentiment actual = classification.getSentiment();
        double percentage = classification.getSentimentPercentage();
        System.out.println(
                emoticonSentiment.name() + "\t" +
                        actual.name() + "\t" +
                        decimalFormat.format(percentage) + "\t" +
                        sentence.replaceAll("\n", " ") + "\t" +
                        classification.detailsMap().values()
        );
    }
}
