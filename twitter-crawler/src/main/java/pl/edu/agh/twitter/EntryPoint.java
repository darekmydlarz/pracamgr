package pl.edu.agh.twitter;


import org.jboss.weld.environment.se.Weld;
import pl.edu.agh.twitter.sentiment.NegationParoubekClassifier;

/**
 * Starting class. You have to change #startable variable value to change way of program is being executed.
 * Then you just start this class with EntryPoint#main method
 */
public class EntryPoint {
    private final static Class<? extends Startable> startable = NegationParoubekClassifier.class;

    public static void main(String[] args) {
        Weld weld = new Weld();
        run(startable, weld);
        weld.shutdown();
    }

    private static void run(Class<? extends Startable> itemClass, Weld weld) {
        weld.initialize().instance().select(itemClass).get().start();
    }

}
