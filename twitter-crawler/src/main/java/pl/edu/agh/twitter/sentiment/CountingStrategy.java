package pl.edu.agh.twitter.sentiment;

import pl.edu.agh.twitter.business.wordfrequency.CountStrategy;

public interface CountingStrategy {
    CountStrategy getCountStrategy();
}
