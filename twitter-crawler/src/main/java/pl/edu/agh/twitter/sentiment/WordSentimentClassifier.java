package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Maps;
import pl.edu.agh.twitter.entities.wordfrequency.WordFrequency;
import pl.edu.agh.twitter.sentiment.cleaner.TextCleaner;

import java.util.Arrays;
import java.util.Map;

/**
 * This class is classyfing given sentence as being positive or negative due to given word-frequency map, and TextCleaner
 * implementation. It gets word-freqency from database, which was already computed.
 */
public class WordSentimentClassifier {
    private final Map<String, WordFrequency> frequencyMap;
    private final TextCleaner cleaner;

    public WordSentimentClassifier(Map<String, WordFrequency> frequencyMap, TextCleaner cleaner) {
        this.frequencyMap = frequencyMap;
        this.cleaner = cleaner;
    }

    public Classification classify(String sentence) {
        Classification classification = new Classification(cleaner.clean(sentence).getText());
        for(String word : classification.text)
            addValuesIfWordFound(classification, word);
        return classification;
    }

    private void addValuesIfWordFound(Classification classification, String word) {
        if(frequencyMap.containsKey(word)) {
            classification.positives += frequencyMap.get(word).getPositive();
            classification.negatives += frequencyMap.get(word).getNegative();
            classification.putWord(frequencyMap.get(word));
        }
    }

    static class Classification {
        long positives = 0;
        long negatives = 0;
        final String [] text;
        private Map<String, WordFrequency> detailsMap = Maps.newHashMap();

        public Classification(String text) {
            this.text = text.split("\\s");
        }

        private void putWord(WordFrequency wordFrequency) {
            detailsMap.put(wordFrequency.getWord(), wordFrequency);
        }

        public Sentiment getSentiment() {
            return positives > negatives ? Sentiment.POS : positives == negatives ? Sentiment.NEU : Sentiment.NEG;
        }

        public Map<String, WordFrequency> detailsMap() {
            return detailsMap;
        }

        public double getSentimentPercentage() {
            switch(getSentiment()) {
                case POS:
                    return (double) positives / (positives + negatives);
                case NEG:
                    return (double) negatives / (positives + negatives);
                default:
                    return 0d;
            }
        }

        @Override
        public String toString() {
            return "Classification{" +
                    "positives=" + positives +
                    ", negatives=" + negatives +
                    ", text=" + Arrays.toString(text) +
                    '}';
        }
    }
}
