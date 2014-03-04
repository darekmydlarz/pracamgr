package pl.edu.agh.twitter.sentiment.counterclassifier;

import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;

import java.text.Normalizer;

public class AlphabetSavingCleaner implements TextCleaner {
    public static final String NON_APLHABET = "[^\\w\\s]+";
    public static final String NUMERIC = "\\b\\d+\\b";
    public static final String POSSESIVES = "('s|_)";
    public static final String NEGATIONS = "n't";

    @Override
    public Sentence clean(String text) {
        Sentence sentence = new Sentence();
        text = text.replaceAll(POSSESIVES, "");
        text = text.replaceAll(NEGATIONS, "nt");
        text = normalizeAlphabet(text);
        text = removeWhitespaces(text);
        sentence.text = text;
        return sentence;
    }

    public static String removeWhitespaces(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    private String normalizeAlphabet(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll(NON_APLHABET, " ")
                .replaceAll(NUMERIC, " ")
                .trim();
    }

    @Override
    public CountStrategy getCountStrategy() {
        return CountStrategy.TOKENIZER;
    }
}
