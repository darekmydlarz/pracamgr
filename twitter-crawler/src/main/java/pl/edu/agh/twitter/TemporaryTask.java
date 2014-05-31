package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.IStemmer;
import edu.mit.jwi.morph.WordnetStemmer;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.business.Relationship;
import pl.edu.agh.twitter.business.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.team.boundary.TeamDAO;
import pl.edu.agh.twitter.business.team.entity.Team;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;
import pl.edu.agh.twitter.sentiment.Sentiment;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

    private void cfinderDataGenerator() throws IOException {
        List<String> teams = Lists.newArrayList("Arsenal", "Manchester United", "Manchester City", "Chelsea");
        for(String name : teams) {
            final Team team = teamDAO.get(name);
            final List<MatchEvent> matchEvents = matchEventDAO.fetchAll(team);
            cfinderEachTeam(team.getName(), matchEvents);
        }
    }

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

    private void countGeotaggedData() {
        final List<MatchEvent> matchEvents = matchEventDAO.fetchAll();
        for(MatchEvent me : matchEvents) {
            final Long geotagged = tweetDAO.countGeotagged(me);
            me.setGeotagged(geotagged);
            matchEventDAO.merge(me);
            logger.info(me + " PERSISTED");
        }
    }

    private void getAllWithEmoticons() {
        final int size = tweetDAO.getAllWithEmoticons().size();
    }

    private void jwiDictionary() {
        try {
            IDictionary dict = new Dictionary(getClass().getClassLoader().getResource("dict"));
            dict.open();
            IStemmer stemmer = new WordnetStemmer(dict);
            for (POS pos : POS.values()) {
                final IIndexWord indexWord = dict.getIndexWord("swims", pos);
                if (indexWord != null) {
                    System.out.println(pos);
                    System.out.println(indexWord);
                    System.out.println("Lemma: == " + indexWord.getLemma());
                    System.out.println(indexWord.getWordIDs());
                    System.out.println(indexWord);
                    final List<String> stems = stemmer.findStems("swims", pos);
                    System.out.println(pos + ":" + stems);
                }
            }
            dict.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTweetsWithEmoticonAndNegation() {
        final List<Tweet> tweets = tweetDAO.getWithEmoticonsAndPattern(0, 100, "% not %");
        for (Tweet tweet : tweets) {
            Sentiment sentiment = EmoticonClassifier.getSentimentByEmoticon(tweet.getText());
            System.out.println(tweet.getId() + "\t" + tweet.getText().replaceAll("\\n", " ") + "\t" + sentiment);
        }
    }
}
