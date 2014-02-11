package pl.edu.agh.twitter.sentiment.counterclassifier;

public interface TextSplitter {
    String[] split(String text);
}
