package pl.edu.agh.twitter.sentiment.bayesclassifier;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.sentiment.Sentiment;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Singleton
public class BayesBasedClassification implements Startable {
    private static final DecimalFormat decimalFormat = new DecimalFormat("00.00%");

    @Inject
    private TweetBasedBayesClassifierBuilder tweetBasedClassifierBuilder;

    @Inject
    private WordBasedBayesClassifierBuilder wordBasedClassifierBuilder;

    @Inject
    private TweetDAO tweetDAO;

    @Override
    public void start() {
        final Classifier<String,String> tweetBased = tweetBasedClassifierBuilder.sentimentClassifier(10000);
        final Classifier<String, String> wordBased = wordBasedClassifierBuilder.sentimentClassifier(10000);

        List<Tweet> tweets = tweetDAO.getWithEmoticons(new Random().nextInt(100000), 100);
        System.out.println("Expected ;)\tTweetBased\t\tWordBased\t\tText");
        for(Tweet tweet : tweets) {
            classify(tweetBased, wordBased, tweet);
        }
    }

    private void classify(Classifier<String, String> tweetBased, Classifier<String, String> wordBased, Tweet tweet) {
        List<String> tweetText = Arrays.asList(tweet.getText().split("\\s"));
        String tweetBasedValue = calculateSentiment(tweetBased, tweetText);
        String wordBasedValue = calculateSentiment(wordBased, tweetText);
        Sentiment emoticonSentiment = EmoticonClassifier.getSentimentByEmoticon(tweet.getText());
        System.out.println(
                emoticonSentiment.name() + "\t" +
                tweetBasedValue + "\t" +
                wordBasedValue + "\t" +
                tweet.getText().replaceAll("\n", " ")
        );
    }

    private String calculateSentiment(Classifier<String, String> classifier, List<String> tweetText) {
        final List<Classification<String,String>> allClassifications = new ArrayList<>(((BayesClassifier<String, String>) classifier).classifyDetailed(tweetText));
        final Classification<String, String> chosenClassification = classifier.classify(tweetText);
        double probabilitiesSum = 0.0;
        for(Classification<String, String> c : allClassifications)
            probabilitiesSum += c.getProbability();
        double percentage = chosenClassification.getProbability() / probabilitiesSum;

        StringBuilder sb = new StringBuilder();
        if(percentage == 0.5) {
            sb.append("\t\t");
        } else {
            sb.append(decimalFormat.format(percentage)).append("\t").append(chosenClassification.getCategory()).append("\t");
        }
        return sb.toString();
    }
}
