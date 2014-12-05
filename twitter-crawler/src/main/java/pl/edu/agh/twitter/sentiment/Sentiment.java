package pl.edu.agh.twitter.sentiment;

/**
 * Sentiment ENUM for easier use.
 */
public enum Sentiment {
    POS, NEG, NEU;

    public static Sentiment getInstanceByFullName(String sentiment) {
        return valueOf(sentiment.substring(0, 3).toUpperCase());
    }
}
