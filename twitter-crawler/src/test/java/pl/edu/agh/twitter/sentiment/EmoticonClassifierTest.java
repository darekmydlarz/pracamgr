package pl.edu.agh.twitter.sentiment;

import org.junit.Assert;
import org.junit.Test;

public class EmoticonClassifierTest {

    class TestCase {
        String text;
        Sentiment expected;

        TestCase(String text, Sentiment expected) {
            this.text = text;
            this.expected = expected;
        }
    }

    TestCase[] testCases = new TestCase[] {
        new TestCase("LOL Mirallas is on a Mission Kicking Racism out of Football and Phil Dowd helping him out ..XD", Sentiment.POS),
        new TestCase("waiting for @LFC match :)", Sentiment.POS)
    };

    @Test
    public void testGetSentimentByEmoticon() throws Exception {
        for(TestCase testCase : testCases) {
            Sentiment actual = EmoticonClassifier.getSentimentByEmoticon(testCase.text);
            Assert.assertEquals(testCase.expected, actual);
        }
    }
}
