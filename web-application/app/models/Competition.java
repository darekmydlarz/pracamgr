package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "mgr", name = "competitions")
public class Competition {
    @Id
    public Long id;
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
