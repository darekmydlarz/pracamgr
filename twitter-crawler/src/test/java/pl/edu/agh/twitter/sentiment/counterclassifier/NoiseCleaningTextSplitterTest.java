package pl.edu.agh.twitter.sentiment.counterclassifier;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class NoiseCleaningTextSplitterTest {

    @Test
    public void case1() {
        String[] given = {
                "RT @J_SPEKZ: Haha quality! #Fellaini #United #Moyes http://t.co/rJB4K1fvZy",
                "RT @FootballFunnys: VIDEO: José Mourinho rings David Moyes after Man Utd lose to Everton! Absolutely brilliant!... http://t.co/OQb10qtJhG",
                "Arsenal >>>>",
                "RT @UtdIndonesia: Subs : Lindegaard, Anderson, Buttner, Young, Cleverley, Hernandez, Welbeck.",
                "What Match!good comeback!#mufc",
                "RT @9jaBloke: SZCZESNY!!!............yet again pulls off a top save",
                "RT @101greatgoals: BREAKING: Liverpool striker Daniel Sturridge ruled out for six to eight weeks with an ankle injury",
                "RT @FootballFunnys: David Moyes told reporters: \"The fans are behind me.\"\n" +
                        "\n" +
                        "Yes they are David.\n" +
                        "\n" +
                        "The Arsenal fans, Chelsea fans, Man City fa…",
                "My Word YOUNG!!! What a goal!!!! Man Utd 3-0 West Ham. Love it. :')",
                "Saw this coming ----»\"@Sir_Moby: Everton finally get their 1st win at old Trafford in 21years!!! Lol.. I see what you did there Moyes\""
        };

        String[] expected = {
                "Haha quality",
                "VIDEO rings lose Absolutely brilliant",
                "",
                "Subs",
                "Match good comeback",
                "pulls save",
                "BREAKING striker ruled weeks ankle injury",
                "told reporters fans fans fans fa",
                "Word goal Love",
                "coming finally 1st win 21years Lol"
        };
        compareSentences(given, expected);
    }

    @Test
    public void case2() {
        String[] given = {
                "@RickyTait_ @Fragular that's why you lost RVP though. Shit money for all the goals he scored.",
                "@afifuddinafandi no, I dont really mind about Liverpool but I just cant stand with Chelsea. I'm really hope Arsenal can beat them hmm",
                "RT @Football__Tweet: Manchester United fans chanting: \"We're shit and we're champions.\" #MUFC",
                "Weekend endin perfectly .. Waitin for chelsea/Mancity to lose or draw! Shikena #Arsenal (ManUtd,Liverpool,Tot down)",
                "RT @arsenalaction: Ozil rested today. Koscielny starts ahead of Vermaelen.",
                "but why is Bendtner looking like a garbage engineer??",
                "RT @ArseneWeTrust8: RIDICULOUS. I AM SO DONE WITH POOR REFS AGAINST ARSENAL.",
                "Cheyrou remplace Lemina",
                "Possible opponents Barca in 16 Draw : Leverkusen, Galatasaray, Olympiakos, Man City, Schalke, Arsenal, Zenit.. Draw: Monday.. #ViscaBarca",
                "U need a t at d back of dat can \"@iam_freshvibes: Arsenal, you can do it\""
        };

        String[] expected = {
                "lost shit money goals scored",
                "dont mind stand hope beat hmm",
                "fans chanting shit champions",
                "weekend endin perfectly waitin lose draw shikena tot",
                "rested today starts ahead",
                "garbage engineer",
                "ridiculous poor refs",
                "remplace",
                "opponents draw galatasaray olympiakos schalke zenit draw monday viscabarca",
                "dat"
        };
        compareSentences(given, expected);
    }

    @Test
    public void case3() {
        String[] given = {
                "RT @ahmed_hamed11: Have a feeling that lallana will score today ! #ARSSOU",
                "Back in this. Let's win this #MUFC #Rooney",
                "RT @MCFC: SUBS NEWS: Hart, Lescott, Richards, Garcia, Rodwell, Negredo, Milner #westbromvcity #mcfc",
                "RT @NatalieNat_: Ramsey!!! Almost!! #AFCvEFC",
                "RT @piersmorgan: Need Ramsey & Ozil to step up and be counted. Big games against big teams need big players to have big performances. #Afc",
                "RT @christy_okane: all these idiots believing that kyle walker is dead, take a look at who scored first for spurs today.",
                "Lallana dey sell groundnut in front of goal",
                "Come on @Arsenal Need a @_OlivierGiroud_ goal or two!!! http://t.co/HEjAcD76bl",
                "Fucking Mike Dean. You blind ass. #Arsenal",
                "David Moyes suit makes him look like a funeral director tonight"
        };

        String[] expected = {
                "feeling score today arssou",
                "win",
                "subs news westbromvcity",
                "afcvefc",
                "step counted big games big teams big players big performances",
                "idiots believing dead scored today",
                "dey sell groundnut goal",
                "goal",
                "fucking blind ass",
                "suit makes funeral director tonight"
        };
        compareSentences(given, expected);
    }

    private void compareSentences(String[] given, String[] expected) {
        NoiseCleaningTextSplitter splitter = new NoiseCleaningTextSplitter(new RegexTextSplitter("\\s"));
        for (int i = 0; i < given.length; ++i) {
            String actual = StringUtils.join(splitter.split(given[i]), " ");
            System.out.println("given:\n" + given[i]);
            System.out.println("actual:\n" + actual);
            System.out.println("expected:\n" + expected[i]);
            System.out.println();
            Assert.assertTrue(actual.equalsIgnoreCase(expected[i]));
        }
    }
}
