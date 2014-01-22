package pl.edu.agh.twitter.sentiment;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.*;

@Singleton
public class SentimentClassification implements Startable {
    private static final DecimalFormat decimalFormat = new DecimalFormat("00.00%");

    private static final Map<String, SentimentClassifierBuilder.Sentiment> emoticonsSentiment;

    static {
        emoticonsSentiment = new HashMap<>();
        emoticonsSentiment.put(":)", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":D", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":(", SentimentClassifierBuilder.Sentiment.NEG);
        emoticonsSentiment.put(";)", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":-)", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":P", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put("=)", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put("(:", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(";-)", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":/", SentimentClassifierBuilder.Sentiment.NEG);
        emoticonsSentiment.put("XD", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put("=D", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":o", SentimentClassifierBuilder.Sentiment.NEG);
        emoticonsSentiment.put("=]", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(";D", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":]", SentimentClassifierBuilder.Sentiment.POS);
        emoticonsSentiment.put(":-(", SentimentClassifierBuilder.Sentiment.NEG);
        emoticonsSentiment.put("=/", SentimentClassifierBuilder.Sentiment.NEG);
        emoticonsSentiment.put("=(", SentimentClassifierBuilder.Sentiment.NEG);
    }

    @Inject
    private TweetBasedSentimentClassifierBuilder tweetBasedClassifierBuilder;

    @Inject
    private WordBasedSentimentClassifierBuilder wordBasedClassifierBuilder;

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
        SentimentClassifierBuilder.Sentiment emoticonSentiment = matchByEmoticon(tweet);
        System.out.println(
                emoticonSentiment.name() + "\t" +
                tweetBasedValue + "\t" +
                wordBasedValue + "\t" +
                tweet.getText().replaceAll("\n", " ")
        );
    }

    private SentimentClassifierBuilder.Sentiment matchByEmoticon(Tweet tweet) {
        Map<String, Integer> emoticonsCounter = new HashMap<>();
        for(String key : emoticonsSentiment.keySet()) {
            if(tweet.getText().contains(key)) {
                Integer value = emoticonsCounter.containsKey(key) ? emoticonsCounter.get(key) : 0;
                emoticonsCounter.put(key, value + 1);
            }
        }
        String mostOftenKey = "";
        int maxCount = 0;
        for(String key : emoticonsCounter.keySet()) {
            if (emoticonsCounter.get(key) > maxCount) {
                maxCount = emoticonsCounter.get(key);
                mostOftenKey = key;
            }
        }
        return emoticonsSentiment.get(mostOftenKey);
    }

    private String calculateSentiment(Classifier<String, String> classifier, List<String> tweetText) {
        final List<Classification<String,String>> allClassifications = new ArrayList<>(((BayesClassifier<String, String>) classifier).classifyDetailed(tweetText));
        final Classification<String, String> chosenClassification = classifier.classify(tweetText);
        double probabilitiesSum = 0.0;
        for(Classification<String, String> c : allClassifications)
            probabilitiesSum += c.getProbability();

        StringBuilder sb = new StringBuilder();
        double percentage = chosenClassification.getProbability() / probabilitiesSum;
        sb.append(decimalFormat.format(percentage)).append("\t").append(chosenClassification.getCategory()).append("\t");
        return sb.toString();
    }
}
