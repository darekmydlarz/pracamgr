package pl.edu.agh.twitter.sentiment.counterclassifier;

import org.junit.Assert;
import org.junit.Test;

public class AlphabetSavingCleanerTest {
    @Test
    public void testClean() throws Exception {
        String[] given = {
                "Stay woke brah! The Arsenal is about to make everything alright soon :)",
                "@abdul1haseeb My arsenal is not disappointing too :P",
                "false,its time to ARSENDAL not arsenal =))",
                "You up for Arsenal's match later on? — what time? maybe if i'm not busy baby sitting :)",
                "WOI diem be leverpool tu",
                ":)",
                "Yeah not Van Persie",
                "RvP?? :))",
                ":)",
                "@Arsenal didn't think i could respect @aaronramsey any more than i already did, bute what a gentleman he is for not to celebrate that goal:)",
                "LOOL Olivier Giroud, don't you ? ;)",
                "Lmao yes, he is a model",
                "Lol",
                "#hollyrowena I'm not sure, They might play there,, They did on TBP 2007 tour! :) I hope they do,, Cardiff is the closest place to me! xD",
                "At all...",
                "LOL",
                ":)",
                "Una don start again",
                "Lorl arsenal fans be like",
                "@Arsenal Manchester City won the game -_- yes please, it will not be a constraint for the opportunity Arsenal to victory :)",
                "Lol, I'm not even watching",
                "@bob959 I soooo agree!It seems not to be #Arsenal# day :-( :-( :-(!",
                "BENDTNER!!! ARE YOU FUCKING SERIOUS!! Even though im not arsenal fan :o",
                "Today @ 1:45 am big match it's all about football'"
        };

        String[] expected = {
                "Stay woke brah The Arsenal is about to make everything alright soon",
                "abdul1haseeb My arsenal is not disappointing too P",
                "false its time to ARSENDAL not arsenal",
                "You up for Arsenals match later on what time maybe if im not busy baby sitting",
                "WOI diem be leverpool tu",
                "",
                "Yeah not Van Persie",
                "RvP",
                "",
                "Arsenal didnt think i could respect aaronramsey any more than i already did bute what a gentleman he is for not to celebrate that goal",
                "LOOL Olivier Giroud dont you",
                "Lmao yes he is a model",
                "Lol",
                "hollyrowena Im not sure They might play there They did on TBP tour I hope they do Cardiff is the closest place to me xD",
                "At all",
                "LOL",
                "",
                "Una don start again",
                "Lorl arsenal fans be like",
                "Arsenal Manchester City won the game yes please it will not be a constraint for the opportunity Arsenal to victory",
                "Lol Im not even watching",
                "bob959 I soooo agree It seems not to be Arsenal day",
                "BENDTNER ARE YOU FUCKING SERIOUS Even though im not arsenal fan o",
                "Today am big match its all about football"
        };
        compareSentences(given, expected);

    }

    private void compareSentences(String[] given, String[] expected) {
        TextCleaner cleaner = new AlphabetSavingCleaner();
        for (int i = 0; i < given.length; ++i) {
            String actual = cleaner.clean(given[i]).text;
            System.out.println("given:\n" + given[i]);
            System.out.println("actual:\n" + actual);
            System.out.println("expected:\n" + expected[i]);
            System.out.println();
            Assert.assertTrue(actual.equalsIgnoreCase(expected[i]));
        }
    }
}
