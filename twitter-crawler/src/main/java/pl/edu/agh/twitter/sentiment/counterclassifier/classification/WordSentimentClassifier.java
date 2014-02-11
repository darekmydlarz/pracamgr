package pl.edu.agh.twitter.sentiment.counterclassifier.classification;

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
        }
    }

    static class Classification {
        long positives = 0;
        long negatives = 0;
        final String [] text;

        public Classification(String[] text) {
            this.text = text;
        }

        public Sentiment getSentiment() {
            return positives > negatives ? Sentiment.POS : positives == negatives ? Sentiment.NEU : Sentiment.NEG;
        }

        public double getSentimentPercentage() {
            return (double) positives / (positives + negatives);
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
