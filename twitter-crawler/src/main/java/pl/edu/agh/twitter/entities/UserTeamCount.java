package pl.edu.agh.twitter.entities;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import org.apache.commons.lang.math.NumberUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Mapped DB table
 */
@Entity
@Table(schema = "mgr")
public class UserTeamCount {
    @Id
    long userId;

    Long arsenal;
    Long chelsea;
    Long manchesterCity;
    Long manchesterUnited;

    public UserTeamCount(Number userId) {
        this.userId = userId.longValue();
    }

    public UserTeamCount() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getArsenal() {
        return arsenal;
    }

    public void setArsenal(Long arsenal) {
        this.arsenal = arsenal;
    }

    public long getChelsea() {
        return chelsea;
    }

    public void setChelsea(Long chelsea) {
        this.chelsea = chelsea;
    }

    public long getManchesterCity() {
        return manchesterCity;
    }

    public void setManchesterCity(Long manchesterCity) {
        this.manchesterCity = manchesterCity;
    }

    public long getManchesterUnited() {
        return manchesterUnited;
    }

    public void setManchesterUnited(Long manchesterUnited) {
        this.manchesterUnited = manchesterUnited;
    }

    public String getTopTeam() {
        List<Long> values = Lists.newArrayList();
        if(arsenal != null)
            values.add(arsenal);
        if(chelsea != null)
            values.add(chelsea);
        if(manchesterCity != null)
            values.add(manchesterCity);
        if(manchesterUnited != null)
            values.add(manchesterUnited);
        final long max = NumberUtils.max(Longs.toArray(values));
        String topTeam = null;
        if(arsenal != null && arsenal == max) {
            topTeam = "Arsenal";
        }
        if(chelsea != null && chelsea == max) {
            if(topTeam != null)
                return null;
            topTeam = "Chelsea";
        }
        if(manchesterCity != null && manchesterCity == max) {
            if(topTeam != null)
                return null;
            topTeam = "City";
        }
        if(manchesterUnited != null && manchesterUnited == max) {
            if(topTeam != null)
                return null;
            topTeam = "United";
        }
        return topTeam;
    }
}
