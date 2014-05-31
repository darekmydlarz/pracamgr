package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(schema = "mgr", name = "cliques_match_user")
public class CliquesMatchUser {
    @Id
    public long id;

    public long positives;
    public long negatives;

    @Transient
    public double positivness;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CLIQUE_ID")
    public CliquesMatch cliquesMatch;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public User user;

    @PostLoad
    private void postLoad() {
        positivness = 100.0 * positives / (positives + negatives);
    }
}
