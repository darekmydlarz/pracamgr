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



    public static Sentiment getSentimentByEmoticon(String sentence) {
        Map<String, Integer> emoticonsCounter = new HashMap<>();
        for(String key : emoticonsSentiment.keySet())
            putIfMatched(sentence, emoticonsCounter, key);
        String mostOftenEmoticon = getMostOftenEmoticon(emoticonsCounter);
        return emoticonsSentiment.get(mostOftenEmoticon);
    }

    private static String getMostOftenEmoticon(Map<String, Integer> emoticonsCounter) {
        String mostOftenKey = "";
        int maxCount = 0;
        for(String key : emoticonsCounter.keySet())
            if (emoticonsCounter.get(key) > maxCount) {
                maxCount = emoticonsCounter.get(key);
                mostOftenKey = key;
            }
        return mostOftenKey;
    }

    private static void putIfMatched(String sentence, Map<String, Integer> emoticonsCounter, String key) {
        if(sentence.contains(key)) {
            Integer value = emoticonsCounter.containsKey(key) ? emoticonsCounter.get(key) : 0;
            emoticonsCounter.put(key, value + 1);
        }
    }
}
