package pl.edu.agh.twitter.entities.player.entity;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema = "mgr", name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "players_nicknames")
    @Column(name = "nickname")
    private Set<String> nicknames = new HashSet<String>();

    public Player() {
    }

    public Player(String lastName, Set<String> nicknames) {
        this.lastName = lastName;
        this.nicknames.addAll(nicknames);
    }

    public Player(String lastName, String ... nicknames) {
        this(lastName, Sets.newHashSet(nicknames));
    }

    public Player(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(Set<String> nicknames) {
        this.nicknames = nicknames;
    }

    public Set<String> getKeywords() {
        Set<String> keywords = Sets.newHashSet(nicknames);
        keywords.add(lastName);
        return keywords;
    }
}
