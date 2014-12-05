package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.Relationship;
import pl.edu.agh.twitter.entities.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.entities.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.entities.team.boundary.TeamDAO;
import pl.edu.agh.twitter.entities.team.entity.Team;
import pl.edu.agh.twitter.entities.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.entities.tweet.entity.Tweet;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;
import pl.edu.agh.twitter.sentiment.Sentiment;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * Class where many temporary tasks is coded.
 * You can change which method to call by changing #start() method implementation.
 */
@Singleton
public class TemporaryTask implements Startable {
    Logger logger = Logger.getLogger(getClass());
    @Inject
    TweetDAO tweetDAO;

    @Inject
    MatchEventDAO matchEventDAO;

    @Inject
    TeamDAO teamDAO;

    @Override
    public void start() {
        try {
            cfinderDataGenerator();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all relationships for all teams into file which can be consumed by cfinder
     */
    private void cfinderDataGenerator() throws IOException {
        List<String> teams = Lists.newArrayList("Arsenal", "Manchester United", "Manchester City", "Chelsea");
        for(String name : teams) {
            final Team team = teamDAO.get(name);
            final List<MatchEvent> matchEvents = matchEventDAO.fetchAll(team);
            cfinderEachTeam(team.getName(), matchEvents);
        }
    }

    /**
     * Writes all relationships for a team into file which can be consumed by cfinder
     */
    private void cfinderEachTeam(String filename, List<MatchEvent> matchEvents) throws IOException {
        System.out.println("Writing to file: " + filename);
        final List<Relationship> relationships = tweetDAO.getRelationships(matchEvents);
        StringBuilder message = new StringBuilder();
        for(Relationship relationship : relationships) {
            message.append(relationship).append("\n");
        }
        Files.write(Paths.get("./cfinder/" + filename), message.toString().getBytes());
        System.out.println("[OK]");
    }


    /**
     * Writes all relationships for MatchEvents list into file which can be consumed by cfinder
     */
    private void cfinderEachMatch(List<MatchEvent> matchEvents) throws IOException {
        for(MatchEvent matchEvent : matchEvents) {
            System.out.println("Fetching data for: " + matchEvent);
            final List<Relationship> relationships = tweetDAO.getRelationships(matchEvent);
            StringBuilder message = new StringBuilder();
            String fileName = matchEvent.getHomeTeam() + " vs " + matchEvent.getAwayTeam();
            System.out.println("Writing to file: " + fileName);
            for(Relationship relationship : relationships) {
                message.append(relationship).append("\n");
            }

            Files.write(Paths.get("./cfinder/" + fileName), message.toString().getBytes());
            System.out.println("[OK]");
        }
    }

    /**
     * It simply counts geotagged tweets and store that info into MatchEvent entity
     */
    private void countGeotaggedData() {
        final List<MatchEvent> matchEvents = matchEventDAO.fetchAll();
        for(MatchEvent me : matchEvents) {
            final Long geotagged = tweetDAO.countGeotagged(me);
            me.setGeotagged(geotagged);
            matchEventDAO.merge(me);
            logger.info(me + " PERSISTED");
        }
    }


    /**
     * It prints tweets emoticons with negation to output. Written for debugging purposes.
     */
    private void getTweetsWithEmoticonAndNegation() {
        final List<Tweet> tweets = tweetDAO.getWithEmoticonsAndPattern(0, 100, "% not %");
        for (Tweet tweet : tweets) {
            Sentiment sentiment = EmoticonClassifier.getSentimentByEmoticon(tweet.getText());
            System.out.println(tweet.getId() + "\t" + tweet.getText().replaceAll("\\n", " ") + "\t" + sentiment);
        }
    }
}
