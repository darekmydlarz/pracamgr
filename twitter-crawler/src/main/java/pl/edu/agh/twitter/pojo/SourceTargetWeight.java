package pl.edu.agh.twitter.pojo;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public class SourceTargetWeight {
    private long source;
    private long target;
    private long weight;

    public SourceTargetWeight(long source, long target, long weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public SourceTargetWeight() {
    }

    public long getSource() {
        return source;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return StringUtils.join(Arrays.asList(source, target, weight), "\t");
    }
}
