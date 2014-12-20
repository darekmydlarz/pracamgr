package pl.edu.agh.twitter.entities;

import javax.persistence.*;

/**
 * Mapped DB table
 */
@Entity
@Table(schema = "mgr", name = "competitions")
public class Competition {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;

    public Competition() {
    }

    public Competition(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
