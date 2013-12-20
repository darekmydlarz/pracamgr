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
        List<MatchEvent> matchEvents = Lists.newArrayList(
                manunitedWestHam(),
                fulhamMcfc(),
                arsenalChelsea()
        );
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

    private static MatchEvent arsenalHull() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team arsenal = twitterDAO.createOrGetTeam("arsenal");
        Team hull = twitterDAO.createOrGetTeam("hull");
        DateTime startDate = new DateTime(2013, 12, 04, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, hull, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("andre marriner", "emirates"));
        return matchEvent;
    }

    private static MatchEvent arsenalChelsea() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team arsenal = twitterDAO.createOrGetTeam("arsenal");
        Team chelsea = twitterDAO.createOrGetTeam("chelsea");
        DateTime startDate = new DateTime(2013, 12, 23, 21, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, chelsea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("michael dean", "emirates"));
        return matchEvent;
    }

    private static MatchEvent arsenalEverton() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team arsenal = twitterDAO.createOrGetTeam("arsenal");
        Team everton = twitterDAO.createOrGetTeam("everton");
        DateTime startDate = new DateTime(2013, 12, 8, 17, 00, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, everton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("howard webb", "emirates"));
        return matchEvent;
    }

    private static MatchEvent manunitedEverton() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team manunited = twitterDAO.createOrGetTeam("manchester united");
        Team everton = twitterDAO.createOrGetTeam("everton");
        DateTime startDate = new DateTime(2013, 12, 10, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, everton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("martin atkinson", "old trafford"));
        return matchEvent;
    }

    private static MatchEvent manunitedShakhtar() {
        Competition competition = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team manunited = twitterDAO.createOrGetTeam("manchester united");
        Team shakhtar = twitterDAO.createOrGetTeam("shakhtar");
        if(shakhtar.getId() == null) {
            Set<Player> players = shakhtarPlayers();
            Manager manager = new Manager("Lucescu");
            shakhtar = new Team(Country.UA, "Shakhtar Donetsk", manager, players, "shakhtar", "donetsk");
        }
        DateTime startDate = new DateTime(2013, 12, 10, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, shakhtar, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("old trafford"));
        return matchEvent;
    }

    private static Set<Player> shakhtarPlayers() {
        return Sets.newHashSet(
                new Player("pyatov"),
                new Player("srna"),
                new Player("aleksandr kucher"),
                new Player("vyacheslav schevchuk"),
                new Player("Rakitskiy"),
                new Player("alex teixeira"),
                new Player("taras stepanenko"),
                new Player("douglas costa"),
                new Player("luiz adriano"),
                new Player("facundo ferreyra"),
                new Player("eduardo da silva"),
                new Player("tomas hubschmann"),
                new Player("lucas fernando"),
                new Player("kanibolotskiy"),
                new Player("sergey krivtsov")
        );
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

    private static MatchEvent southamptonMcfc() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team southampton = twitterDAO.createOrGetTeam("southampton");
        Team mcfc = twitterDAO.createOrGetTeam("manchester city");

        DateTime startDate = new DateTime(2013, 12, 07, 16, 00, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), southampton, mcfc, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("anthony taylor", "st mary stadium"));
        return matchEvent;
    }


    private static MatchEvent fulhamMcfc() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team fulham = twitterDAO.createOrGetTeam("fulham");
        if(fulham.getId() == null) {
            Set<Player> players = fulhamPlayers();
            Manager manager = new Manager("Meulensteen");
            fulham = new Team(Country.EN, "Fulham FC", manager, players, "ffc", "fulhamfc", "fulham");
        }
        Team mcfc = twitterDAO.createOrGetTeam("manchester city");

        DateTime startDate = new DateTime(2013, 12, 21, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), fulham, mcfc, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("kevin friend", "craven cottage"));
        return matchEvent;
    }

    private static Set<Player> fulhamPlayers() {
        return Sets.newHashSet(
                new Player("stekelenburg"),
                new Player("senderos"),
                new Player("riise"),
                new Player("aaron hughes"),
                new Player("rieter"),
                new Player("dejagah"),
                new Player("steve sidwell"),
                new Player("scott parker"),
                new Player("karagounis"),
                new Player("kacaniklic"),
                new Player("berbatov"),
                new Player("kasami"),
                new Player("darren bent"),
                new Player("damien duff"),
                new Player("bryan ruiz"),
                new Player("david stockdale"),
                new Player("kieran richardson"),
                new Player("zverotic")
        );
    }

    private static MatchEvent bayernMcfc() {
        Competition competition = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team bayern = twitterDAO.createOrGetTeam("bayern monachium");
        if(bayern.getId() == null) {
            Set<Player> players = bayernPlayers();
            Manager manager = new Manager("Guardiola");
            bayern = new Team(Country.DE, "Bayern Monachium", manager, players, "bayern", "bayern munich", "fcbayern", "fc bayern");
        }
        Team mcfc = twitterDAO.createOrGetTeam("manchester city");

        DateTime startDate = new DateTime(2013, 12, 10, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), bayern, mcfc, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("allianz arena"));
        return matchEvent;
    }

    private static Set<Player> bayernPlayers() {
        return Sets.newHashSet(
                new Player("neuer"),
                new Player("rafinha"),
                new Player("van buyten"),
                new Player("boateng"),
                new Player("alaba"),
                new Player("ribery"),
                new Player("kroos"),
                new Player("thiago alcantara"),
                new Player("gotze", "goetze"),
                new Player("mandzukic"),
                new Player("thomas muller"),
                new Player("dante"),
                new Player("jan kirchhoff"),
                new Player("claudio pizarro"),
                new Player("javi martinez"),
                new Player("shaqiri"),
                new Player("tom starke")
        );
    }

    private static MatchEvent chelseaSouthampton() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team chelsea = twitterDAO.createOrGetTeam("chelsea");
        Team southampton = twitterDAO.createOrGetTeam("southampton");

        DateTime startDate = new DateTime(2013, 12, 1, 17, 10, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, southampton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge", "michael oliver"));
        return matchEvent;
    }

	private static MatchEvent chelseaCrystalPalace() {
		Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
		Team chelsea = twitterDAO.createOrGetTeam("chelsea");
		Team crystalPalace = twitterDAO.createOrGetTeam("crystal palace");
		if(crystalPalace.getId() == null) {
			Set<Player> players = crystalPalacePlayers();
			Manager manager = new Manager("Pulis");
			crystalPalace = new Team(Country.EN, "Crystal Palace", manager, players, "CPFC");
		}
		DateTime startDate = new DateTime(2013, 12, 14, 16, 0, 0);
		MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, crystalPalace, competition);
		matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge"));
		return matchEvent;
	}

	private static Set<Player> crystalPalacePlayers() {
		return Sets.newHashSet(
				new Player("Speroni"),
				new Player("dany gabbidon", "daniel gabbidon"),
				new Player("damien delaney"),
				new Player("dean moxey"),
				new Player("joel ward"),
				new Player("mile jedinak"),
				new Player("digkacoi"),
				new Player("barry bannan"),
				new Player("chamakh"),
				new Player("cameron jerome"),
				new Player("mariappa"),
				new Player("stuart okeefe"),
				new Player("kevin philips"),
				new Player("dwight gayle"),
				new Player("jonathan williams"),
				new Player("jimmy kebe"),
				new Player("lewis price")
		);
	}

	private static MatchEvent chelseaSteaua() {
        Competition competition = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team chelsea = twitterDAO.createOrGetTeam("chelsea");
        Team steaua = twitterDAO.createOrGetTeam("steaua");
        if(steaua.getId() == null) {
            Set<Player> players = steauaPlayers();
            Manager manager = new Manager("Reghecampf");
            steaua = new Team(Country.RO, "Steaua Bucuresti", manager, players, "Steaua", "Steaua Bucharest");
        }

        DateTime startDate = new DateTime(2013, 12, 11, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, steaua, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge"));
        return matchEvent;
    }

    private static Set<Player> steauaPlayers() {
        return Sets.newHashSet(
                new Player("tatarusanu"),
                new Player("szukala"),
                new Player("parvulescu"),
                new Player("fernando varela"),
                new Player("gardos"),
                new Player("prepelita"),
                new Player("chipciu"),
                new Player("tanase"),
                new Player("bourceanu"),
                new Player("piovaccari"),
                new Player("stanciu"),
                new Player("iancu"),
                new Player("kapetanos"),
                new Player("rapa"),
                new Player("tarnovan"),
                new Player("gradinaru"),
                new Player("florin nita")
        );
    }

    private static MatchEvent manunitedWestHam() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team manunited = twitterDAO.createOrGetTeam("manchester united");
        Team westham = twitterDAO.createOrGetTeam("west ham");
        if(westham.getId() == null) {
            Set<Player> players = westhamPlayers();
            Manager manager = new Manager("Allardyce");
            westham = new Team(Country.EN, "West Ham United", manager, players, "wufc", "westham", "west ham");
        }
        DateTime startDate = new DateTime(2013, 12, 21, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, westham, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("michael jones", "old trafford"));
        return matchEvent;
    }

    private static Set<Player> westhamPlayers() {
        return Sets.newHashSet(
                new Player("adrian san miguel"),
                new Player("joey brien"),
                new Player("george mccartney"),
                new Player("james collins"),
                new Player("razvan rat"),
                new Player("joseph cole"),
                new Player("alou diarra"),
                new Player("matthew taylor"),
                new Player("matthew jarvis"),
                new Player("jack collison"),
                new Player("cartlon cole"),
                new Player("modibo maiga"),
                new Player("mohamed diame"),
                new Player("ravel morrison"),
                new Player("leo chambers"),
                new Player("guy demel"),
                new Player("jussi jaaskelainen"),
                new Player("mark noble")
        );
    }


    private static MatchEvent manunitedNewcastle() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team manunited = twitterDAO.createOrGetTeam("manchester united");
        Team newcastle = twitterDAO.createOrGetTeam("newcastle");
        if(newcastle.getId() == null) {
            Set<Player> players = newcastlePlayers();
            Manager manager = new Manager("Pardew");
            newcastle = new Team(Country.EN, "Newcastle United", manager, players, "nufc", "newcastle");
        }
        DateTime startDate = new DateTime(2013, 12, 07, 13, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, newcastle, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("andre marriner", "old trafford"));
        return matchEvent;
    }

    private static Set<Player> newcastlePlayers() {
        return Sets.newHashSet(
            new Player("krul"),
            new Player("coloccini"),
            new Player("debuchy"),
            new Player("mike williamson"),
            new Player("santon"),
            new Player("cabaye"),
            new Player("tiote"),
            new Player("sissoko"),
            new Player("shola ameobi"),
            new Player("remy"),
            new Player("gouffran"),
            new Player("haidara"),
            new Player("obertan"),
            new Player("mbiwa"),
            new Player("vurnon anita"),
            new Player("robert elliot"),
            new Player("sammy ameobi"),
            new Player("paul dummett")
        );
    }

    private static MatchEvent stokeChelsea() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team stoke = twitterDAO.createOrGetTeam("stoke");
        if(stoke.getId() == null) {
            Set<Player> players = stokePlayers();
            Manager manager = new Manager("mark hughes");
            stoke = new Team(Country.EN, "Stoke City", manager, players, "stoke");
        }
        Team chelsea = twitterDAO.createOrGetTeam("chelsea");

        DateTime startDate = new DateTime(2013, 12, 07, 16, 00, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), stoke, chelsea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("britannia stadium", "jonathan moss"));
        return matchEvent;
    }

    private static Set<Player> stokePlayers() {
        return Sets.newHashSet(
            new Player("asmir begovic"),
            new Player("erik pieters"),
            new Player("shawcross"),
            new Player("marc wilson"),
            new Player("geoff cameron"),
            new Player("charlie adam"),
            new Player("glenn whelan"),
            new Player("steven nzozi"),
            new Player("peter crouch"),
            new Player("arnautovic"),
            new Player("muniesa"),
            new Player("stephen ireland"),
            new Player("jermaine pennant"),
            new Player("wilson palacios"),
            new Player("kenwyne jones"),
            new Player("jonathan walters"),
            new Player("thomas sorensen")
        );
    }

    private static MatchEvent sunderlandChelsea() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team sunderland = twitterDAO.createOrGetTeam("sunderland");
        if(sunderland.getId() == null) {
            Set<Player> players = sunderlandPlayers();
            Manager manager = new Manager("Poyet");
            sunderland = new Team(Country.EN, "Sunderland AFC", manager, players, "safc", "SAFCofficial", "sunderland");
        }
        Team chelsea = twitterDAO.createOrGetTeam("chelsea");

        DateTime startDate = new DateTime(2013, 12, 04, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), sunderland, chelsea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stadium of light", "philip dowd"));
        return matchEvent;
    }

    private static Set<Player> sunderlandPlayers() {
        return Sets.newHashSet(
                new Player("vito mannone"),
                new Player("wesley brown"),
                new Player("john oshea"),
                new Player("adrea dossena"),
                new Player("philip bardsley"),
                new Player("craig gardner"),
                new Player("sebastian larsson"),
                new Player("sung yong ki"),
                new Player("emanuele giaccherini"),
                new Player("steven fletcher"),
                new Player("fabio borini"),
                new Player("adam johnson"),
                new Player("jack colback"),
                new Player("ondrej celustka"),
                new Player("jordan pickford"),
                new Player("josmer altidore"),
                new Player("valentin roberge"),
                new Player("lee cattermole")
        );
    }

    private static MatchEvent hullLiverpool() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team liverpool = twitterDAO.createOrGetTeam("liverpool");
        Team hull = twitterDAO.createOrGetTeam("hull");
        if (hull.getId() == null) {
            Set<Player> players = hullPlayers();
            Manager manager = new Manager("Steve Bruce");
            hull = new Team(Country.EN, "Hull City", manager, players, "hull");
        }

        DateTime startDate = new DateTime(2013, 12, 1, 15, 5, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), hull, liverpool, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("howard webb"));
        return matchEvent;
    }

    private static Set<Player> hullPlayers() {
        return Sets.newHashSet(
                new Player("allan mcgregor"),
                new Player("Maynor FIGUEROA"),
                new Player("curtis davies"),
                new Player("paul mcshane"),
                new Player("huddlestone"),
                new Player("robert koren"),
                new Player("ahmed el mohamady"),
                new Player("jake livermore"),
                new Player("yannick sagbo"),
                new Player("geore boyd"),
                new Player("robert brady"),
                new Player("danny graham"),
                new Player("liam rosenior"),
                new Player("mohamed gedo"),
                new Player("alex bruce"),
                new Player("david meyler"),
                new Player("stephen harper"),
                new Player("Abdoulaye Fayé")
        );
    }

    private static MatchEvent napoliArsenal() {
        Competition competition = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team arsenal = twitterDAO.createOrGetTeam("arsenal");
        Team napoli = twitterDAO.createOrGetTeam("napoli");
        if (napoli.getId() == null) {
            Set<Player> players = napoliPlayers();
            Manager manager = new Manager("Rafael Benitez", "Rafa Benitez");
            napoli = new Team(Country.IT, "Napoli", manager, players);
        }
        DateTime startDate = new DateTime(2013, 12, 11, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), napoli, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("san paolo"));
        return matchEvent;
    }

    private static Set<Player> napoliPlayers() {
        return Sets.newHashSet(
                new Player("rafael barbosa"),
                new Player("reveillere"),
                new Player("albiol"),
                new Player("federico fernandez"),
                new Player("dzemali"),
                new Player("christian maggio"),
                new Player("inler"),
                new Player("pandev"),
                new Player("higuain"),
                new Player("callejon"),
                new Player("lorenzo insigne"),
                new Player("mertens dries"),
                new Player("behrami"),
                new Player("armero"),
                new Player("bruno uvini"),
                new Player("roberto colombo"),
                new Player("radosevic"),
                new Player("pep reina", "pepe reina"),
                new Player("cannavaro paolo"),
                new Player("daniel zapata")
        );
    }

    private static MatchEvent cardiffArsenal() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team arsenal = twitterDAO.createOrGetTeam("arsenal");
        Team cardiff = twitterDAO.createOrGetTeam("cardiff");
        if (cardiff.getId() == null) {
            Set<Player> players = cardiffPlayers();
            Manager manager = new Manager("Mackay");
            cardiff = new Team(Country.EN, "Cardiff City FC", manager, players, "ccfc", "cardiff", "@Bluebirds_News");
        }

        DateTime startDate = new DateTime(2013, 11, 30, 16, 00, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), cardiff, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("CCFCvAFC"));
        return matchEvent;
    }

    private static Set<Player> cardiffPlayers() {
        return Sets.newHashSet(
                new Player("david marshall"),
                new Player("andrew taylor"),
                new Player("ben turner"),
                new Player("theophile catherine"),
                new Player("caulker"),
                new Player("whittingham"),
                new Player("don cowie"),
                new Player("gary medel"),
                new Player("jordon mutch"),
                new Player("odemwingie"),
                new Player("craig noone"),
                new Player("bo-kyung kim"),
                new Player("andreas kornelius"),
                new Player("mark hudson"),
                new Player("gunnarsson"),
                new Player("joe lewis"),
                new Player("craig bellamy"),
                new Player("frazier campbell")
        );
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



	private static MatchEvent astonVillaManuited() {
		Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
		Team astonVilla = twitterDAO.createOrGetTeam("aston villa");
		if (astonVilla.getId() == null) {
			Set<Player> players = astonVillaPlayers();
			Manager manager = new Manager("Paul Lambert");
			astonVilla = new Team(Country.EN, "Aston Villa", manager, players, "avfc");
		}
		Team manunited = twitterDAO.createOrGetTeam("manchester united");
		DateTime startDate = new DateTime(2013, 12, 15, 14, 30, 0);
		MatchEvent matchEvent = new MatchEvent(startDate.toDate(), astonVilla, manunited, competition);
		matchEvent.setAdditionalKeywords(Sets.newHashSet("villa park", "chris foy"));
		return matchEvent;

	}

	private static Set<Player> astonVillaPlayers() {
		return Sets.newHashSet(
			new Player("brad guzan"),
			new Player("ciaran clark"),
			new Player("nathan baker"),
			new Player("el ahmadi"),
			new Player("fabian delph"),
			new Player("ashley westwood"),
			new Player("leandro bacuna"),
			new Player("chris herd", "christopher herd"),
			new Player("benteke"),
			new Player("andreas weimann"),
			new Player("tonev"),
			new Player("marc albrighton"),
			new Player("matthew lowton"),
			new Player("helenius"),
			new Player("jed steer"),
			new Player("yacouba sylla"),
			new Player("jordan bowery")
		);
	}

	private static MatchEvent tottenhamUnited() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team tottenham = twitterDAO.createOrGetTeam("tottenham");
        if (tottenham.getId() == null) {
            Set<Player> players = tottenhamPlayers();
            Manager manager = new Manager("Villas-Boas", "avb", "boas");
            tottenham = new Team(Country.EN, "Tottenham Hotspur", manager, players, "tottenham", "@SpursOfficial");
        }
        Team manuited = twitterDAO.createOrGetTeam("manchester united");
        DateTime startDate = new DateTime(2013, 12, 1, 13, 00, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), tottenham, manuited, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("spurs united", "white hart lane"));
        return matchEvent;
    }

    private static Set<Player> tottenhamPlayers() {
        return Sets.newHashSet(
                new Player("lloris"),
                new Player("younes kaboul"),
                new Player("vertonghen"),
                new Player("michael dawson"),
                new Player("kyle walker"),
                new Player("aaron lennon"),
                new Player("paulinho"),
                new Player("lewis holtby"),
                new Player("soldado"),
                new Player("erik lamela"),
                new Player("adebayor"),
                new Player("dembele"),
                new Player("sigurdsson"),
                new Player("chiriches"),
                new Player("townsend"),
                new Player("defoe"),
                new Player("brad friedel")
        );
    }


    private static MatchEvent leverkusenUnited() {
        Competition championsLeague = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team leverkusen = twitterDAO.createOrGetTeam("leverkusen");
        if (leverkusen.getId() == null) {
            Set<Player> players = leverkusenPlayers();
            Manager manager = new Manager("Hyypia");
            leverkusen = new Team(Country.DE, "Bayer Leverkusen", manager, players, "leverkusen", "bayer");
        }
        Team manuited = twitterDAO.createOrGetTeam("manchester united");
        if (manuited.getId() == null) {
            Set<Player> players = manuPlayers();
            Manager manager = new Manager("Moyes");
            manuited = new Team(Country.EN, "Manchester United", manager, players, "Man United", "manutd");
        }
        DateTime startDate = new DateTime(2013, 11, 27, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), leverkusen, manuited, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("bayarena", "svein moen"));
        return matchEvent;
    }

    private static Set<Player> leverkusenPlayers() {
        return Sets.newHashSet(
                new Player("bernd leno"),
                new Player("boenisch", "bonisch"),
                new Player("spahic"),
                new Player("toprak"),
                new Player("donati"),
                new Player("rolfes"),
                new Player("sidney sam"),
                new Player("bender"),
                new Player("gonzalo castro"),
                new Player("kiessling", "kiesling"),
                new Player("hueng min son"),
                new Player("emre can"),
                new Player("jens hegeler"),
                new Player("robbie kruse"),
                new Player("wollscheid"),
                new Player("derdiyok"),
                new Player("roberto hilbert"),
                new Player("david yelldell")
        );
    }

    private static Set<Player> manuPlayers() {
        return Sets.newHashSet(
                new Player("de gea"),
                new Player("ferdinand"),
                new Player("vidic"),
                new Player("evra"),
                new Player("smalling"),
                new Player("antonio valencia"),
                new Player("giggs"),
                new Player("fellaini"),
                new Player("rooney"),
                new Player("kagawa"),
                new Player("hernandez"),
                new Player("ashley young"),
                new Player("van persie", "rvp"),
                new Player("phil jones"),
                new Player("anderson"),
                new Player("lindegaard"),
                new Player("nani"),
                new Player("buttner")
        );
    }


    private static MatchEvent westBromwichMancity() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team wba = twitterDAO.createOrGetTeam("West Bromwich");
        Team mancity = twitterDAO.createOrGetTeam("manchester city");
        if (wba.getId() == null) {
            Set<Player> players = wbaPlayers();
            Manager manager = new Manager("Steve Clarke");
            wba = new Team(Country.EN, "West Bromwich Albion", manager, players, "west brom", "west bromwich", "WBAFCofficial", "baggies");
        }
        DateTime startDate = new DateTime(2013, 12, 04, 21, 00, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), wba, mancity, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("Hawthorns", "chris foy"));
        return matchEvent;
    }

    private static Set<Player> wbaPlayers() {
        return Sets.newHashSet(
                new Player("boaz myhill"),
                new Player("jonas olsson"),
                new Player("liam ridgewell"),
                new Player("billy jones"),
                new Player("gareth mcauley"),
                new Player("morgan amalfitano"),
                new Player("stephane sessegnon"),
                new Player("youssouf mulumbu"),
                new Player("chris brunt"),
                new Player("shane long"),
                new Player("saido berahino"),
                new Player("victor anichebe"),
                new Player("zoltan gera"),
                new Player("goran popov"),
                new Player("claudio yacob"),
                new Player("diego lugano"),
                new Player("luke daniels")
        );
    }


	private static MatchEvent mancityArsenal() {
		Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
		Team mancity = twitterDAO.createOrGetTeam("manchester city");
		Team arsenal = twitterDAO.createOrGetTeam("arsenal");
		DateTime startDate = new DateTime(2013, 12, 14, 13, 45, 0);
		MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, arsenal, competition);
		matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad", "martin atkinson"));
		return matchEvent;
	}


    private static MatchEvent mancitySwansea() {
        Competition competition = twitterDAO.createOrGetCompetition("Barclays Premier League");
        Team mancity = twitterDAO.createOrGetTeam("manchester city");
        Team swansea = twitterDAO.createOrGetTeam("swansea");
        if (swansea.getId() == null) {
            Set<Player> players = swanseaPlayers();
            Manager manager = new Manager("Laudrup");
            swansea = new Team(Country.EN, "Swansea City", manager, players, "swans");
        }
        DateTime startDate = new DateTime(2013, 12, 1, 17, 10, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, swansea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad", "mark clattenburg"));
        return matchEvent;
    }

    private static Set<Player> swanseaPlayers() {
        return Sets.newHashSet(
            new Player("michael vorm"),
            new Player("ashley williams"),
            new Player("angel rangel"),
            new Player("benjamin davies"),
            new Player("jonathan de guzman"),
            new Player("nathan dyer"),
            new Player("jose canas"),
            new Player("alejandro pozuelo"),
            new Player("roland lamah"),
            new Player("bono wilfried", "wilfred bony"),
            new Player("jonjo shelvey"),
            new Player("dwight tiendalli"),
            new Player("alvaro vazquez"),
            new Player("jordi amat"),
            new Player("neil taylor"),
            new Player("leon britton"),
            new Player("gerhard tremmel")
        );
    }



    private static MatchEvent mancityViktoria() {
        Competition championsLeague = twitterDAO.createOrGetCompetition("UEFA Champions League");
        Team mancity = twitterDAO.createOrGetTeam("manchester city");
        if (mancity.getId() == null) {
            Set<Player> players = mancityPlayers();
            Manager manager = new Manager("Pellegrini");
            mancity = new Team(Country.EN, "Manchester City", manager, players);
        }
        Team viktoria = twitterDAO.createOrGetTeam("viktoria plzen");
        if (viktoria.getId() == null) {
            Set<Player> players = viktoriaPlayers();
            Manager manager = new Manager("vrba");
            viktoria = new Team(Country.CZ, "Viktoria Plzen", manager, players);
        }
        DateTime startDate = new DateTime(2013, 11, 27, 20, 45, 00);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, viktoria, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad", "aydinus"));
        return matchEvent;
    }

    private static Set<Player> mancityPlayers() {
        return Sets.newHashSet(
                new Player("pantilimon"),
                new Player("demichelis"),
                new Player("clichy"),
                new Player("zabaleta"),
                new Player("nastastic"),
                new Player("nasri"),
                new Player("silva"),
                new Player("yaya toure"),
                new Player("fernandinho"),
                new Player("aguero"),
                new Player("negredo"),
                new Player("kolarov"),
                new Player("navas"),
                new Player("hart"),
                new Player("micah richards"),
                new Player("lescott"),
                new Player("dzeko")
        );
    }

    private static Set<Player> viktoriaPlayers() {
        return Sets.newHashSet(
                new Player("kozacik"),
                new Player("hubnik"),
                new Player("prochazka"),
                new Player("cisovsky"),
                new Player("rajtoral"),
                new Player("petrzela"),
                new Player("kolar"),
                new Player("horvath"),
                new Player("horava"),
                new Player("duris"),
                new Player("tecl"),
                new Player("radim reznik"),
                new Player("pospisil"),
                new Player("bakos"),
                new Player("hejda"),
                new Player("petr bolek"),
                new Player("thomas wagner"),
                new Player("kovarik")
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
