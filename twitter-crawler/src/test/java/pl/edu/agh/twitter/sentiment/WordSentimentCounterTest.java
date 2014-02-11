package pl.edu.agh.twitter.sentiment;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.twitter.sentiment.counterclassifier.databuild.WordSentimentCounter;

import java.util.Map;

public class WordSentimentCounterTest {

    @Test
    public void consume() {
        WordSentimentCounter counter = new WordSentimentCounter();
        String sentence = "I really like you boy! ;)";

        counter.consume(sentence);

        Assert.assertEquals(6, counter.getConsumedNumber());
    }

    @Test
    public void getPositives() {
        WordSentimentCounter counter = new WordSentimentCounter();
        String sentence = "I really like you boy! ;)";
        String sentence2 = "I dont know what are you talking about ;)";

        counter.consume(sentence);
        counter.consume(sentence2);
        final Map<String,Integer> positives = counter.getPositivesSorted();
        System.out.println(positives);

        Assert.assertEquals(12, counter.getConsumedNumber());
    }
}
