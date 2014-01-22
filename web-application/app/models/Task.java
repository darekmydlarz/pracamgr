package models;

import play.data.validation.Constraints.Required;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "mgr")
public class Task implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;
    @Required
    public String label;

    public static List<Task> all() {
        return JPA.em().createQuery("FROM Task", Task.class).getResultList();
    }

    public static void create(Task task) {
        JPA.em().persist(task);
    }

    public static void delete(Long id) {
        JPA.em().remove(JPA.em().find(Task.class, id));
    }
}
