package pl.edu.agh.twitter.sentiment.counterclassifier;

import org.junit.Assert;
import org.junit.Test;

public class NegationDetectingCleanerTest {
    @Test
    public void testClean() throws Exception {

        String[] given = {
                "I don't like him. He is not a good person",
                "@Darah_Glenn pizza for breakfast, trying your best to live the student life at all costs lol",
                "@EmilyJayneBibb cause I went there for work experience and coudnt stand being there! Plus I wanna save dollars for skiing",
                "I'm so broke, I can't even afford to pay attention...",
                "couldn't think of anything worse than going out this weekend #chilling #cute",
                "@kevdev9 absolutely; he's done more tactically in 3months than Moyes did in 11 years and isn't afraid to make changes#notafter80minutes!",
                "@chloeemaeeregan @PR1CKST1CK you didn't warn me that, that wasn't suitable to open in public. http://t.co/NUGkMS5C1n",
                "I've been trying to explain this for the past half an hour I just don't feel like we're getting anywhere @sholabell http://t.co/gG04W60ZU2",
                "@Obi_LE @Requiem_LE @CLARKEY_LE he hasn't even been recruited. Nobody is New\" unless they are recruited officially. Which he has not been\"",
                "\"#c4news RT\n" +
                        "@theharryshearer Busting an important~myth:butter,cheese&red meat don't increase heart~attack risk http://t.co/ezC1FseFUC …\n" +
                        "x\""
        };

        String[] expected = {
                "I don't NOT_like NOT_him He is not NOT_a NOT_good NOT_person",
                "pizza for breakfast trying your best to live the student life at all costs lol",
                "cause I went there for work experience and coudnt stand being there Plus I wanna save dollars for skiing",
                "I'm so broke I can't NOT_even NOT_afford NOT_to NOT_pay NOT_attention",
                "couldn't NOT_think NOT_of NOT_anything NOT_worse NOT_than NOT_going NOT_out NOT_this NOT_weekend",
                "absolutely he's done more tactically in 3months than Moyes did in 11 years and isn't NOT_afraid NOT_to NOT_make NOT_changes#notafter80minutes",
                "you didn't NOT_warn NOT_me NOT_that that wasn't NOT_suitable NOT_to NOT_open NOT_in NOT_public",
                "I've been trying to explain this for the past half an hour I just don't NOT_feel NOT_like NOT_we're NOT_getting NOT_anywhere",
                "he hasn't NOT_even NOT_been NOT_recruited Nobody NOT_is NOT_New\" NOT_unless NOT_they NOT_are NOT_recruited NOT_officially Which he has not NOT_been\"",
                "\"#c4news rt busting an important~myth butter cheese&red meat don't NOT_increase NOT_heart~attack NOT_risk NOT_… NOT_x\""
        };
        compareSentences(given, expected);
    }

    private void compareSentences(String[] given, String[] expected) {
        TextCleaner cleaner = new NegationDetectingCleaner();
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
