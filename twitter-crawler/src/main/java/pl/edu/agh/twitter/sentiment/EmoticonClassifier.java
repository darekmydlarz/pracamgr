package pl.edu.agh.twitter.sentiment;

import java.util.HashMap;
import java.util.Map;

public class EmoticonClassifier {
    private static final Map<String, Sentiment> emoticonsSentiment;

    static {
        emoticonsSentiment = new HashMap<>();
        emoticonsSentiment.put(":)", Sentiment.POS);
        emoticonsSentiment.put(":D", Sentiment.POS);
        emoticonsSentiment.put(":(", Sentiment.NEG);
        emoticonsSentiment.put(";)", Sentiment.POS);
        emoticonsSentiment.put(":-)", Sentiment.POS);
        emoticonsSentiment.put(":P", Sentiment.POS);
        emoticonsSentiment.put("=)", Sentiment.POS);
        emoticonsSentiment.put("(:", Sentiment.POS);
        emoticonsSentiment.put(";-)", Sentiment.POS);
        emoticonsSentiment.put(":/", Sentiment.NEG);
        emoticonsSentiment.put("XD", Sentiment.POS);
        emoticonsSentiment.put("=D", Sentiment.POS);
        emoticonsSentiment.put(":o", Sentiment.POS);
        emoticonsSentiment.put("=]", Sentiment.POS);
        emoticonsSentiment.put(";D", Sentiment.POS);
        emoticonsSentiment.put(":]", Sentiment.POS);
        emoticonsSentiment.put(":-(", Sentiment.NEG);
        emoticonsSentiment.put("=/", Sentiment.NEG);
        emoticonsSentiment.put("=(", Sentiment.NEG);
    }

    public static final String NEUTRAL_KEY = "";

    public static Sentiment getSentimentByEmoticon(String sentence) {
        Map<String, Integer> emoticonsCounter = new HashMap<>();
        for(String key : emoticonsSentiment.keySet())
            putIfMatched(sentence, emoticonsCounter, key);
        return getMostOftenSentiment(emoticonsCounter);
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

    private static void putIfMatched(String sentence, Map<String, Integer> emoticonsCounter, String key) {
        if(sentence.contains(key)) {
            Integer value = emoticonsCounter.containsKey(key) ? emoticonsCounter.get(key) : 0;
            emoticonsCounter.put(key, value + 1);
        }
    }
}
