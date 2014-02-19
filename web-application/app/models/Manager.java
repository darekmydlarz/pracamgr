package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "mgr", name = "managers")
public class Manager {
    @Id
    public Long id;
    public String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "managers_nicknames")
    @Column(name = "nickname")
    public Set<String> nicknames = new HashSet<String>();
}
