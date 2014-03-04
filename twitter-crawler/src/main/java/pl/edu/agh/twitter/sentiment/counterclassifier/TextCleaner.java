package pl.edu.agh.twitter.sentiment.counterclassifier;

import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;
import pl.edu.agh.twitter.sentiment.Sentiment;

public interface TextCleaner {
    Sentence clean(String text);
    CountStrategy getCountStrategy();

    static class Sentence {
        String text;
        Sentiment sentiment;

        public String getText() {
            return text;
        }

        public Sentiment getSentiment() {
            return sentiment;
        }
    }
}
