package pl.edu.agh.twitter.sentiment.counterclassifier;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.twitter.sentiment.cleaner.NegationIrrelevantRemovingCleaner;
import pl.edu.agh.twitter.sentiment.cleaner.TextCleaner;

public class NegationIrrelevantRemovingCleanerTest {
    @Test
    public void testClean() throws Exception {

        String[] given = {
                "RT @J_SPEKZ: Haha quality! #Fellaini #United #Moyes http://t.co/rJB4K1fvZy",
                "Stay woke brah! The Arsenal is about to make everything alright soon :) RT @JCphoenixx: So damn tired, So not sleepy.",
                "@abdul1haseeb My arsenal is not disappointing too :P",
                "false,its time to ARSENDAL not arsenal =)) \"@kevin_94yordan: ARSENAL TIMEEEE!!!!!!\"",
                "You up for Arsenal's match later on? — what time? maybe if i'm not busy baby sitting :) http://t.co/aC5Ec8ipy1",
                "WOI diem be leverpool tu \"@AlfiiMunandarr: false,its time to ARSENDAL not arsenal =)) \"@kevin_94yordan: ARSENAL TIMEEEE!!!!!!\"\"",
                ":) RT @TeamRamsey_Indo: Ramsey didnt celebrate against his ex club, thats what i called respect and classy player not like you-know-who...",
                "Yeah not Van Persie\"@Iam_Kale: Yea dats RESPECT !!! \"@BadtTrillz: Ehya ramsey can't even celebrate :(\"\"",
                "RvP?? :)) RT @MatiasGooner: Aaron Ramsey didn't celebrate scoring against his former club. Love him, not like the cunt. #OneAaronRamsey",
                ":)\"@MatiasGooner: Aaron Ramsey didn't celebrate scoring against his former club. Love him, not like the cunt. #OneAaronRamsey\"",
                "@Arsenal didn't think i could respect @aaronramsey any more than i already did, bute what a gentleman he is for not to celebrate that goal:)",
                "\"@prettiedove: \"@homot: We need a rude striker not a model\"\"LOOL Olivier Giroud, don't you ? ;)",
                "\"@CarmelitaNews: \"@prettiedove: \"@homot: We need a rude striker not a model\"\"LOOL Olivier Giroud, don't you ? ;)\" Lmao yes, he is a model",
                "Lol RT @CarmelitaNews: \"@prettiedove: \"@homot: We need a rude striker not a model\"\"LOOL Olivier Giroud, don't you ? ;)",
                "#hollyrowena I'm not sure, They might play there,, They did on TBP 2007 tour! :) I hope they do,, Cardiff is the closest place to me! xD",
                "At all...\"@dotun_somoye: Even city's first goal negredo was offside....:( the refs not helping at all\"",
                "LOL RT. @AIS_JKT: Navas & Cazorla don't play now because of it's still Advent, not Christmas yet :)",
                ":)RT @idcabasa_coded: Arsenal I'm not a fan but don't fall my hand! I don make mouth about una o",
                "Una don start again\"@snowflakes504: At all...\"@dotun_somoye: Even city's first goal negredo was offside....:( the refs not helping at all\"\"",
                "Lorl arsenal fans be like \"@Lindtor8: Yessssss!!!!!!! RT @kinglloyd001: Nohhhhhh not Aguero... :( #MCIvsARS\"",
                "@Arsenal Manchester City won the game -_- yes please, it will not be a constraint for the opportunity Arsenal to victory :)",
                "Lol, I'm not even watching RT @MkTebza: TF?are u sure? Pls check SS3 RT @Bucs_BaBey: Man city (0) Ars (2) :) RT @MkTebza: Score??? Mancity v",
                "@bob959 I soooo agree!It seems not to be #Arsenal# day :-( :-( :-(!",
                "BENDTNER!!! ARE YOU FUCKING SERIOUS!! Even though im not arsenal fan :o",
                "STEVEN GERRARD RT “@TooSomali: “@Capiii_: Now that's my captain” So you know :) aw, thanks.”",
                "Such a Disney buff. Just found out about the new Alice in Wonderland movie. Official trailer: http://bit.ly/131Js0 I love the Cheshire Cat.",
                "Haha, you gotta agree, no one gets booed like Manchester United :D #ZeDevilza",
                "OHHHHHH!!!!! SO CLOSE!!! Wilshere!!! Good Job Ramsey keeping that move alive",
                // "Why didn't Chelsea loan Lukaku to Arsenal?"
        };

        String[] expected = {
                "",
                "Stay woke brah make alright",
                "not_disappointing",
                "false time ARSENDAL",
                "match time not_busy not_baby not_sitting",
                "WOI diem leverpool tu",
                "",
                "yeah",
                "",
                "",
                "didnt not_respect bute gentleman not_celebrate not_goal",
                "LOOL Olivier dont",
                "Lmao model",
                "Lol",
                "play TBP tour hope closest place xd",
                "",
                "LOL",
                "",
                "Una start",
                "Lorl fans",
                "won game not_constraint not_opportunity not_victory",
                "Lol not_watching",
                "soooo agree not_day",
                "FUCKING im not_fan",
                "",
                "disney buff alice wonderland movie official trailer love cheshire cat",
                "haha gotta agree not_booed",
                "ohhhhhh close good job keeping alive",
                //""
        };
        compareSentences(given, expected);
    }

    private void compareSentences(String[] given, String[] expected) {
        TextCleaner cleaner = new NegationIrrelevantRemovingCleaner();
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
