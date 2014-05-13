package pl.edu.agh.twitter.business;

import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.matcheventgephi.boundary.MatchEventGephiDAO;
import pl.edu.agh.twitter.business.matcheventgephi.entity.MatchEventGephi;
import pl.edu.agh.twitter.business.team.boundary.TeamDAO;
import pl.edu.agh.twitter.business.team.entity.Team;

import javax.inject.Inject;
import java.util.List;

public class GephiDataPrinter implements Startable {
    @Inject
    MatchEventDAO matchEventDAO;

    @Inject
    TeamDAO teamDAO;

    @Inject
    MatchEventGephiDAO matchEventGephiDAO;



    @Override
    public void start() {
        final String[] teams = {"Arsenal", "Manchester United", "Manchester City", "Chelsea"};
        for(String name : teams) {
            final Team team = teamDAO.get(name);
            System.out.println("# " + team);
            printTeam(team);
        }
    }

    private void printTeam(Team team) {
        final List<MatchEvent> matchEvents = matchEventDAO.fetchAll(team);
        for(MatchEvent matchEvent : matchEvents) {
            printMatchEvent(matchEvent);
        }
    }

    private void printMatchEvent(MatchEvent matchEvent) {
        final String[] measures = {"inDegree", "outDegree", "weightedInDegree", "weightedOutDegree", "eccentricity",
                "closenessCentrality", "betweennessCentrality", "authority", "hub", "clusteringCoefficient",
                "eigenvectorCentrality"};
        System.out.println("## " + matchEvent);
        for(String measure : measures) {
            final List<MatchEventGephi> top10meg = matchEventGephiDAO.findTop10(matchEvent, measure);
            printMeasure(top10meg, measure);
        }
    }

    private void printMeasure(List<MatchEventGephi> top10meg, String measure) {
        System.out.println("### " + measure);
        for(MatchEventGephi meg : top10meg) {
            System.out.println("\t" + meg.prettyString(measure));
        }
    }
}
