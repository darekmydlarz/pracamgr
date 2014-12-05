package pl.edu.agh.twitter.entities.usereventstats;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class UserEventStats {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long userId;

    long matchId;

    long positives;

    long negatives;


}
