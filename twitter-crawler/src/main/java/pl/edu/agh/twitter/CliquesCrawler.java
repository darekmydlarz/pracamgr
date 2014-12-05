package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import pl.edu.agh.twitter.entities.CliquesMatchUser.CliquesMatchUser;
import pl.edu.agh.twitter.entities.CliquesMatchUser.CliquesMatchUserDAO;
import pl.edu.agh.twitter.entities.cliquesmatch.CliquesMatch;
import pl.edu.agh.twitter.entities.cliquesmatch.CliquesMatchDAO;
import pl.edu.agh.twitter.entities.cliquesteam.business.CliquesTeamDAO;
import pl.edu.agh.twitter.entities.cliquesteam.entity.CliquesTeam;
import pl.edu.agh.twitter.entities.cliquesteamuser.business.CliquesTeamUserDAO;
import pl.edu.agh.twitter.entities.cliquesteamuser.entity.CliquesTeamUser;
import pl.edu.agh.twitter.entities.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.entities.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.entities.paroubektweet.boundary.ParoubekTweetDAO;
import pl.edu.agh.twitter.entities.paroubektweet.entity.ParoubekTweet;
import pl.edu.agh.twitter.entities.team.boundary.TeamDAO;
import pl.edu.agh.twitter.entities.team.entity.Team;
import pl.edu.agh.twitter.entities.user.boundary.UserDAO;
import pl.edu.agh.twitter.entities.user.entity.User;

import javax.inject.Inject;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class CliquesCrawler implements Startable {
    enum TeamEnum {
        Arsenal("Arsenal", "Arsenal Londyn_files-w-th1.000000"),
        Chelsea("Chelsea", "Chelsea Londyn_files-w-th1.000000"),
        ManchesterCity("Manchester City", "Manchester City_files-w-th1.000000"),
        ManchesterUnited("Manchester United", "Manchester United_files-w-th1.000000");

        TeamEnum(String name, String dir) {
            this.name = name;
            this.dir = dir;
        }

        String name;
        String dir;
    }

    @Inject
    TeamDAO teamDAO;

    @Inject
    CliquesTeamDAO cliquesTeamDAO;

    @Inject
    CliquesTeamUserDAO cliquesTeamUserDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    MatchEventDAO matchEventDAO;

    @Inject
    CliquesMatchDAO cliquesMatchDAO;

    @Inject
    CliquesMatchUserDAO cliquesMatchUserDAO;

    @Inject
    ParoubekTweetDAO paroubekTweetDAO;

    @Override
    public void start() {
//        sentimentForCliquesTeam();
        sentimentForCliquesMatches();
    }

    private void sentimentForCliquesMatches() {
        final List<CliquesMatchUser> matchUsers = cliquesMatchUserDAO.findAll();
        for(CliquesMatchUser matchUser : matchUsers) {
            System.out.println(matchUser);
            countSentimentValues(matchUser);
            cliquesMatchUserDAO.merge(matchUser);
            System.out.println("[OK]");
        }
    }

    private void countSentimentValues(CliquesMatchUser matchUser) {
        int positives = 0, negatives = 0;
        final List<ParoubekTweet> paroubekTweets = paroubekTweetDAO.find(matchUser.getUserId(), matchUser.getCliquesMatch().getMatch().getId());
        for(ParoubekTweet pt : paroubekTweets) {
            if(Double.isNaN(pt.getValence()))
                continue;
            if(pt.isPositive()) {
                positives++;
            } else {
                negatives++;
            }
        }
        matchUser.setPositives(positives);
        matchUser.setNegatives(negatives);
    }

    private void sentimentForCliquesTeam() {
        final List<CliquesTeamUser> teamUsers = cliquesTeamUserDAO.findAll();
        for(CliquesTeamUser teamUser : teamUsers) {
            System.out.println(teamUser);
            countSentimentValues(teamUser);
            cliquesTeamUserDAO.merge(teamUser);
            System.out.println("[OK]");
        }
    }

    private void countSentimentValues(CliquesTeamUser teamUser) {
        int positives = 0, negatives = 0;
        for(MatchEvent matchEvent : matchEventDAO.fetchAll(teamUser.getCliquesTeam().getTeam())) {
            System.out.println(teamUser + "::" + teamUser.getUserId());
            final List<ParoubekTweet> paroubekTweets = paroubekTweetDAO.find(teamUser.getUserId(), matchEvent.getId());
            for(ParoubekTweet pt : paroubekTweets) {
                if(Double.isNaN(pt.getValence()))
                    continue;
                if(pt.isPositive()) {
                    positives++;
                } else {
                    negatives++;
                }
            }
        }
        teamUser.setPositives(positives);
        teamUser.setNegatives(negatives);
    }

    private void persistCliquesMatches() {
        for(TeamEnum teamEnum : TeamEnum.values()) {
            Team team = teamDAO.get(teamEnum.name);
            System.out.println(team);
            final List<MatchEvent> matchEvents = matchEventDAO.fetchAll(team);
            for(MatchEvent matchEvent : matchEvents) {
                try {
                    System.out.println(matchEvent);
                    procesCliquePerMatch(matchEvent);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void procesCliquePerMatch(MatchEvent matchEvent) throws URISyntaxException, IOException {
        String matchEventPattern = matchEvent.getHomeTeam() + " vs " + matchEvent.getAwayTeam();
        File cfinderDir = new File(getClass().getClassLoader().getResource("cfinder").toURI());
        for(File file : cfinderDir.listFiles()) {
            if(file.isDirectory() && file.getName().startsWith(matchEventPattern)) {
                System.out.println(file);
                readCliqueForMatchDir(matchEvent, file);
            }
        }
    }

    private void readCliqueForMatchDir(MatchEvent matchEvent, File dir) throws IOException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("cfinder/" + dir.getName() + "/cliques");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null) {
            if(line.startsWith("#") || line.isEmpty())
                continue;
            long cliqueId = cliquesMatchDAO.persist(new CliquesMatch(matchEvent.getId())).getId();
            persistMatchCliqueUsers(cliqueId, getCliqueUsersScreenNames(line));
        }
    }

    private void persistCliquesTeam() {
        for(TeamEnum teamEnum : TeamEnum.values()) {
            try {
                readTeamDir(teamDAO.get(teamEnum.name), teamEnum.dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readTeamDir(Team team, String dir) throws IOException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("cfinder/" + dir + "/cliques");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null) {
            if(line.startsWith("#") || line.isEmpty())
                continue;
            long cliqueId = cliquesTeamDAO.persist(new CliquesTeam(team.getId())).getId();
            persistTeamCliqueUsers(cliqueId, getCliqueUsersScreenNames(line));
        }
    }

    private void persistTeamCliqueUsers(long cliqueId, List<String> cliqueUsers) {
        System.out.println(cliqueUsers);
        List<CliquesTeamUser> cliquesTeamUsers = Lists.newArrayList();
        for(String screeName : cliqueUsers) {
            final User user = userDAO.findByScreenName(screeName);
            cliquesTeamUsers.add(new CliquesTeamUser(cliqueId, user.getId()));
        }
        cliquesTeamUserDAO.persist(cliquesTeamUsers);
    }

    private void persistMatchCliqueUsers(long cliqueId, List<String> cliqueUsers) {
        System.out.println(cliqueUsers);
        List<CliquesMatchUser> cliquesMatchUsers = Lists.newArrayList();
        for(String screenName : cliqueUsers) {
            final User user = userDAO.findByScreenName(screenName);
            cliquesMatchUsers.add(new CliquesMatchUser(cliqueId, user.getId()));
        }
        cliquesMatchUserDAO.persist(cliquesMatchUsers);
    }

    private List<String> getCliqueUsersScreenNames(String line) {
        final String[] splitted = line.split(" ");
        return Arrays.asList(splitted).subList(1, splitted.length);
    }
}
