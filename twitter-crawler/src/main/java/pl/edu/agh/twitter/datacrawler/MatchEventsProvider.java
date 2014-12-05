package pl.edu.agh.twitter.datacrawler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.entities.Country;
import pl.edu.agh.twitter.entities.competition.boundary.CompetitionDAO;
import pl.edu.agh.twitter.entities.competition.entity.Competition;
import pl.edu.agh.twitter.entities.manager.entity.Manager;
import pl.edu.agh.twitter.entities.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.entities.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.entities.player.entity.Player;
import pl.edu.agh.twitter.entities.team.boundary.TeamDAO;
import pl.edu.agh.twitter.entities.team.entity.Team;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

/**
 * It this class all listened events are stored.
 * Here are described all key words which were used to listen streamed tweets.
 */
@Singleton
public class MatchEventsProvider implements Startable {

    @Inject
    private CompetitionDAO competitionDAO;

    @Inject
    private MatchEventDAO matchEventDAO;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private PlayersProvider playersProvider;

    @Override
    public void start() {
        List<MatchEvent> matchEvents = Lists.newArrayList(
                chelseaSwansea()
        );
        for (MatchEvent matchEvent : matchEvents) {
            matchEventDAO.merge(matchEvent);
        }
    }

    private MatchEvent barcelonaMilan() {
        Competition championsLeague = competitionDAO.createOrGet("UEFA Champions League");
        Set<Player> barcelonaPlayers = playersProvider.barcelonaPlayers();
        Set<Player> acmilanPlayers = playersProvider.acmilanPlayers();
        Manager gerardoMartino = new Manager("Gerardo Martino", Sets.newHashSet("Tata Martino", "Martino"));
        Manager massimilianoAllegri = new Manager("Massimiliano Allegri", Sets.newHashSet("Allegri"));
        Team fcBarcelona = new Team(Country.SP, "FC Barcelona", gerardoMartino, barcelonaPlayers,
                "fcb", "blaugrana", "barca", "barcelona", "barza", "@FCBarcelona");
        Team acMilan = new Team(Country.IT, "AC Milan", massimilianoAllegri, acmilanPlayers,
                "@acmilan", "ac milano", "milan");

        DateTime startDate = new DateTime(2013, 11, 6, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), fcBarcelona, acMilan, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("barcamilan", "camp nou"));
        return matchEvent;
    }

    private MatchEvent arsenalHull() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team arsenal = teamDAO.createOrGet("arsenal");
        Team hull = teamDAO.createOrGet("hull");
        DateTime startDate = new DateTime(2013, 12, 4, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, hull, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("andre marriner", "emirates"));
        return matchEvent;
    }

    private MatchEvent arsenalChelsea() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team arsenal = teamDAO.createOrGet("arsenal");
        Team chelsea = teamDAO.createOrGet("chelsea");
        DateTime startDate = new DateTime(2013, 12, 23, 21, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, chelsea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("michael dean", "emirates"));
        return matchEvent;
    }

    private MatchEvent arsenalEverton() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team arsenal = teamDAO.createOrGet("arsenal");
        Team everton = teamDAO.createOrGet("everton");
        DateTime startDate = new DateTime(2013, 12, 8, 17, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, everton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("howard webb", "emirates"));
        return matchEvent;
    }

    private MatchEvent manunitedEverton() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team manunited = teamDAO.createOrGet("manchester united");
        Team everton = teamDAO.createOrGet("everton");
        DateTime startDate = new DateTime(2013, 12, 10, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, everton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("martin atkinson", "old trafford"));
        return matchEvent;
    }

    private MatchEvent manunitedShakhtar() {
        Competition competition = competitionDAO.createOrGet("UEFA Champions League");
        Team manunited = teamDAO.createOrGet("manchester united");
        Team shakhtar = teamDAO.createOrGet("shakhtar");
        if (shakhtar.getId() == null) {
            Set<Player> players = playersProvider.shakhtarPlayers();
            Manager manager = new Manager("Lucescu");
            shakhtar = new Team(Country.UA, "Shakhtar Donetsk", manager, players, "shakhtar", "donetsk");
        }
        DateTime startDate = new DateTime(2013, 12, 10, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, shakhtar, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("old trafford"));
        return matchEvent;
    }

    private MatchEvent evertonLiverpool() {
        Competition premiership = competitionDAO.createOrGet("Barclays Premier League");
        Set<Player> evertonPlayers = playersProvider.evertonPlayers();
        Set<Player> liverpoolPlayers = playersProvider.liverpoolPlayers();
        Manager robertoMartinez = new Manager("roberto martinez");
        Manager brendanRodgers = new Manager("brendan rodgers");
        Team evertonFc = new Team(Country.EN, "Everton FC", robertoMartinez, evertonPlayers,
                "everton", "efc", "coyb", "Toffees");
        Team liverpoolFc = new Team(Country.EN, "Liverpool FC", brendanRodgers, liverpoolPlayers,
                "liverpool", "lfc", "reds", "kopites", "pool");

        DateTime startDate = new DateTime(2013, 11, 23, 13, 45, 0);
        return new MatchEvent(startDate.toDate(), evertonFc, liverpoolFc, premiership, "efclfc", "goodison");
    }

    private MatchEvent arsenalOlympique() {
        Competition championsLeague = competitionDAO.createOrGet("UEFA Champions League");
        Team arsenal = teamDAO.createOrGet("arsenal");
        if (arsenal.getId() == null) {
            Set<Player> arsenalPlayers = playersProvider.arsenalPlayers();
            Manager arseneWenger = new Manager("Wenger");
            arsenal = new Team(Country.EN, "Arsenal Londyn", arseneWenger, arsenalPlayers, "arsenal", "afc", "gunners");
        }
        Team olympique = teamDAO.createOrGet("marseille");
        if (olympique.getId() == null) {
            Set<Player> olympiquePlayers = playersProvider.olympiquePlayers();
            Manager elieBaup = new Manager("Elie Baup");
            olympique = new Team(Country.FR, "OLYMPIQUE MARSEILLE", elieBaup, olympiquePlayers, "marseille");
        }
        DateTime startDate = new DateTime(2013, 11, 26, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), arsenal, olympique, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("emirates", "antonio mateu"));
        return matchEvent;
    }

    private MatchEvent southamptonMcfc() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team southampton = teamDAO.createOrGet("southampton");
        Team mcfc = teamDAO.createOrGet("manchester city");

        DateTime startDate = new DateTime(2013, 12, 7, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), southampton, mcfc, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("anthony taylor", "st mary stadium"));
        return matchEvent;
    }


    private MatchEvent fulhamMcfc() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team fulham = teamDAO.createOrGet("fulham");
        if (fulham.getId() == null) {
            Set<Player> players = playersProvider.fulhamPlayers();
            Manager manager = new Manager("Meulensteen");
            fulham = new Team(Country.EN, "Fulham FC", manager, players, "ffc", "fulhamfc", "fulham");
        }
        Team mcfc = teamDAO.createOrGet("manchester city");

        DateTime startDate = new DateTime(2013, 12, 21, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), fulham, mcfc, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("kevin friend", "craven cottage"));
        return matchEvent;
    }

    private MatchEvent bayernMcfc() {
        Competition competition = competitionDAO.createOrGet("UEFA Champions League");
        Team bayern = teamDAO.createOrGet("bayern monachium");
        if (bayern.getId() == null) {
            Set<Player> players = playersProvider.bayernPlayers();
            Manager manager = new Manager("Guardiola");
            bayern = new Team(Country.DE, "Bayern Monachium", manager, players, "bayern", "bayern munich", "fcbayern", "fc bayern");
        }
        Team mcfc = teamDAO.createOrGet("manchester city");

        DateTime startDate = new DateTime(2013, 12, 10, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), bayern, mcfc, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("allianz arena"));
        return matchEvent;
    }

    private MatchEvent chelseaSwansea() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team chelsea = teamDAO.createOrGet("chelsea");
        Team southampton = teamDAO.createOrGet("swansea");

        DateTime startDate = new DateTime(2013, 12, 26, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, southampton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge"));
        return matchEvent;
    }

    private MatchEvent chelseaLiverpool() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team chelsea = teamDAO.createOrGet("chelsea");
        Team liverpool = teamDAO.createOrGet("liverpool");

        DateTime startDate = new DateTime(2013, 12, 29, 17, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, liverpool, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge"));
        return matchEvent;
    }

    private MatchEvent chelseaSouthampton() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team chelsea = teamDAO.createOrGet("chelsea");
        Team southampton = teamDAO.createOrGet("southampton");

        DateTime startDate = new DateTime(2013, 12, 1, 17, 10, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, southampton, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge", "michael oliver"));
        return matchEvent;
    }

    private MatchEvent chelseaCrystalPalace() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team chelsea = teamDAO.createOrGet("chelsea");
        Team crystalPalace = teamDAO.createOrGet("crystal palace");
        if (crystalPalace.getId() == null) {
            Set<Player> players = playersProvider.crystalPalacePlayers();
            Manager manager = new Manager("Pulis");
            crystalPalace = new Team(Country.EN, "Crystal Palace", manager, players, "CPFC");
        }
        DateTime startDate = new DateTime(2013, 12, 14, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, crystalPalace, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge"));
        return matchEvent;
    }

    private MatchEvent chelseaSteaua() {
        Competition competition = competitionDAO.createOrGet("UEFA Champions League");
        Team chelsea = teamDAO.createOrGet("chelsea");
        Team steaua = teamDAO.createOrGet("steaua");
        if (steaua.getId() == null) {
            Set<Player> players = playersProvider.steauaPlayers();
            Manager manager = new Manager("Reghecampf");
            steaua = new Team(Country.RO, "Steaua Bucuresti", manager, players, "Steaua", "Steaua Bucharest");
        }

        DateTime startDate = new DateTime(2013, 12, 11, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), chelsea, steaua, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stamford bridge"));
        return matchEvent;
    }

    private MatchEvent manunitedWestHam() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team manunited = teamDAO.createOrGet("manchester united");
        Team westham = teamDAO.createOrGet("west ham");
        if (westham.getId() == null) {
            Set<Player> players = playersProvider.westhamPlayers();
            Manager manager = new Manager("Allardyce");
            westham = new Team(Country.EN, "West Ham United", manager, players, "wufc", "westham", "west ham");
        }
        DateTime startDate = new DateTime(2013, 12, 21, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, westham, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("michael jones", "old trafford"));
        return matchEvent;
    }

    private MatchEvent manunitedNewcastle() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team manunited = teamDAO.createOrGet("manchester united");
        Team newcastle = teamDAO.createOrGet("newcastle");
        if (newcastle.getId() == null) {
            Set<Player> players = playersProvider.newcastlePlayers();
            Manager manager = new Manager("Pardew");
            newcastle = new Team(Country.EN, "Newcastle United", manager, players, "nufc", "newcastle");
        }
        DateTime startDate = new DateTime(2013, 12, 7, 13, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), manunited, newcastle, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("andre marriner", "old trafford"));
        return matchEvent;
    }
    private MatchEvent stokeChelsea() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team stoke = teamDAO.createOrGet("stoke");
        if (stoke.getId() == null) {
            Set<Player> players = playersProvider.stokePlayers();
            Manager manager = new Manager("mark hughes");
            stoke = new Team(Country.EN, "Stoke City", manager, players, "stoke");
        }
        Team chelsea = teamDAO.createOrGet("chelsea");

        DateTime startDate = new DateTime(2013, 12, 7, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), stoke, chelsea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("britannia stadium", "jonathan moss"));
        return matchEvent;
    }

    private MatchEvent sunderlandChelsea() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team sunderland = teamDAO.createOrGet("sunderland");
        if (sunderland.getId() == null) {
            Set<Player> players = playersProvider.sunderlandPlayers();
            Manager manager = new Manager("Poyet");
            sunderland = new Team(Country.EN, "Sunderland AFC", manager, players, "safc", "SAFCofficial", "sunderland");
        }
        Team chelsea = teamDAO.createOrGet("chelsea");

        DateTime startDate = new DateTime(2013, 12, 4, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), sunderland, chelsea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("stadium of light", "philip dowd"));
        return matchEvent;
    }

    private MatchEvent hullManchesterUnited() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team hull = teamDAO.createOrGet("hull");
        Team manchesterUnited = teamDAO.createOrGet("manchester united");

        DateTime startDate = new DateTime(2013, 12, 26, 13, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), hull, manchesterUnited, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("kingston stadium"));
        return matchEvent;
    }

    private MatchEvent norwichManchesterUnited() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team norwich = teamDAO.createOrGet("norwich");
        if (norwich.getId() == null) {
            Set<Player> players = playersProvider.norwichPlayers();
            Manager manager = new Manager("Chris Hughton");
            norwich = new Team(Country.EN, "Norwich City", manager, players, "norwich", "NorwichCityFC", "ncfc");
        }
        Team manchesterUnited = teamDAO.createOrGet("manchester united");

        DateTime startDate = new DateTime(2013, 12, 28, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), norwich, manchesterUnited, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("carrow road"));
        return matchEvent;
    }

    private MatchEvent hullLiverpool() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team liverpool = teamDAO.createOrGet("liverpool");
        Team hull = teamDAO.createOrGet("hull");
        if (hull.getId() == null) {
            Set<Player> players = playersProvider.hullPlayers();
            Manager manager = new Manager("Steve Bruce");
            hull = new Team(Country.EN, "Hull City", manager, players, "hull");
        }

        DateTime startDate = new DateTime(2013, 12, 1, 15, 5, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), hull, liverpool, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("howard webb"));
        return matchEvent;
    }

    private MatchEvent napoliArsenal() {
        Competition competition = competitionDAO.createOrGet("UEFA Champions League");
        Team arsenal = teamDAO.createOrGet("arsenal");
        Team napoli = teamDAO.createOrGet("napoli");
        if (napoli.getId() == null) {
            Set<Player> players = playersProvider.napoliPlayers();
            Manager manager = new Manager("Rafael Benitez", "Rafa Benitez");
            napoli = new Team(Country.IT, "Napoli", manager, players);
        }
        DateTime startDate = new DateTime(2013, 12, 11, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), napoli, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("san paolo"));
        return matchEvent;
    }

    private MatchEvent westhamArsenal() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team westham = teamDAO.createOrGet("west ham");
        Team arsenal = teamDAO.createOrGet("arsenal");
        DateTime startDate = new DateTime(2013, 12, 26, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), westham, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("boleyn ground"));
        return matchEvent;
    }

    private MatchEvent newcastleArsenal() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team newcastle = teamDAO.createOrGet("newcastle");
        Team arsenal = teamDAO.createOrGet("arsenal");
        DateTime startDate = new DateTime(2013, 12, 29, 14, 30, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), newcastle, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("james park"));
        return matchEvent;
    }

    private MatchEvent cardiffArsenal() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team arsenal = teamDAO.createOrGet("arsenal");
        Team cardiff = teamDAO.createOrGet("cardiff");
        if (cardiff.getId() == null) {
            Set<Player> players = playersProvider.cardiffPlayers();
            Manager manager = new Manager("Mackay");
            cardiff = new Team(Country.EN, "Cardiff City FC", manager, players, "ccfc", "cardiff", "@Bluebirds_News");
        }

        DateTime startDate = new DateTime(2013, 11, 30, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), cardiff, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("CCFCvAFC"));
        return matchEvent;
    }


    private MatchEvent astonVillaManuited() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team astonVilla = teamDAO.createOrGet("aston villa");
        if (astonVilla.getId() == null) {
            Set<Player> players = playersProvider.astonVillaPlayers();
            Manager manager = new Manager("Paul Lambert");
            astonVilla = new Team(Country.EN, "Aston Villa", manager, players, "avfc");
        }
        Team manunited = teamDAO.createOrGet("manchester united");
        DateTime startDate = new DateTime(2013, 12, 15, 14, 30, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), astonVilla, manunited, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("villa park", "chris foy"));
        return matchEvent;

    }

    private MatchEvent tottenhamUnited() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team tottenham = teamDAO.createOrGet("tottenham");
        if (tottenham.getId() == null) {
            Set<Player> players = playersProvider.tottenhamPlayers();
            Manager manager = new Manager("Villas-Boas", "avb", "boas");
            tottenham = new Team(Country.EN, "Tottenham Hotspur", manager, players, "tottenham", "@SpursOfficial");
        }
        Team manuited = teamDAO.createOrGet("manchester united");
        DateTime startDate = new DateTime(2013, 12, 1, 13, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), tottenham, manuited, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("spurs united", "white hart lane"));
        return matchEvent;
    }


    private MatchEvent leverkusenUnited() {
        Competition championsLeague = competitionDAO.createOrGet("UEFA Champions League");
        Team leverkusen = teamDAO.createOrGet("leverkusen");
        if (leverkusen.getId() == null) {
            Set<Player> players = playersProvider.leverkusenPlayers();
            Manager manager = new Manager("Hyypia");
            leverkusen = new Team(Country.DE, "Bayer Leverkusen", manager, players, "leverkusen", "bayer");
        }
        Team manuited = teamDAO.createOrGet("manchester united");
        if (manuited.getId() == null) {
            Set<Player> players = playersProvider.manuPlayers();
            Manager manager = new Manager("Moyes");
            manuited = new Team(Country.EN, "Manchester United", manager, players, "Man United", "manutd");
        }
        DateTime startDate = new DateTime(2013, 11, 27, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), leverkusen, manuited, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("bayarena", "svein moen"));
        return matchEvent;
    }


    private MatchEvent westBromwichMancity() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team wba = teamDAO.createOrGet("West Bromwich");
        Team mancity = teamDAO.createOrGet("manchester city");
        if (wba.getId() == null) {
            Set<Player> players = playersProvider.wbaPlayers();
            Manager manager = new Manager("Steve Clarke");
            wba = new Team(Country.EN, "West Bromwich Albion", manager, players, "west brom", "west bromwich", "WBAFCofficial", "baggies");
        }
        DateTime startDate = new DateTime(2013, 12, 4, 21, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), wba, mancity, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("Hawthorns", "chris foy"));
        return matchEvent;
    }

    private MatchEvent baselChelsea() {
        Competition championsLeague = competitionDAO.createOrGet("UEFA Champions League");
        Team basel = teamDAO.createOrGet("basel");
        if (basel.getId() == null) {
            Set<Player> players = playersProvider.baselPlayers();
            Manager manager = new Manager("Yakin");
            basel = new Team(Country.CH, "FC Basel", manager, players, "basel", "bebbi", "rotblau");
        }
        Team chelsea = teamDAO.createOrGet("chelsea");
        if (chelsea.getId() == null) {
            Set<Player> players = playersProvider.chelseaPlayers();
            Manager manager = new Manager("Mourinho");
            chelsea = new Team(Country.EN, "Chelsea Londyn", manager, players, "chelsea", "cfc");
        }
        DateTime startDate = new DateTime(2013, 11, 26, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), basel, chelsea, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("jakob-park", "stephane lannoy"));
        return matchEvent;
    }

    private MatchEvent mancityViktoria() {
        Competition championsLeague = competitionDAO.createOrGet("UEFA Champions League");
        Team mancity = teamDAO.createOrGet("manchester city");
        if (mancity.getId() == null) {
            Set<Player> players = playersProvider.mancityPlayers();
            Manager manager = new Manager("Pellegrini");
            mancity = new Team(Country.EN, "Manchester City", manager, players);
        }
        Team viktoria = teamDAO.createOrGet("viktoria plzen");
        if (viktoria.getId() == null) {
            Set<Player> players = playersProvider.viktoriaPlayers();
            Manager manager = new Manager("vrba");
            viktoria = new Team(Country.CZ, "Viktoria Plzen", manager, players);
        }
        DateTime startDate = new DateTime(2013, 11, 27, 20, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, viktoria, championsLeague);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad", "aydinus"));
        return matchEvent;
    }
    private MatchEvent mancitySwansea() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team mancity = teamDAO.createOrGet("manchester city");
        Team swansea = teamDAO.createOrGet("swansea");
        if (swansea.getId() == null) {
            Set<Player> players = playersProvider.swanseaPlayers();
            Manager manager = new Manager("Laudrup");
            swansea = new Team(Country.EN, "Swansea City", manager, players, "swans");
        }
        DateTime startDate = new DateTime(2013, 12, 1, 17, 10, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, swansea, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad", "mark clattenburg"));
        return matchEvent;
    }


    private MatchEvent mancityArsenal() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team mancity = teamDAO.createOrGet("manchester city");
        Team arsenal = teamDAO.createOrGet("arsenal");
        DateTime startDate = new DateTime(2013, 12, 14, 13, 45, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad", "martin atkinson"));
        return matchEvent;
    }


    private MatchEvent mancityCrystalPalace() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team mancity = teamDAO.createOrGet("manchester city");
        Team arsenal = teamDAO.createOrGet("crystal palace");
        DateTime startDate = new DateTime(2013, 12, 28, 16, 0, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, arsenal, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad"));
        return matchEvent;
    }


    private MatchEvent mancityLiverpool() {
        Competition competition = competitionDAO.createOrGet("Barclays Premier League");
        Team mancity = teamDAO.createOrGet("manchester city");
        Team liverpool = teamDAO.createOrGet("liverpool");
        DateTime startDate = new DateTime(2013, 12, 26, 18, 30, 0);
        MatchEvent matchEvent = new MatchEvent(startDate.toDate(), mancity, liverpool, competition);
        matchEvent.setAdditionalKeywords(Sets.newHashSet("etihad"));
        return matchEvent;
    }
}
