package pl.edu.agh.twitter.socialnetwork;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import pl.edu.agh.twitter.dao.MatchEventDAO;
import pl.edu.agh.twitter.dao.TeamDAO;
import pl.edu.agh.twitter.dao.TweetDAO;
import pl.edu.agh.twitter.dao.UserDAO;
import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Team;
import pl.edu.agh.twitter.model.UserEntity;
import pl.edu.agh.twitter.pojo.SourceTargetWeight;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
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

    public void run() {
        List<String> teamNames = Arrays.asList(
                "manchester united",
                "manchester city",
                "chelsea",
                "arsenal"
        );
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
//        printUsersReplies(allUsers);
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
                long geotaggedNumber = tweetDAO.getGeotaggedNumber(userEntity, match);
                System.out.println(userEntity + "\t" + geotaggedNumber);
            }
        }
    }
}
