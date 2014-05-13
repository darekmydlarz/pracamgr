package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "teams")
public class Team {
    @Id
    @JsonIgnore
    public Long id;

    @JsonIgnore
    @Enumerated(value = EnumType.STRING)
    public Country country;
    public String name;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    public Manager manager;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "team")
    public Set<Player> players;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "teams_nicknames")
    @Column(name = "nicknames")
    public Set<String> nicknames = new HashSet<String>();

    @Override
    public String toString() {
        return name;
    }

    public static Team find(String name) {
        final String query = "FROM Team t WHERE Lower(t.name) LIKE :name";
        return JPA.em().createQuery(query, Team.class)
                .setParameter("name", "%" + name.toLowerCase().replaceAll("-", " ") + "%")
                .getSingleResult();
    }
}
