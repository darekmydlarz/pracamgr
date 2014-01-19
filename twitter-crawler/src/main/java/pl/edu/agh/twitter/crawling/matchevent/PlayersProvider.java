package pl.edu.agh.twitter.crawling.matchevent;

import com.google.common.collect.Sets;
import pl.edu.agh.twitter.business.player.entity.Player;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class PlayersProvider {
    public Set<Player> barcelonaPlayers() {
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

    public Set<Player> acmilanPlayers() {
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



    public Set<Player> shakhtarPlayers() {
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

    public Set<Player> evertonPlayers() {
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


    public Set<Player> liverpoolPlayers() {
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

    public Set<Player> fulhamPlayers() {
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



    public Set<Player> bayernPlayers() {
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


    public Set<Player> crystalPalacePlayers() {
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


    public Set<Player> steauaPlayers() {
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

    public Set<Player> westhamPlayers() {
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


    public Set<Player> newcastlePlayers() {
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


    public Set<Player> stokePlayers() {
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

    public Set<Player> swanseaPlayers() {
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

    public Set<Player> mancityPlayers() {
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

    public Set<Player> viktoriaPlayers() {
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

    public Set<Player> baselPlayers() {
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

    public Set<Player> chelseaPlayers() {
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

    public Set<Player> cardiffPlayers() {
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


    public Set<Player> olympiquePlayers() {
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


    public Set<Player> arsenalPlayers() {
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

    public Set<Player> astonVillaPlayers() {
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

    public Set<Player> tottenhamPlayers() {
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

    public Set<Player> leverkusenPlayers() {
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

    public Set<Player> manuPlayers() {
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

    public Set<Player> wbaPlayers() {
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

    public Set<Player> sunderlandPlayers() {
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

    public Set<Player> norwichPlayers() {
        return Sets.newHashSet(
                new Player("john ruddy"),
                new Player("steven whittaker"),
                new Player("martin olsson"),
                new Player("michael turner"),
                new Player("sebastien bassong"),
                new Player("leroy fer"),
                new Player("jonathan howson"),
                new Player("bradley johnson"),
                new Player("nathan redmond"),
                new Player("johan elmander"),
                new Player("gary hooper"),
                new Player("luciano becchio"),
                new Player("josh murphy"),
                new Player("russell martin"),
                new Player("wesley hoolahan"),
                new Player("javier garrido"),
                new Player("carlo nash"),
                new Player("Ryan Bennett")
        );
    }

    public Set<Player> hullPlayers() {
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

    public Set<Player> napoliPlayers() {
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
}
