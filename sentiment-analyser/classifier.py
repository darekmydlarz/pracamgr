from textblob.classifiers import NaiveBayesClassifier

cl = NaiveBayesClassifier("train.json", format="json")

prob_dist = cl.prob_classify("This is an amazing library!")
print(prob_dist.max())
print(prob_dist.prob("pos"))
print(prob_dist.prob("neg"))
print(prob_dist.prob("test"))

print(cl.accuracy("train.json"))
print(cl.show_informative_features())