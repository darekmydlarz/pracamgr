package models;

import com.google.common.collect.Lists;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(schema = "mgr")
public class UserTeamStats {
    @Id
    public long id;
    public long userId;
    public long teamId;
    public long matches;
    public long positives;
    public long negatives;

    public static List<UserOccurencesSentimentDTO> getAggregatedForTeam(long teamId) {
        final String query = "SELECT team_Id, matches, count(*), sum(positives) pos, sum(negatives) neg " +
                " FROM mgr.user_team_stats " +
                " WHERE team_Id = " + teamId +
                " GROUP BY team_Id, matches" +
                " ORDER BY matches DESC  ";
        final Iterator<Object[]> it = JPA.em().createNativeQuery(query).getResultList().iterator();
        List<UserOccurencesSentimentDTO> uosDTOList = Lists.newArrayList();
        while(it.hasNext()) {
            Object[] row = it.next();
            final long teamId1 = ((Number) row[0]).longValue();
            final long matches1 = ((Number) row[1]).longValue();
            final long users = ((Number) row[2]).longValue();
            final long positives1 = ((Number) row[3]).longValue();
            final long negatives1 = ((Number) row[4]).longValue();
            UserOccurencesSentimentDTO uosDTO = new UserOccurencesSentimentDTO(teamId1, matches1, users, positives1, negatives1);
            uosDTOList.add(uosDTO);
        }
        return uosDTOList;
    }
}
