package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "mgr", name = "match_events_gephi")
public class MatchEventsGephi implements Serializable {
    @Id
    @JsonIgnore
    public Long id;

    @JsonIgnore
    public Long matchEventId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public User user;

    public Long inDegree;
    public Long outDegree;
    public Double weightedInDegree;
    public Double weightedOutDegree;
    public Double eccentricity;
    public Double closenessCentrality;
    public Double betweennessCentrality;
    public Double authority;
    public Double hub;
    public Double clusteringCoefficient;
    public Double eigenvectorCentrality;
}
