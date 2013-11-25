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
    private static TwitterDAO twitterDAO = new TwitterDAO();


    public static void main(String[] args) {
        List<MatchEvent> matchEvents = Lists.newArrayList(baselChelsea());
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
        Competition championsLeague = twitterDAO.createOrGetCompetition("UEFA Champions League");
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
        Competition premiership = twitterDAO.createOrGetCompetition("Barclays Premier League");
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

    private static MatchEvent arsenalOlympique() {
        Competition championsLeague = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team arsenal = twitterDAO.createOrGetTeam("arsenal");
        if (arsenal.getId() == null) {
            Set<Player> arsenalPlayers = arsenalPlayers();
            Manager arseneWenger = new Manager("Wenger");
            arsenal = new Team(Country.EN, "Arsenal Londyn", arseneWenger, arsenalPlayers, "arsenal", "afc", "gunners");
        }
        Team olympique = twitterDAO.createOrGetTeam("marseille");
        if (olympique.getId() == null) {
            Set<Player> olympiquePlayers = olympiquePlayers();
            Manager elieBaup = new Manager("Elie Baup");
            olympique = new Team(Country.FR, "OLYMPIQUE MARSEILLE", elieBaup, olympiquePlayers, "marseille");

        }
        DateTime startDate = new DateTime(2013, 11, 26, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, olympique, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("emirates", "antonio mateu"));
        return matchEvent;
    }

    private static Set<Player> olympiquePlayers() {
        return Sets.newHashSet(
                new Player("mandanda"),
                new Player("morel"),
                new Player("diawara"),
                new Player("nkoulou"),
                new Player("abdallah"),
                new Player("cheyrou"),
                new Player("valbuena"),
                new Player("romao"),
                new Player("thauvin"),
                new Player("ayew"),
                new Player("payet"),
                new Player("gignac"),
                new Player("lemina"),
                new Player("brice samba"),
                new Player("benjamin mendy"),
                new Player("imbula"),
                new Player("khelifa")
        );
    }


    private static Set<Player> arsenalPlayers() {
        return Sets.newHashSet(
                new Player("szczesny", "13szczesny13"),
                new Player("sagna"),
                new Player("vermaelen"),
                new Player("gibbs"),
                new Player("koscielny"),
                new Player("ozil", "oezil"),
                new Player("arteta"),
                new Player("cazorla"),
                new Player("ramsey"),
                new Player("giroud"),
                new Player("wilshere"),
                new Player("bendtner"),
                new Player("gnabry"),
                new Player("nacho monreal"),
                new Player("fabianski"),
                new Player("jenkinson"),
                new Player("hayden")
        );
    }



    private static MatchEvent baselChelsea() {
        Competition championsLeague = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team basel = twitterDAO.createOrGetTeam("basel");
        if (basel.getId() == null) {
            Set<Player> players = baselPlayers();
            Manager manager = new Manager("Yakin");
            basel = new Team(Country.CH, "FC Basel", manager, players, "basel", "bebbi", "rotblau");
        }
        Team chelsea = twitterDAO.createOrGetTeam("chelsea");
        if (chelsea.getId() == null) {
            Set<Player> players = chelseaPlayers();
            Manager manager = new Manager("Mourinho");
            chelsea = new Team(Country.EN, "Chelsea Londyn", manager, players, "chelsea", "cfc");

        }
        DateTime startDate = new DateTime(2013, 11, 26, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), basel, chelsea, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("jakob-park", "stephane lannoy"));
        return matchEvent;
    }

    private static Set<Player> baselPlayers() {
        return Sets.newHashSet(
                new Player("sommer"),
                new Player("voser"),
                new Player("ivanov"),
                new Player("schar"),
                new Player("xhaka"),
                new Player("delgado"),
                new Player("stocker"),
                new Player("serey die"),
                new Player("fabian frei"),
                new Player("mohamed salah"),
                new Player("streller"),
                new Player("giovanni sio"),
                new Player("marcelo diaz"),
                new Player("david degen"),
                new Player("ajeti"),
                new Player("vailati"),
                new Player("el nenny")
        );
    }

    private static Set<Player> chelseaPlayers() {
        return Sets.newHashSet(
                new Player("cech"),
                new Player("terry"),
                new Player("cahill"),
                new Player("ivanovic"),
                new Player("azpilicueta"),
                new Player("obi mikel"),
                new Player("wilian"),
                new Player("ramires"),
                new Player("samuel eto"),
                new Player("schurrle", "schuerrle"),
                new Player("demba ba"),
                new Player("de bruyne"),
                new Player("frank lampard"),
                new Player("ashley cole"),
                new Player("david luiz"),
                new Player("juan mata"),
                new Player("mark schwarzer")
        );
    }
}
