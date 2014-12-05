package pl.edu.agh.twitter.sentiment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple classifier which is classyfing tweets sentiment by occurences of concrete emoticons in it.
 * It applies only to tweets with emoticons, though.
 */
public class EmoticonClassifier {
    private static final Map<String, Sentiment> emoticonsSentiment;

    static {
        emoticonsSentiment = new HashMap<>();
        emoticonsSentiment.put(":)", Sentiment.POS);
        emoticonsSentiment.put(":d", Sentiment.POS);
        emoticonsSentiment.put(":(", Sentiment.NEG);
        emoticonsSentiment.put(";)", Sentiment.POS);
        emoticonsSentiment.put(":-)", Sentiment.POS);
        emoticonsSentiment.put(":p", Sentiment.POS);
        emoticonsSentiment.put("=)", Sentiment.POS);
        emoticonsSentiment.put("(:", Sentiment.POS);
        emoticonsSentiment.put(";-)", Sentiment.POS);
        emoticonsSentiment.put(":/", Sentiment.NEG);
        emoticonsSentiment.put("xd", Sentiment.POS);
        emoticonsSentiment.put("=d", Sentiment.POS);
        emoticonsSentiment.put(":o", Sentiment.POS);
        emoticonsSentiment.put("=]", Sentiment.POS);
        emoticonsSentiment.put(";d", Sentiment.POS);
        emoticonsSentiment.put(":]", Sentiment.POS);
        emoticonsSentiment.put(":-(", Sentiment.NEG);
        emoticonsSentiment.put("=/", Sentiment.NEG);
        emoticonsSentiment.put("=(", Sentiment.NEG);
    }

    public static Sentiment getSentimentByEmoticon(final String text) {
        Map<String, Integer> emoticonsCounter = new HashMap<>();
        for(String key : emoticonsSentiment.keySet())
            putIfMatched(text, emoticonsCounter, key);
        return getMostOftenSentiment(emoticonsCounter);
    }

    private static void putIfMatched(final String text, Map<String, Integer> emoticonsCounter, String key) {
        final String pattern = Pattern.quote(key);
        Matcher matcher = Pattern.compile(pattern).matcher(text.toLowerCase());
        final boolean found = matcher.find();
        if(found) {
            Integer value = emoticonsCounter.containsKey(key) ? emoticonsCounter.get(key) : 0;
            emoticonsCounter.put(key, value + 1);
        }
    }

    private static Sentiment getMostOftenSentiment(Map<String, Integer> emoticonsCounter) {
        int positives = 0, negatives = 0;
        for(String emoticon : emoticonsCounter.keySet()) {
            final Integer emoticonOccurences = emoticonsCounter.get(emoticon);
            final Sentiment sentiment = emoticonsSentiment.get(emoticon);
            if(sentiment == Sentiment.POS)
                positives += emoticonOccurences;
            else if(sentiment == Sentiment.NEG)
                negatives += emoticonOccurences;
        }
        if(positives > negatives)
            return Sentiment.POS;
        if(negatives > positives)
            return Sentiment.NEG;
        return Sentiment.NEU;
    }
}
