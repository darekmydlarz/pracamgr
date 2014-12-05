package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Maps;
import pl.edu.agh.twitter.sentiment.Sentiment;
import pl.edu.agh.twitter.sentiment.cleaner.TextCleaner;

import java.util.*;

/**
 * Helper class to count and store words with number of being used in positive or negative context.
 */
public class WordSentimentCounter {
    private Map<String, Integer> positives = Maps.newHashMap();
    private Map<String, Integer> negatives = Maps.newHashMap();
    private final TextCleaner cleaner;

    public WordSentimentCounter(TextCleaner cleaner) {
        this.cleaner = cleaner;
    }

    public void consume(String text) {
        TextCleaner.Sentence sentence = cleaner.clean(text);
        System.out.println(sentence.getSentiment() + " ### " + sentence.getText());
        if(sentence.getSentiment() == Sentiment.POS)
            addEntryToMap(text, positives);
        else if(sentence.getSentiment() == Sentiment.NEG)
            addEntryToMap(text, negatives);
    }

    private void addEntryToMap(String sentence, Map<String, Integer> sentimentCounter) {
        final String[] words = cleaner.clean(sentence).getText().split("\\s");
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
