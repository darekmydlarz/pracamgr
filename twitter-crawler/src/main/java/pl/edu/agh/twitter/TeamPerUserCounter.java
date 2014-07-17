package pl.edu.agh.twitter;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.twitter.business.UserTeamCount;
import pl.edu.agh.twitter.business.user.entity.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TeamPerUserCounter implements Startable {
    Logger logger = LoggerFactory.getLogger(getClass());

    String chelseaMatches = "267, 2680, 255, 720, 709, 243, 674, 589, 445";
    String arsenalMatches = "266, 258, 255, 715, 714, 244, 611, 547, 404, 176";
    String unitedMatches = "265, 256, 249, 725, 699, 197, 612, 567, 486";
    String cityMatches = "260, 259, 254, 715, 704, 198, 694, 609, 526";

    @Inject
    EntityManager em;

    Map<Long, UserTeamCount> utcMap = Maps.newHashMap();


    @Override
    public void start() {
        try {
            fillUsersTopTeam();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<UserTeamCount> findUTCList(int offset, int limit) {
        String query = "FROM UserTeamCount";
        return em.createQuery(query, UserTeamCount.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    void mergeUsers(Map<Long, String> usersTopTeam) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();

        for(Long userId : usersTopTeam.keySet()) {
            final User user = em.find(User.class, userId);
            if(user.getTopTeam() == null) {
                user.setTopTeam(usersTopTeam.get(userId));
                em.merge(user);
            }
        }

        transaction.commit();
    }

    private void fillUsersTopTeam() throws IOException {
        final Path file = Files.createFile(Paths.get("./topTeams.txt"));
        final FileWriter fileWriter = new FileWriter(file.toFile());
        int limit = 100_000, rowsNumber = 1_567_435;
        for(int offset = 0; offset < rowsNumber; offset += limit) {
            logger.info("Fetching {} / {}", offset, rowsNumber);
//            Map<Long, String> usersTopTeam = Maps.newHashMap();
            final List<UserTeamCount> utcList = findUTCList(offset, limit);
            logger.info("ok | appending...");
            for (UserTeamCount userTeamCount : utcList) {
                final String topTeam = userTeamCount.getTopTeam();
                final User user = em.find(User.class, userTeamCount.getUserId());
                if(topTeam != null && user.getTopTeam() == null) {
                    final String userId = userTeamCount.getUserId() + "";
//                    usersTopTeam.put(userId, topTeam);
                    fileWriter.append("UPDATE mgr.users SET top_team = '")
                            .append(topTeam)
                            .append("' WHERE id = ")
                            .append(userId)
                            .append(";")
                            .append(System.lineSeparator());
                }
            }

            logger.info("ok");
//            mergeUsers(usersTopTeam);
        }
        fileWriter.close();
    }

    UserTeamCount getUTC(long userId) {
        UserTeamCount userTeamCount = utcMap.get(userId);
        if (userTeamCount == null) {
            userTeamCount = new UserTeamCount(userId);
        }
        utcMap.put(userId, userTeamCount);
        return userTeamCount;
    }

    private void countUserPerTeamOccurences() {
        logger.info("DARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        logger.info("Chelsea");
        Iterator<Object[]> it = execute(chelseaMatches);
        logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        while(it.hasNext()) {
            Object[] row = it.next();
            UserTeamCount utc = getUTC(((Number) row[0]).longValue());
            utc.setChelsea(((Number) row[1]).longValue());
        }

        logger.info("Arsenal");
        it = execute(arsenalMatches);
        logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        while(it.hasNext()) {
            Object[] row = it.next();
            UserTeamCount utc = getUTC(((Number) row[0]).longValue());
            utc.setArsenal(((Number) row[1]).longValue());
        }

        logger.info("United");
        it = execute(unitedMatches);
        logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        while(it.hasNext()) {
            Object[] row = it.next();
            UserTeamCount utc = getUTC(((Number) row[0]).longValue());
            utc.setManchesterUnited(((Number) row[1]).longValue());
        }

        logger.info("City");
        it = execute(cityMatches);
        logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        while(it.hasNext()) {
            Object[] row = it.next();
            UserTeamCount utc = getUTC(((Number) row[0]).longValue());
            utc.setManchesterCity(((Number) row[1]).longValue());
        }
        logger.info("PERSISTING....");
        persistAll(utcMap);
        logger.info("ok!");
    }

    private void persistAll(Map<Long, UserTeamCount> utcMap) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        final Collection<UserTeamCount> values = utcMap.values();
        logger.info("USERS TO PERSIST: " + values.size());
        for (UserTeamCount userTeamCount : values) {
            em.merge(userTeamCount);
        }
        transaction.commit();
    }

//    private void persist(List<UserTeamCount> utcList) {
//        final EntityTransaction transaction = em.getTransaction();
//        if(!transaction.isActive())
//            transaction.begin();
//        for (UserTeamCount userTeamCount : utcList) {
//            em.merge(userTeamCount);
//        }
//        transaction.commit();
//    }

    private Iterator<Object[]> execute(String eventsIds) {
        String query = " select user_id, count(*) " +
            " from mgr.tweets " +
            "   where match_event IN ("+eventsIds+") " +
            "   GROUP BY user_id ";
        return em.createNativeQuery(query).getResultList().iterator();
    }
}
