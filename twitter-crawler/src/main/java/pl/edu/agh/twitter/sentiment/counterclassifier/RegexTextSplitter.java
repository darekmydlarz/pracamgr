package pl.edu.agh.twitter.sentiment.counterclassifier;

public class RegexTextSplitter implements TextSplitter {
    private final String regex;

    public RegexTextSplitter(String regex) {
        this.regex = regex;
    }

    @Override
    public String[] split(String text) {
        return text.split(regex);
    }
}
