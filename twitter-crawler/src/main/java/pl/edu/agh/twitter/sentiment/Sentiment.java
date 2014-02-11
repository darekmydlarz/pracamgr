package pl.edu.agh.twitter.sentiment;

public enum Sentiment {
    POS, NEG, NEU, BOT, WEA;

    public static Sentiment getInstanceByFullName(String sentiment) {
        return valueOf(sentiment.substring(0, 3).toUpperCase());
    }
}
