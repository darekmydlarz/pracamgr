package pl.edu.agh.twitter.entities.matcheventgephi;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import pl.edu.agh.twitter.entities.user.entity.User;

import javax.persistence.*;

@Entity
@Table(schema = "mgr", name = "match_events_gephi")
public class MatchEventGephi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long matchEventId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "userId", referencedColumnName = "ID", updatable = false, insertable = false)
    private User user;
    private Long userId;
    private Long modularityClass;
    private Long inDegree;
    private Long outDegree;
    private Long degree;
    private Double weightedDegree;
    private Double weightedInDegree;
    private Double weightedOutDegree;
    private Double eccentricity;
    private Double closenessCentrality;
    private Double betweennessCentrality;
    private Double authority;
    private Double hub;
    private Double clusteringCoefficient;
    private Double eigenvectorCentrality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchEventId() {
        return matchEventId;
    }

    public void setMatchEventId(Long matchEventId) {
        this.matchEventId = matchEventId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getModularityClass() {
        return modularityClass;
    }

    public void setModularityClass(Long modularityClass) {
        this.modularityClass = modularityClass;
    }

    public Long getInDegree() {
        return inDegree;
    }

    public void setInDegree(Long inDegree) {
        this.inDegree = inDegree;
    }

    public Long getOutDegree() {
        return outDegree;
    }

    public void setOutDegree(Long outDegree) {
        this.outDegree = outDegree;
    }

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public Double getWeightedDegree() {
        return weightedDegree;
    }

    public void setWeightedDegree(Double weightedDegree) {
        this.weightedDegree = weightedDegree;
    }

    public Double getWeightedInDegree() {
        return weightedInDegree;
    }

    public void setWeightedInDegree(Double weightedInDegree) {
        this.weightedInDegree = weightedInDegree;
    }

    public Double getWeightedOutDegree() {
        return weightedOutDegree;
    }

    public void setWeightedOutDegree(Double weightedOutDegree) {
        this.weightedOutDegree = weightedOutDegree;
    }

    public Double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(Double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public Double getClosenessCentrality() {
        return closenessCentrality;
    }

    public void setClosenessCentrality(Double closenessCentrality) {
        this.closenessCentrality = closenessCentrality;
    }

    public Double getBetweennessCentrality() {
        return betweennessCentrality;
    }

    public void setBetweennessCentrality(Double betweennessCentrality) {
        this.betweennessCentrality = betweennessCentrality;
    }

    public Double getAuthority() {
        return authority;
    }

    public void setAuthority(Double authority) {
        this.authority = authority;
    }

    public Double getHub() {
        return hub;
    }

    public void setHub(Double hub) {
        this.hub = hub;
    }

    public Double getClusteringCoefficient() {
        return clusteringCoefficient;
    }

    public void setClusteringCoefficient(Double clusteringCoefficient) {
        this.clusteringCoefficient = clusteringCoefficient;
    }

    public Double getEigenvectorCentrality() {
        return eigenvectorCentrality;
    }

    public void setEigenvectorCentrality(Double eigenvectorCentrality) {
        this.eigenvectorCentrality = eigenvectorCentrality;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MatchEventGephi{" +
                "id=" + id +
                ", matchEventId=" + matchEventId +
                ", user=" + user +
                ", modularityClass=" + modularityClass +
                ", inDegree=" + inDegree +
                ", outDegree=" + outDegree +
                ", degree=" + degree +
                ", weightedDegree=" + weightedDegree +
                ", weightedInDegree=" + weightedInDegree +
                ", weightedOutDegree=" + weightedOutDegree +
                ", eccentricity=" + eccentricity +
                ", closenessCentrality=" + closenessCentrality +
                ", betweennessCentrality=" + betweennessCentrality +
                ", authority=" + authority +
                ", hub=" + hub +
                ", clusteringCoefficient=" + clusteringCoefficient +
                ", eigenvectorCentrality=" + eigenvectorCentrality +
                '}';
    }

    private Number getValueOf(String measure) {
        try {
            return (Number) getClass().getDeclaredField(measure).get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    public String prettyString(String measure) {
        StringBuilder sb = new StringBuilder();
        sb.append(user != null ? user.getId() + "\t" + user.getScreenName() : userId + "\t" + userId);
        sb.append("\t").append(getValueOf(measure));
        return sb.toString();
    }
}
