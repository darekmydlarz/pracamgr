package pl.edu.agh.twitter;


import org.jboss.weld.environment.se.Weld;
import pl.edu.agh.twitter.sentiment.ParoubekClassifierPro;

public class EntryPoint {
//    private final static Class<? extends Startable> startable = GeoReversing.class;
    private final static Class<? extends Startable> startable = ParoubekClassifierPro.class;

    public static void main(String[] args) {
        Weld weld = new Weld();
        run(startable, weld);
        weld.shutdown();
    }

    private static void run(Class<? extends Startable> itemClass, Weld weld) {
        weld.initialize().instance().select(itemClass).get().start();
    }

}
