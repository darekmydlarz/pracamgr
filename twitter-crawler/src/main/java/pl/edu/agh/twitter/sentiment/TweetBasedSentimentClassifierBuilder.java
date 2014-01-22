package pl.edu.agh.twitter.sentiment;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import pl.edu.agh.twitter.business.classifiedtweet.boundary.ClassifiedTweetDAO;
import pl.edu.agh.twitter.business.classifiedtweet.entity.ClassifiedTweet;

import javax.inject.Inject;
import java.util.*;

public class TweetBasedSentimentClassifierBuilder implements SentimentClassifierBuilder {

    @Inject
    private ClassifiedTweetDAO classifiedTweetDAO;

    @Override
    public Classifier<String, String> sentimentClassifier(int capacity) {
        Classifier<String, String> bayes = new BayesClassifier<>();
        bayes.setMemoryCapacity(capacity);
        learnClassification(bayes);
        return bayes;
    }

    @Override
    public String name() {
        return "TweetBased";
    }

    private void learnClassification(Classifier<String, String> classifier) {
        final Map<Sentiment, List<String>> classificationMap = buildClassificationMap();
        for(Sentiment sentiment : classificationMap.keySet()) {
            classifier.learn(sentiment.name(), classificationMap.get(sentiment));
        }
    }

    private Map<Sentiment, List<String>> buildClassificationMap() {
        Map<Sentiment, List<String>> classificationMap = new HashMap<>();
        for(ClassifiedTweet tweet : classifiedTweetDAO.all()) {
            Sentiment sentiment = Sentiment.getInstanceByFullName(tweet.getSentiment());
            if(Arrays.asList(Sentiment.POS, Sentiment.NEG).contains(sentiment)) {
                if(classificationMap.containsKey(sentiment)) {
                    String[] text = tweet.getText().split("\\s");
                    for(String word : text) {
                        classificationMap.get(sentiment).add(word);
                    }
                } else {
                    classificationMap.put(sentiment, new ArrayList<String>());
                }
            }
        }
        return classificationMap;
    }
}
