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
    private static final DecimalFormat decimalformat = new DecimalFormat("00.0000");
    private static final DecimalFormat percentageformat = new DecimalFormat("00.00%");
    public static final int TRAIN_LENGTH = 82940;
    public static final int EMOTED_LENGTH = 103676;
    public static final int TWEETS_NUMBER = 100;
    public static final int MINIMUM_FREQUENCY = 50;
    public static final int MIN_WORD_LENGTH = 3;
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
        List<ParoubekSentence> sentences = fetchTweetsAndCreateSentences();
//        simpleGetAndPrint(sentences);
        findBestParameters(sentences);
    }

    private void findBestParameters(List<ParoubekSentence> sentences) {
        double[][] correctnesses = new double[11][101];
        for(int minWordLength = 0; minWordLength <= 10; ++minWordLength) {
            for(int minFrequenecy = 0; minFrequenecy <= 100; ++minFrequenecy) {
                final Double correctness = fetchWordsCountAvgValenceAndCorrectness(sentences, minFrequenecy, minWordLength);
                correctnesses[minWordLength][minFrequenecy] =
                        correctness;
                System.out.println("DM1/L" + minWordLength + "/F" + minFrequenecy + "\t/C == " + correctness);
            }
        }

        for(int minFrequency = 0; minFrequency <= 100; ++minFrequency) {
            for(int minWordLength = 0; minWordLength <= 10; ++minWordLength) {
                System.out.print(correctnesses[minWordLength][minFrequency] + "\t");
            }
            System.out.println();
        }
    }

    private Double fetchWordsCountAvgValenceAndCorrectness(List<ParoubekSentence> sentences, int minFrequency, int minWordLength) {
        Map<String, WordFrequency> frequencyMap = wordFrequencyDAO.fetchAll(minFrequency, minWordLength, countStrategy);
        computeValences(sentences, frequencyMap);
        final Double averageValence = computeAverageValence(frequencyMap.values());
//        printResults(paroubekSentences, averageValence);
        return countCorrectness(sentences, averageValence);
    }

    private void simpleGetAndPrint(List<ParoubekSentence> paroubekSentences) {
        fetchWordsCountAvgValenceAndCorrectness(paroubekSentences, MINIMUM_FREQUENCY, MIN_WORD_LENGTH);
    }

    private Double countCorrectness(List<ParoubekSentence> sentences, Double averageValence) {
        Integer corrects = 0;
        Integer classified = 0;
        for(ParoubekSentence sentence : sentences) {
            Sentiment expected = EmoticonClassifier.getSentimentByEmoticon(sentence.text);
            Sentiment actual = sentence.getSentiment(averageValence);
            if(actual == expected) {
                corrects += 1;
                classified += 1;
            } else if(actual != Sentiment.NEU) {
                classified += 1;
            }
        }
        return corrects * 1.0 / classified;
    }

    private Double computeAverageValence(Collection<WordFrequency> values) {
        if(values.size() == 0)
            return 0.0;
        Double sum = 0.0;
        for(WordFrequency wordFrequency : values) {
            sum += wordFrequency.getValence();
        }
        return sum / values.size();
    }

    private void printResults(Collection<ParoubekSentence> paroubekSentences, Double averageValence) {
        System.out.println("Average\tValence\tExpected\tSentim.\tSentence\tWords");
        for(ParoubekSentence paroubekSentence : paroubekSentences) {
            final String valence = Double.isNaN(paroubekSentence.valence()) ? "?" : decimalformat.format(paroubekSentence.valence());
            Sentiment expected = EmoticonClassifier.getSentimentByEmoticon(paroubekSentence.text);
            System.out.println(
                    decimalformat.format(averageValence) + "\t" +
                            valence + "\t" +
                            expected + "\t" +
                            paroubekSentence.getSentiment(averageValence) + "\t" +
                            paroubekSentence.text.replaceAll("\\n", " ") + "\t" +
                            paroubekSentence.wordValences
            );
        }
    }

    private List<ParoubekSentence> fetchTweetsAndCreateSentences() {
        // get some
//        final int offset = offset();
//        System.out.println("Dario1.0 == " + offset + " :: " + TWEETS_NUMBER + " :: " + EMOTED_LENGTH + " :: " + TRAIN_LENGTH);
//        final List<Tweet> tweets = tweetDAO.getWithEmoticons(offset, TWEETS_NUMBER);
        // get all
        final List<Tweet> tweets = tweetDAO.getWithEmoticons(TRAIN_LENGTH + 1, EMOTED_LENGTH - TRAIN_LENGTH);
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
            if(frequencyMap.containsKey(word)) {
                final WordFrequency wordFrequency = frequencyMap.get(word);
                paroubekSentence.wordValences.put(word, wordFrequency.getValence());
            }
        }
    }
}
