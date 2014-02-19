package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "players")
public class Player {
    @Id
    public Long id;
    public String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "players_nicknames")
    @Column(name = "nickname")
    public Set<String> nicknames = new HashSet<String>();
}
