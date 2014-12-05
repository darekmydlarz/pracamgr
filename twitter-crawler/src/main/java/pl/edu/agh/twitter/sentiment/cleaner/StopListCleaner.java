package pl.edu.agh.twitter.sentiment.cleaner;

import pl.edu.agh.twitter.entities.wordfrequency.CountStrategy;

import java.util.Set;

/**
 * Cleaner which removes words found in StopList.
 */
public class StopListCleaner implements TextCleaner {

    @Override
    public Sentence clean(String text) {
        Sentence sentence = new Sentence();
        sentence.text = performStopListCleaning(sentence, text);
        return sentence;
    }

    private String performStopListCleaning(Sentence sentence, String text) {
        final Set<String> words = StopList.ALL.getWords();
        text = text.toLowerCase();
        for(String word : words) {
            text = text.replaceAll("\\b" + word.toLowerCase() + "\\b", "");
            text = text.replaceAll("\\bnot_" + word.toLowerCase() + "\\b", "");
        }
        return text.trim().replaceAll("\\n+", " ");
    }

    @Override
    public CountStrategy getCountStrategy() {
        return CountStrategy.STOP_LIST;
    }
}
