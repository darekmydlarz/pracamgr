package pl.edu.agh.twitter.sentiment.cleaner;

import pl.edu.agh.twitter.entities.CountStrategy;
import pl.edu.agh.twitter.sentiment.Sentiment;

/**
 * Cleaner interfaces for cleaning text.
 */
public interface TextCleaner {
    Sentence clean(String text);
    CountStrategy getCountStrategy();

    static class Sentence {
        public String text;
        Sentiment sentiment;

        public String getText() {
            return text;
        }

        public Sentiment getSentiment() {
            return sentiment;
        }
    }
}
