package pl.edu.agh.twitter.sentiment;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SentimentAnalyser {

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
                sentiment = Sentiment.valueOf(priorPolarity.substring(0, 3).toUpperCase());
            }
        }
    }

    enum Sentiment {
        POS, NEG, NEU, BOT, WEA
    }

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


    private static void analyseTest() {
        Classifier<String, String> bayes = new BayesClassifier<>();

        String[] pos = "I love sunny days".split("\\s");
        String[] neg = "I hate rain".split("\\s");

        bayes.learn("pos", Arrays.asList(pos));
        bayes.learn("neg", Arrays.asList(neg));

        String[] unknownText1 = "today is a sunny day".split("\\s");
        String[] unknownText2 = "there will be rain".split("\\s");

        Classification<String, String> class1 = bayes.classify(Arrays.asList(unknownText1));
        Classification<String, String> class2 = bayes.classify(Arrays.asList(unknownText2));

        System.out.println(class1.getCategory());
        System.out.println(class2.getCategory());

        System.out.println(bayes.getCategories());
        System.out.println(bayes.getCategoriesTotal());
        System.out.println(bayes.getFeatures());
        System.out.println(bayes.featureCount("sunny", "neg"));
        System.out.println(bayes.featureCount("sunny", "pos"));

        System.out.println(((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(unknownText1)));
    }
}
