package pl.edu.agh.twitter.sentiment;

import de.daslaboratorium.machinelearning.classifier.Classifier;

public interface SentimentClassifierBuilder {
    Classifier<String, String> sentimentClassifier(int capacity);

    public enum Sentiment {
        POS, NEG, NEU, BOT, WEA;

        public static Sentiment getInstanceByFullName(String sentiment) {
            return valueOf(sentiment.substring(0, 3).toUpperCase());
        }
    }

    String name();
}
