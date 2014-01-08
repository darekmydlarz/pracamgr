package pl.edu.agh.twitter.sentiment;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;

import java.util.Arrays;

public class SentimentAnalyser {

    public static void main(String[] args) {


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
