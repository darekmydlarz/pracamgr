package pl.edu.agh.twitter.entities.team.entity;

import com.google.common.collect.Lists;
import pl.edu.agh.twitter.entities.Country;
import pl.edu.agh.twitter.entities.manager.entity.Manager;
import pl.edu.agh.twitter.entities.player.entity.Player;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema = "mgr", name = "teams")
public class Team {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Country country;
    @Column(unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "team")
    private Set<Player> players;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "teams_nicknames")
    @Column(name = "nicknames")
    private Set<String> nicknames = new HashSet<String>();

    public Team() {
    }

    public Team(String name) {
        this(null, name, null, null);
    }

    public Team(Country country, String name, Manager manager, Set<Player> players, String ... nicknames) {
        this.country = country;
        this.name = name;
        this.manager = manager;
        this.players = players;
        this.nicknames.addAll(Arrays.asList(nicknames));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(Set<String> nicknames) {
        this.nicknames = nicknames;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<String> getKeywords() {
        List<String> keywords = Lists.newArrayList(nicknames);
        keywords.add(name);
        keywords.addAll(manager.getKeywords());
        for(Player player : players) {
            keywords.addAll(player.getKeywords());
        }
        return keywords;
    }

    @Override
    public String toString() {
        return name + " (" + country + ")";
    }
}
