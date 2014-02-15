package pl.edu.agh.twitter.sentiment.counterclassifier.classification;

import com.google.common.collect.Maps;
import pl.edu.agh.twitter.business.wordfrequency.entity.WordFrequency;
import pl.edu.agh.twitter.sentiment.Sentiment;
import pl.edu.agh.twitter.sentiment.counterclassifier.TextSplitter;

import java.util.Arrays;
import java.util.Map;

public class WordSentimentClassifier {
    private final Map<String, WordFrequency> frequencyMap;
    private final TextSplitter splitter;

    public WordSentimentClassifier(Map<String, WordFrequency> frequencyMap, TextSplitter splitter) {
        this.frequencyMap = frequencyMap;
        this.splitter = splitter;
    }

    public Classification classify(String sentence) {
        Classification classification = new Classification(splitter.split(sentence));
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

        public Classification(String[] text) {
            this.text = text;
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
