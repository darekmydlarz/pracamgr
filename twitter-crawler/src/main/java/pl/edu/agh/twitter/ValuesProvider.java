package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import pl.edu.agh.twitter.model.*;

import java.util.List;
import java.util.Set;

public class ValuesProvider {
    public static void main(String[] args) {
        List<MatchEvent> matchEvents = Lists.newArrayList(evertonLiverpool());
        for (MatchEvent matchEvent : matchEvents) {
            try {
                persistMatchEvent(matchEvent);
            } catch (ConstraintViolationException e) {
                continue;
            }
        }
    }

    private static void persistMatchEvent(MatchEvent matchEvent) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(matchEvent);
        transaction.commit();
        session.close();
    }

    private static Set<Player> barcelonaPlayers() {
        return Sets.newHashSet(
                new Player("Valdes", Sets.newHashSet("@1victorvaldes")),
                new Player("Pinto", Sets.newHashSet("@13_Pinto")),
                new Player("Oier"),
                new Player("Montoya", Sets.newHashSet("@2MONTOYA")),
                new Player("Pique", Sets.newHashSet("@3gerardpique")),
                new Player("Puyol", Sets.newHashSet("@Carles5puyol")),
                new Player("Bartra", Sets.newHashSet("@MarcBartra91")),
                new Player("Jordi Alba"),
                new Player("Adriano", Sets.newHashSet("@AdrianoCorreia6 ")),
                new Player("Dani Alves", Sets.newHashSet("@DaniAlvesD2")),
                new Player("Fabregas", Sets.newHashSet("cesc, @cesc4official")),
                new Player("Xavi"),
                new Player("Iniesta", Sets.newHashSet("@andresiniesta8")),
                new Player("dos Santos", Sets.newHashSet("@jona2santos")),
                new Player("Mascherano", Sets.newHashSet("@Mascherano")),
                new Player("Busquets"),
                new Player("Alex Song"),
                new Player("Sergi Roberto", Sets.newHashSet("@SergiRoberto10")),
                new Player("Pedro Rodriguez", Sets.newHashSet("@_Pedro17_")),
                new Player("Alex Sanchez"),
                new Player("Messi"),
                new Player("Neymar", Sets.newHashSet("@neymarjr")),
                new Player("Cristian Tello", Sets.newHashSet("@ctello91")),
                new Player("Cuenca", Sets.newHashSet("@CuencaIsaac"))
        );
    }

    private static Set<Player> acmilanPlayers() {
        return Sets.newHashSet(
                new Player("Amelia", Sets.newHashSet("@AmeliaGoalie")),
                new Player("Abbiati"),
                new Player("Ferdinando Coppola"),
                new Player("Gabriel Ferreira"),
                new Player("De Sciglio"),
                new Player("Mexes"),
                new Player("Zapata", Sets.newHashSet("@zapatacristian3")),
                new Player("Ignazio Abate", Sets.newHashSet("@_igna20_")),
                new Player("Kevin Constant", Sets.newHashSet("@ck21_official")),
                new Player("Bonera"),
                new Player("Matias Silvestre"),
                new Player("Vergara"),
                new Player("Iotti"),
                new Player("Zaccardo", Sets.newHashSet("@czaccardo")),
                new Player("Muntari", Sets.newHashSet("@SulleyMuntari14")),
                new Player("Saponara"),
                new Player("Birsa"),
                new Player("Poli"),
                new Player("Montolivo", Sets.newHashSet("@OfficialMonto")),
                new Player("Kaka", Sets.newHashSet("@KAKA")),
                new Player("Nocerino"),
                new Player("Cristante"),
                new Player("Emanuelson", Sets.newHashSet("@Urby28")),
                new Player("Nigel de Jong", Sets.newHashSet("@NDJ_Official")),
                new Player("Piccinocchi"),
                new Player("Mastalli"),
                new Player("Benedicic"),
                new Player("Robinho", Sets.newHashSet("@oficialrobinho")),
                new Player("Matri", Sets.newHashSet("@Ale_Matri")),
                new Player("Pazzini"),
                new Player("Balotelli", Sets.newHashSet("Super Mario", "@FinallyMario")),
                new Player("Mbaye Niang", Sets.newHashSet("@OfficialNiang")),
                new Player("Shaarawy", Sets.newHashSet("@OfficialEl92"))
        );
    }

    private static MatchEvent barcelonaMilan() {
        Competition championsLeague = new Competition("UEFA Champions League");
        Set<Player> barcelonaPlayers = barcelonaPlayers();
        Set<Player> acmilanPlayers = acmilanPlayers();
        Manager gerardoMartino = new Manager("Gerardo Martino", Sets.newHashSet("Tata Martino", "Martino"));
        Manager massimilianoAllegri = new Manager("Massimiliano Allegri", Sets.newHashSet("Allegri"));
        Team fcBarcelona = new Team(Country.SP, "FC Barcelona", gerardoMartino, barcelonaPlayers,
                "fcb", "blaugrana", "barca", "barcelona", "barza", "@FCBarcelona");
        Team acMilan = new Team(Country.IT, "AC Milan", massimilianoAllegri, acmilanPlayers,
                "@acmilan", "ac milano", "milan");

        DateTime startDate = new DateTime(2013, 11, 06, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), fcBarcelona, acMilan, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("barcamilan", "camp nou"));
        return matchEvent;
    }

    private static MatchEvent evertonLiverpool() {
        Competition premiership = new Competition("Barclays Premier League");
        Set<Player> evertonPlayers = evertonPlayers();
        Set<Player> liverpoolPlayers = liverpoolPlayers();
        Manager robertoMartinez = new Manager("roberto martinez");
        Manager brendanRodgers = new Manager("brendan rodgers");
        Team evertonFc = new Team(Country.EN, "Everton FC", robertoMartinez, evertonPlayers,
                "everton", "efc", "coyb", "Toffees");
        Team liverpoolFc = new Team(Country.EN, "Liverpool FC", brendanRodgers, liverpoolPlayers,
                "liverpool", "lfc", "reds", "kopites", "pool");

        DateTime startDate = new DateTime(2013, 11, 23, 13, 45, 00);
        return new MatchEvent(startDate.toDate(), evertonFc, liverpoolFc, premiership, "efclfc", "goodison");
    }

    private static Set<Player> evertonPlayers() {
        return Sets.newHashSet(
                new Player("Howard"),
                new Player("Jagielka"),
                new Player("Baines"),
                new Player("Distin"),
                new Player("Pienaar"),
                new Player("Barry"),
                new Player("Osman"),
                new Player("Coleman"),
                new Player("McCarthy"),
                new Player("Mirallas"),
                new Player("Lukaku"),
                new Player("Deulofeu"),
                new Player("Barkley"),
                new Player("Robles"),
                new Player("Heitinga"),
                new Player("Jelavic"),
                new Player("Naismith"),
                new Player("Stones")
        );
    }

    ;

    private static Set<Player> liverpoolPlayers() {
        return Sets.newHashSet(
                new Player("Mignolet"),
                new Player("Glen Johnson"),
                new Player("Agger"),
                new Player("Skrtel"),
                new Player("Cissokho"),
                new Player("Gerrard"),
                new Player("Lucas Leiva"),
                new Player("Coutinho"),
                new Player("Suarez"),
                new Player("Sturridge"),
                new Player("Allen"),
                new Player("Moses"),
                new Player("Bradley Jones"),
                new Player("Luis Alberto"),
                new Player("Sakho"),
                new Player("Sterling")
        );
    }

    ;


}
