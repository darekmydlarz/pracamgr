package pl.edu.agh.twitter.entities;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.*;

/**
 * Mapped DB table
 */
@Entity
@Table(schema = "mgr", name = "managers")
public class Manager {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "managers_nicknames")
    @Column(name = "nickname")
    private Set<String> nicknames = new HashSet<String>();

    public Manager() {
    }

    public Manager(String lastName) {
        this.lastName = lastName;
    }

    public Manager(String lastName, Set<String> nicknames) {
        this.lastName = lastName;
        this.nicknames = nicknames;
    }

    public Manager(String lastName, String ... nicknames) {
        this(lastName, Sets.newHashSet(nicknames));
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
