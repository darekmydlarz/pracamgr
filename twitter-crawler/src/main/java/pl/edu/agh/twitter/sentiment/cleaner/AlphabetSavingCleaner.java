package pl.edu.agh.twitter.sentiment.cleaner;

import pl.edu.agh.twitter.entities.CountStrategy;

import java.text.Normalizer;

/**
 * Text cleaner which saves after all only alphabet signs. It removes all non alphabet symbols from text.
 */
public class AlphabetSavingCleaner implements TextCleaner {
    public static final String NON_APLHABET = "[^\\w\\s_]+";
    public static final String SINGLE_UNDERSCORE = "\\b_\\b";
    public static final String NUMERIC = "\\b\\d+\\b";
    public static final String POSSESIVES = "'s";
    public static final String NEGATIONS = "n't";

    @Override
    public Sentence clean(String text) {
        Sentence sentence = new Sentence();
        text = text.replaceAll(POSSESIVES, "");
        text = text.replaceAll(NEGATIONS, "nt");
        text = text.replaceAll(SINGLE_UNDERSCORE, "");
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
