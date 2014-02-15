package pl.edu.agh.twitter.sentiment.counterclassifier.databuild;

import com.google.common.collect.Maps;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;
import pl.edu.agh.twitter.sentiment.Sentiment;
import pl.edu.agh.twitter.sentiment.counterclassifier.TextSplitter;

import java.util.*;

public class WordSentimentCounter {
    private Map<String, Integer> positives = Maps.newHashMap();
    private Map<String, Integer> negatives = Maps.newHashMap();
    private final TextSplitter splitter;

    public WordSentimentCounter(TextSplitter splitter) {
        this.splitter = splitter;
    }

    public void consume(String sentence) {
        Sentiment sentiment = EmoticonClassifier.getSentimentByEmoticon(sentence);
        if (sentiment == Sentiment.POS) {
            addEntry(sentence, positives);
        } else if (sentiment == Sentiment.NEG) {
            addEntry(sentence, negatives);
        }
    }

    private void addEntry(String sentence, Map<String, Integer> sentimentCounter) {
        final String[] words = splitter.split(sentence);
        for (String word : words) {
            int value = sentimentCounter.containsKey(word) ? sentimentCounter.get(word) : 0;
            sentimentCounter.put(word, value + 1);
        }
    }

    public int getConsumedNumber() {
        return positives.size() + negatives.size();
    }

    public Map<String, Integer> getPositives() {
        return new HashMap<>(positives);
    }

    public Map<String, Integer> getPositivesSorted() {
        return sortedValues(positives);
    }

    public Map<String, Integer> getNegativesSorted() {
        return sortedValues(negatives);
    }

    public Map<String, Integer> getNegatives() {
        return new HashMap<>(negatives);
    }

    private Map<String, Integer> sortedValues(Map<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

        Collections.sort(list, new ByValueComparator());

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list)
            sortedMap.put(entry.getKey(), entry.getValue());
        return sortedMap;
    }

    private class ByValueComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return -1 * o1.getValue().compareTo(o2.getValue());
        }
    }
}
