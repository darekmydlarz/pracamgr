package pl.edu.agh.twitter.entities.usergroup.entity;

import pl.edu.agh.twitter.entities.usergroup.UserGroupName;

import javax.persistence.*;

@Entity
@Table(schema = "mgr", name = "users_group")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
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
    private Double componentId;
    private Double stronglyConnectedId;
    private Double clusteringCoefficient;
    private Double eigenvectorCentrality;

    @Enumerated(EnumType.STRING)
    private UserGroupName groupName;

    public UserGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Double getComponentId() {
        return componentId;
    }

    public void setComponentId(Double componentId) {
        this.componentId = componentId;
    }

    public Double getStronglyConnectedId() {
        return stronglyConnectedId;
    }

    public void setStronglyConnectedId(Double stronglyConnectedId) {
        this.stronglyConnectedId = stronglyConnectedId;
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

    public UserGroupName getGroupName() {
        return groupName;
    }

    public void setGroupName(UserGroupName groupName) {
        this.groupName = groupName;
    }
}
