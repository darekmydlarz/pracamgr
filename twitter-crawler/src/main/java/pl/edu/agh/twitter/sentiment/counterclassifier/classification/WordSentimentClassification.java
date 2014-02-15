package pl.edu.agh.twitter.sentiment.counterclassifier.classification;

import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;
import pl.edu.agh.twitter.business.wordfrequency.boundary.WordFrequencyDAO;
import pl.edu.agh.twitter.business.wordfrequency.entity.WordFrequency;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;
import pl.edu.agh.twitter.sentiment.Sentiment;
import pl.edu.agh.twitter.sentiment.counterclassifier.RegexTextSplitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Singleton
public class WordSentimentClassification implements Startable {
    private static final DecimalFormat decimalFormat = new DecimalFormat("00.00%");
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
        final Map<String, WordFrequency> frequencyMap = wordFrequencyDAO.fetchAll(5l, CountStrategy.NOISE_CLEAN);
        wordSentimentClassifier = new WordSentimentClassifier(frequencyMap, new RegexTextSplitter("\\s"));
        List<Tweet> tweets = tweetDAO.getWithEmoticons(new Random().nextInt(100000), 200);
        System.out.println("Expected\tActual\tActual %\tText\tDetails");
        for (Tweet tweet : tweets) {
            classify(tweet.getText());
        }
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
