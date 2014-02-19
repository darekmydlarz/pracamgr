package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "teams")
public class Team {
    @Id
    public Long id;
    @Enumerated(value = EnumType.STRING)
    public Country country;
    public String name;

    @OneToOne(cascade = CascadeType.ALL)
    public Manager manager;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "team")
    public Set<Player> players;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "teams_nicknames")
    @Column(name = "nicknames")
    public Set<String> nicknames = new HashSet<String>();

    @Override
    public String toString() {
        return name;
    }
}
