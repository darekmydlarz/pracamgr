package pl.edu.agh.twitter.socialnetwork;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import org.apache.commons.lang.StringUtils;
import pl.edu.agh.twitter.dao.MatchEventDAO;
import pl.edu.agh.twitter.dao.TeamDAO;
import pl.edu.agh.twitter.dao.TweetDAO;
import pl.edu.agh.twitter.dao.UserDAO;
import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Team;
import pl.edu.agh.twitter.model.Tweet;
import pl.edu.agh.twitter.model.UserEntity;
import pl.edu.agh.twitter.pojo.SourceTargetWeight;
import pl.edu.agh.twitter.sentiment.SentimentAnalyser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class SocialNetworkAnalyser implements Runnable {
    private static final DecimalFormat decimalFormat = new DecimalFormat("##.00");

    @Inject
    private TweetDAO tweetDAO;

    @Inject
    private MatchEventDAO matchEventDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private TeamDAO teamDAO;

    List<String> teamNames = Arrays.asList(
//            "manchester united"
//            "manchester city"
//            "chelsea"
            "arsenal"
    );

    private final Classifier<String,String> sentimentClassifier = new SentimentAnalyser().sentimentClassifier(10000);

    public void run() {
        showSentiment();
    }

    private void showSentiment() {
        for (String name : teamNames) {
            Team team = teamDAO.find(name);
            showTopUsers(matchEventDAO.fetchAll(team), 20);
        }
    }

    private void getUsersStatistics() {
        Set<UserEntity> allUsers = Sets.newHashSet();
        for (String name : teamNames) {
            Team team = teamDAO.find(name);
            Set<UserEntity> users = getTopUsers(matchEventDAO.fetchAll(team), 40);
            for(UserEntity user : users) {
                user.addTeam(team);
//                System.out.println(user.getId() + "\t" + user.getScreenName() + "\t" + team.getName());
            }
            allUsers.addAll(users);
        }
        printUsers(allUsers);
//        printUsersRepliesCFinder(allUsers);
//        printUsersReplies(allUsers);
    }

    private void printUsersRepliesCFinder(Set<UserEntity> allUsers) {
        Map<Long, UserEntity> idUserMap = Maps.newHashMap();
        for(UserEntity user : allUsers) {
            idUserMap.put(user.getId(), user);
        }
        List<SourceTargetWeight> usersRepliesRelations = tweetDAO.getUsersRepliesRelations(allUsers);
        for(SourceTargetWeight relation : usersRepliesRelations) {
            UserEntity source = idUserMap.get(relation.getSource());
            UserEntity target = idUserMap.get(relation.getTarget());
            System.out.println(source.getScreenName() + "\t" + target.getScreenName() + "\t" + relation.getWeight());
        }
    }

    private void printUsersReplies(Set<UserEntity> allUsers) {
        List<SourceTargetWeight> usersRepliesRelations = tweetDAO.getUsersRepliesRelations(allUsers);
        System.out.println(StringUtils.join(usersRepliesRelations, "\n"));
    }

    private void printUsers(Set<UserEntity> allUsers) {
        for (UserEntity user : allUsers)
            System.out.println(user.getId() + "\t" + user.getScreenName() + "\t" + user.getMostOftenTeam().getName());
    }

    private Set<UserEntity> getTopUsers(List<MatchEvent> matchEvents, int limitPerMatch) {
        Set<UserEntity> users = Sets.newHashSet();
        for (MatchEvent match : matchEvents)
            users.addAll(userDAO.findTopUsers(match, limitPerMatch));
        return users;
    }

    private void showTopUsers(List<MatchEvent> matchEvents, int limit) {
        for (MatchEvent match : matchEvents) {
            List<UserEntity> users = userDAO.findTopUsers(match, limit);
            System.out.println(match);
            for (UserEntity userEntity : users) {
//                long geotaggedNumber = tweetDAO.getGeotaggedNumber(userEntity, match);
                int positiveTweets = 0, negativeTweets = 0;
                List<Tweet> tweets = tweetDAO.getTweets(userEntity, match);
                for(Tweet tweet : tweets) {
                    final List<String> text = Arrays.asList(tweet.getText().split("[\\s\\n]"));
                    final Classification<String,String> classification = sentimentClassifier.classify(text);
                    switch(classification.getCategory()) {
                        case "POS": positiveTweets++; break;
                        case "NEG": negativeTweets++; break;
                        default:
                            System.out.println("ERROR: " + classification.getCategory() + "::" + text);
                    }
                }
                System.out.println(userEntity + "\t" + positiveTweets + "\t" + negativeTweets);
            }
        }
    }
}
