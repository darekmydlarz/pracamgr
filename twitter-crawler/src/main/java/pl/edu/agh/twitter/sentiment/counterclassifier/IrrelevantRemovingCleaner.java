package pl.edu.agh.twitter.sentiment.counterclassifier;

import com.google.common.collect.Lists;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.IStemmer;
import edu.mit.jwi.morph.WordnetStemmer;
import org.apache.commons.lang.StringUtils;
import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrrelevantRemovingCleaner implements TextCleaner {
    static enum Regex {
        CitationOnStart("(\"|“)@.+\\: .+(\"|”)(.*)", 3),
        CitationOnEnd("(.*)(\"|“|”)@.+\\: .+", 1),
        CommentRetweet("(.*)rt(\\.)? (\"|“|”)?@.+\\: .*", 1);

        private final String regex;
        private final int group;

        Regex(String regex, int group) {
            this.regex = regex;
            this.group = group;
        }
    }
    private TextCleaner alphabetSavingCleaner = new AlphabetSavingCleaner();
    private TextCleaner stopListCleaner = new StopListCleaner();
    private TextCleaner negationDetectingCleaner = new NegationDetectingCleaner();

    @Override
    public Sentence clean(String text) {
        Sentence sentence = new Sentence();
        text = text.toLowerCase();
        final String[] splitted = cleanAndSplit(sentence, text);
        sentence.text = StringUtils.join(splitted, " ");
        return sentence;
    }

    public String[] cleanAndSplit(Sentence sentence, String text) {
        text = applyRemoves(text);
        sentence.sentiment = EmoticonClassifier.getSentimentByEmoticon(text);
        text = negationDetectingCleaner.clean(text).text;
        text = alphabetSavingCleaner.clean(text).text;
        text = stopListCleaner.clean(text).text;
        return removeWhitespaces(text).split("\\s+");
    }

    private String stemWord(IStemmer stemmer, String word) {
        if(StringUtils.isBlank(word.trim()))
            return word;
        for(POS pos : Arrays.asList(POS.NOUN, POS.ADJECTIVE)) {
            final List<String> stems = stemmer.findStems(word, pos);
            if(!stems.isEmpty())
                return stems.get(0);
        }
        return word;
    }

    private String stemSentenceWithWordnet(final String text) {
        IDictionary dict = null;
        try {
            dict = new Dictionary(getClass().getClassLoader().getResource("dict"));
            dict.open();
            IStemmer stemmer = new WordnetStemmer(dict);
            return stemSentence(text, stemmer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if(dict != null && dict.isOpen()) {
                dict.close();
            }
        }
    }

    private String stemSentence(String text, IStemmer stemmer) {
        final String[] splitted = text.split("\\s+");
        List<String> result = Lists.newArrayList();
        for(String word : splitted) {
            result.add(stemWord(stemmer, word));
        }
        return StringUtils.join(result, " ");
    }

    @Override
    public CountStrategy getCountStrategy() {
        return CountStrategy.NEG_LEARN_CLEANER;
    }

    private String applyRemoves(String text) {
        for(Regex regex : Regex.values()) {
            text = getRelevantText(text, regex);
        }
        return text;
    }

    private String getRelevantText(String text, Regex regex) {
        Matcher matcher = Pattern.compile(regex.regex).matcher(text);
        while(matcher.matches()) {
            text = matcher.group(regex.group);
            matcher = Pattern.compile(regex.regex).matcher(text);
        }
        return text;
    }

    public static String removeWhitespaces(String text) {
        return text.replaceAll("\\n+", " ").replaceAll("\\s+", " ").trim();
    }
}
