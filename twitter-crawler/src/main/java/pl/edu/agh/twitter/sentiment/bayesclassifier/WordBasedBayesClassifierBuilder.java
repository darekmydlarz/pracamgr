package pl.edu.agh.twitter.sentiment.bayesclassifier;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import org.apache.commons.lang.StringUtils;
import pl.edu.agh.twitter.sentiment.Sentiment;
import pl.edu.agh.twitter.sentiment.bayesclassifier.BayesClassifierBuilder;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class WordBasedBayesClassifierBuilder implements BayesClassifierBuilder {

    class ClassifiedWords {
        private List<String> positives = Lists.newArrayList();
        private List<String> negatives = Lists.newArrayList();
    }

    class MPQAParsedLine {
        private String word;
        private Sentiment sentiment;

        MPQAParsedLine(String line) {
            final List<String> splitted = Arrays.asList(line.split("\\s"));
            for (String pair : splitted) {
                findEntities(pair.split("="));
            }
        }

        private void findEntities(String[] splittedPair) {
            if ("word1".equals(splittedPair[0])) {
                word = splittedPair[1];
            } else if ("priorpolarity".equals(splittedPair[0])) {
                String priorPolarity = splittedPair[1];
                sentiment = Sentiment.getInstanceByFullName(priorPolarity);
            }
        }
    }

    @Override
    public Classifier<String, String> sentimentClassifier(int capacity) {
        Classifier<String, String> bayes = new BayesClassifier<>();
        bayes.setMemoryCapacity(capacity);
        Set<ClassifiedWords> classifiedWordsSet = Sets.newHashSet(
                readBingLiu(), readMpqa()
        );
        for (ClassifiedWords classifiedWords : classifiedWordsSet) {
            bayes.learn(Sentiment.POS.name(), classifiedWords.positives);
            bayes.learn(Sentiment.NEG.name(), classifiedWords.negatives);
        }
        return bayes;
    }

    @Override
    public String name() {
        return "WordBased";
    }

    private ClassifiedWords readMpqa() {
        ClassifiedWords classifiedWords = new ClassifiedWords();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("mpqa-subjectivy-lexicon" + File.separator + "subjclueslen1-HLTEMNLP05.tff");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                MPQAParsedLine mpqaLine = new MPQAParsedLine(line);
                switch (mpqaLine.sentiment) {
                    case POS:
                        classifiedWords.positives.add(mpqaLine.word);
                        break;
                    case NEG:
                        classifiedWords.negatives.add(mpqaLine.word);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classifiedWords;
    }

    private ClassifiedWords readBingLiu() {
        ClassifiedWords classifiedWords = new ClassifiedWords();
        classifiedWords.positives = readBingLiuFile("positive-words.txt");
        classifiedWords.negatives = readBingLiuFile("negative-words.txt");
        return classifiedWords;
    }

    private List<String> readBingLiuFile(String fileName) {
        List<String> words = Lists.newArrayList();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("bing-liu-opinion-lexicon" + File.separator + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(";") && StringUtils.isNotBlank(line))
                    words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
