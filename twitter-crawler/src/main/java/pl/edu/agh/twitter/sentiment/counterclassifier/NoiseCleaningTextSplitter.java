package pl.edu.agh.twitter.sentiment.counterclassifier;

import java.text.Normalizer;
import java.util.Set;

public class NoiseCleaningTextSplitter implements TextSplitter {
    private static final String RETWEET = "^RT @[\\w]+: ";
    private static final String URLS = "(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*";
    private static final String NON_ALPHANUMERIC = "[^\\w]+";
    private static final String MULTIPLE_WHITESPACES = "\\s{2,}";
    private static final String NUMERIC = "\\b\\d+\\b";
    private static final String MENTIONS = "@\\w+";

    private final TextSplitter textSplitter;

    public NoiseCleaningTextSplitter(TextSplitter textSplitter) {
        this.textSplitter = textSplitter;
    }

    @Override
    public String[] split(String text) {
        final String cleaned = cleanNoise(text);
        final String stopListed = performStopListCleaning(cleaned);
        return textSplitter.split(stopListed);
    }

    private String performStopListCleaning(String text) {
        final Set<String> words = StopList.ALL.getWords();
        text = text.toLowerCase();
        for(String word : words) {
            text = text.replaceAll("\\b" + word.toLowerCase() + "\\b", "");
        }
        return text.trim().replaceAll(MULTIPLE_WHITESPACES, " ");
    }

    private String cleanNoise(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll(RETWEET, " ")
                .replaceAll(MENTIONS, " ")
                .replaceAll(URLS, " ")
                .replaceAll(NON_ALPHANUMERIC, " ")
                .replaceAll(NUMERIC, " ")
                .trim();
    }
}
