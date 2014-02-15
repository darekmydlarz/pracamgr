package pl.edu.agh.twitter.business.wordfrequency.entity;

import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class WordFrequency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String word;

    private long positive;

    private long negative;

    @Enumerated(EnumType.STRING)
    private CountStrategy countStrategy;

    public WordFrequency() {
    }

    public WordFrequency(String word, CountStrategy countStrategy) {
        this.word = word;
        this.countStrategy = countStrategy;
    }

    public void setValue(String sentiment, long value) {
        if("positive".equals(sentiment)) {
            positive = value;
        } else if ("negative".equals(sentiment)) {
            negative = value;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getPositive() {
        return positive;
    }

    public void setPositive(long positive) {
        this.positive = positive;
    }

    public long getNegative() {
        return negative;
    }

    public void setNegative(long negative) {
        this.negative = negative;
    }

    public CountStrategy getCountStrategy() {
        return countStrategy;
    }

    public void setCountStrategy(CountStrategy countStrategy) {
        this.countStrategy = countStrategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordFrequency)) return false;

        WordFrequency that = (WordFrequency) o;

        if (id != that.id) return false;
        if (negative != that.negative) return false;
        if (positive != that.positive) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (int) (positive ^ (positive >>> 32));
        result = 31 * result + (int) (negative ^ (negative >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return word + "=" + positive + "/" + negative;
    }
}
