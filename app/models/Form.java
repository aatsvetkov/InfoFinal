package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Alexander on 05.02.2016.
 */
@Entity
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "FROM_ANSWER_ID")
    private int id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "form", fetch=FetchType.EAGER)
    private Set<Answer> answers = new HashSet<>();

    public Form() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Form{" +
                "id=" + id +
                ", answers=" + answers +
                '}';
    }
}
