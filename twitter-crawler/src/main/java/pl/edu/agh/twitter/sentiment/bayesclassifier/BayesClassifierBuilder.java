package pl.edu.agh.twitter.sentiment.bayesclassifier;

import de.daslaboratorium.machinelearning.classifier.Classifier;

public interface BayesClassifierBuilder {
    Classifier<String, String> sentimentClassifier(int capacity);

    String name();
}
