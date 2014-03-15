package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;
import pl.edu.agh.twitter.business.wordfrequency.boundary.WordFrequencyDAO;
import pl.edu.agh.twitter.business.wordfrequency.entity.WordFrequency;
import pl.edu.agh.twitter.sentiment.counterclassifier.IrrelevantRemovingCleaner;
import pl.edu.agh.twitter.sentiment.counterclassifier.TextCleaner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.*;

@Singleton
public class ParoubekClassifier implements Startable {
    private static final DecimalFormat df = new DecimalFormat("00.00");
    public static final int TRAIN_LENGTH = 82940;
    public static final int EMOTED_LENGTH = 103676;
    public static final int TWEETS_NUMBER = 100;
    public static final int MINIMUM_FREQUENCY = 50;
    public static final int WORD_LENGTH = 3;
    private final TextCleaner irrelevantRemover = new IrrelevantRemovingCleaner();
    private final CountStrategy countStrategy = irrelevantRemover.getCountStrategy();

    static class ParoubekSentence {
        final String text;
        Map<String, Double> wordValences = Maps.newHashMap();

        ParoubekSentence(String text) {
            this.text = text;
        }

        Sentiment getSentiment(Double valenceAverage) {
            final Double sentenceValence = valence();
            if(Double.isNaN(sentenceValence))
                return Sentiment.NEU;
            if(sentenceValence > valenceAverage)
                return Sentiment.POS;
            return Sentiment.NEG;
        }

        Double valence() {
            return wordValences.isEmpty() ? Double.NaN : countValence();
        }

        private Double countValence() {
            Double valencesSum = 0.0;
            for(Double valence : wordValences.values())
                valencesSum += valence;
            return valencesSum / wordValences.size();
        }
    }

    @Inject
    TweetDAO tweetDAO;

    @Inject
    WordFrequencyDAO wordFrequencyDAO;

    @Override
    public void start() {
        List<ParoubekSentence> paroubekSentences = fetchTweetsAndCreateSentences();
//        evaluateClassifier(paroubekSentences);
        simpleGetAndPrint(paroubekSentences);
    }

    private void simpleGetAndPrint(List<ParoubekSentence> paroubekSentences) {
        Map<String, WordFrequency> frequencyMap = wordFrequencyDAO.fetchAll(MINIMUM_FREQUENCY, WORD_LENGTH, countStrategy);
        computeValences(paroubekSentences, frequencyMap);
        printResults(paroubekSentences);
    }

//    private void evaluateClassifier(List<ParoubekSentence> paroubekSentences) {
//        int minLengthRightBoundary = 3,       // 0..3
//            minFrequencyRightBoundary = 10;  // 0..100
//        double [][] accuracy = new double[minLengthRightBoundary + 1][minFrequencyRightBoundary + 1];
//        for(int i = 0; i <= minLengthRightBoundary; ++i) {
//            for(int j = 0; j <= minFrequencyRightBoundary; ++j) {
//                accuracy[i][j] = countAccuracy(i, j, paroubekSentences);
//            }
//        }
//        System.out.println(Arrays.deepToString(accuracy));
//    }
//
//    private double countAccuracy(int wordLength, int frequency, List<ParoubekSentence> paroubekSentences) {
//        Map<String, WordFrequency> frequencyMap = wordFrequencyDAO.fetchAll(frequency, wordLength, countStrategy);
//        computeValences(paroubekSentences, frequencyMap);
//        final Double valenceAverage = valenceAverage(paroubekSentences);
//        int classifiedSentences = 0, correctClassifications = 0;
//        for(ParoubekSentence paroubekSentence : paroubekSentences) {
//            final Sentiment sentenceSentiment = paroubekSentence.getSentiment(valenceAverage);
//            if(sentenceSentiment != Sentiment.NEU) {
//                TextCleaner.Sentence cleanedSentence = irrelevantRemover.clean(paroubekSentence.text);
//                classifiedSentences++;
//                if(cleanedSentence.getSentiment() == sentenceSentiment) {
//                    correctClassifications++;
//                }
//            }
//        }
//        System.out.println("DM1;wordLength=" + wordLength + ";wordFreq=" + frequency + ";frequencyMapSize=" +
//                frequencyMap.size() + ";correct=" + correctClassifications + ";all=" +classifiedSentences);
//        return correctClassifications * 100.0 / classifiedSentences;
//    }

    private void printResults(Collection<ParoubekSentence> paroubekSentences) {
        final Double valenceAverage = valenceAverage(paroubekSentences);
        System.out.println("Average\tValence\tExpected\tSentim.\tSentence\tWords");
        for(ParoubekSentence paroubekSentence : paroubekSentences) {
            final String valence = Double.isNaN(paroubekSentence.valence()) ? "?" : df.format(paroubekSentence.valence());
            Sentiment expected = EmoticonClassifier.getSentimentByEmoticon(paroubekSentence.text);
            System.out.println(
                    df.format(valenceAverage) + "\t"+
                    valence + "\t" +
                    expected + "\t" +
                    paroubekSentence.getSentiment(valenceAverage) + "\t" +
                    paroubekSentence.text.replaceAll("\\n", " ") + "\t" +
                    paroubekSentence.wordValences
            );
        }
    }

    private List<ParoubekSentence> fetchTweetsAndCreateSentences() {
        final int offset = offset();
        System.out.println("Dario1.0 == " + offset + " :: " + TWEETS_NUMBER + " :: " + EMOTED_LENGTH + " :: " + TRAIN_LENGTH);
        final List<Tweet> tweets = tweetDAO.getWithEmoticons(offset, TWEETS_NUMBER);
        List<ParoubekSentence> paroubekSentences = Lists.newArrayList();
        for(Tweet t : tweets)
            paroubekSentences.add(new ParoubekSentence(t.getText()));
        return paroubekSentences;
    }

    private static int offset() {
        final int begginingIndex = new Random().nextInt(EMOTED_LENGTH - TRAIN_LENGTH - TWEETS_NUMBER);
        final int testOffset = TRAIN_LENGTH + 1;
        return testOffset + begginingIndex;
    }

    private void computeValences(Collection<ParoubekSentence> paroubekSentences, Map<String, WordFrequency> frequencyMap) {
        for(ParoubekSentence paroubekSentence : paroubekSentences) {
            computeSentenceValence(paroubekSentence, frequencyMap);
        }
    }

    private void computeSentenceValence(ParoubekSentence paroubekSentence, Map<String, WordFrequency> frequencyMap) {
        for(String word : paroubekSentence.text.split("\\s")) {
            if(frequencyMap.containsKey(word))
                paroubekSentence.wordValences.put(word, computeWordValence(word, frequencyMap));
        }
    }

    private double computeWordValence(String word, Map<String, WordFrequency> frequencyMap) {
        if(!frequencyMap.containsKey(word))
            throw new IllegalStateException("FrequencyMap does not contain word: " + word);
        final WordFrequency wordFrequency = frequencyMap.get(word);
        return Math.log10(wordFrequency.getPositive() + 1.0 / wordFrequency.getNegative() + 1.0);
    }

    private Double valenceAverage(Collection<ParoubekSentence> paroubekSentences) {
        Double valencesSum = 0.0;
        for(ParoubekSentence paroubekSentence : paroubekSentences) {
            final Double sentenceValence = paroubekSentence.valence();
            if(!Double.isNaN(sentenceValence))
                valencesSum += sentenceValence;
        }
        return valencesSum / paroubekSentences.size();
    }
}
