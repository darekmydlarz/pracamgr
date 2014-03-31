package pl.edu.agh.twitter.sentiment.counterclassifier;

import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;

import java.util.Set;

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
