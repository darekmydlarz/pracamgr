package pl.edu.agh.twitter.sentiment.cleaner;

import org.apache.commons.lang.StringUtils;
import pl.edu.agh.twitter.entities.wordfrequency.CountStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Text Cleaner which is detecting negations in tweets and handles them by append NOT_ to first punctuation mark found.
 */
public class NegationDetectingCleaner implements TextCleaner {
    public static final String[] NEG_WORDS = new String[] {
            "don'?t", "doesn'?t", "didn'?t",
            "haven'?t", "hasn'?t", "hadn'?t",
            "cannot", "can'?t", "couldn'?t",
            "mightn'?t", "shan'?t", "shouldn'?t",
            "won'?t", "wouldn'?t", "daren'?t",
            "mustn'?t", "needn'?t", "oughtn'?t",
            "aren'?t", "isn'?t", "wasn'?t",
            "weren'?t", "ain'?t", "no", "not", "never",
            "nobody", "nothing", "none", "neither", "nor",
            "nowhere"
    };
    private static final String PUNCTUATION = "[,.;:!?-]";
    private static final String NEGATIONS = ".*\\b(" + StringUtils.join(NEG_WORDS, "|") + ")\\b(.*)";
    public static final String P_SPACE = "\\p{Space}+";
    public static final String NOT = "NOT_";

    @Override
    public Sentence clean(String text) {
        Sentence sentence = new Sentence();
        text = text.replaceAll("\\n", " ").trim();
        text = removeUrls(text);
        text = removeUsersMentions(text);
        text = removeHashtags(text);
        text = text.replaceAll("\\s+", " ");
        text = performNegations(text.toLowerCase().split(PUNCTUATION));
        sentence.text = text.replaceAll("\\s+", " ");
        return sentence;
    }

    private String performNegations(String[] sentences) {
        StringBuilder output = new StringBuilder();
        for(String text : sentences) {
            final String negated = prependNotsIfFoundNegation(text.trim()).trim();
            if(StringUtils.isNotBlank(negated))
                output.append(negated);
            else
                output.append(text.trim());
            output.append(" ");
        }
        return output.toString().trim();
    }

    private String prependNotsIfFoundNegation(String text) {
        StringBuilder output = new StringBuilder();
        Matcher matcher = Pattern.compile(NEGATIONS).matcher(text);
        if(matcher.matches()) {
            final String withNots = prependNots(text, matcher);
            output.append(withNots).append(" ");
        }
        return output.toString().trim();
    }

    private String prependNots(String text, Matcher matcher) {
        StringBuilder result = new StringBuilder(text.substring(0, matcher.start(2)));
        final String group = matcher.group(2);
        final String[] toNegate = group.trim().split(P_SPACE);
        for(String word : toNegate) {
            word = word.trim();
            result.append(" ");
            if(!word.startsWith("#") && !word.startsWith("@") && !word.startsWith("http")) {
                result.append(NOT);
            }
            result.append(word);
        }
        return result.toString().trim();
    }

    private String removeUrls(String text) {
        final String urls = "(\\b|^)http(s)?\\://[^\\s]+(\\b|$)";
        text = text.replaceAll(urls, " ");
        return text.trim();
    }

    private String removeUsersMentions(String text) {
        final String mentions = "(^|\\s)(@\\w+)";
        text = text.replaceAll(mentions, " ");
        return text.trim();
    }

    private String removeHashtags(String text) {
        final String hashtags = "(^|\\s)(#\\w+)";
        text = text.replaceAll(hashtags, " ");
        return text.trim();
    }

    @Override
    public CountStrategy getCountStrategy() {
        return CountStrategy.NEGATION_DETECTOR;
    }
}
