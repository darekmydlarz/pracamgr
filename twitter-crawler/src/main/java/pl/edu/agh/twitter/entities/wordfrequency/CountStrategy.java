package pl.edu.agh.twitter.entities.wordfrequency;


/**
 * Enum with count strategy for word frequency
 */
public enum CountStrategy {
    SIMPLE_SPLIT, STOP_LIST, TOKENIZER, SMART_CLEANER, LEARNING_CLEANER, NEGATION_DETECTOR, NEG_LEARN_CLEANER, IRRELEVANT_REMOVER
}
