package pl.edu.agh.twitter.entities;

/**
 * Relationship class: source, target and weight of relationship
 */
public class Relationship {
    String source;
    String target;
    int weight;

    public Relationship() {
    }

    public Relationship(String source, String target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source + " " + target + " " + weight;
    }
}
