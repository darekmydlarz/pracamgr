package pl.edu.agh.twitter.sentiment.cleaner;

import pl.edu.agh.twitter.entities.wordfrequency.CountStrategy;
import pl.edu.agh.twitter.sentiment.Sentiment;

/**
 * Cleaner interfaces for cleaning text.
 */
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
