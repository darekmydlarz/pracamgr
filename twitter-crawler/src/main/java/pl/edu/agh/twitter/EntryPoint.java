package pl.edu.agh.twitter;


import org.jboss.weld.environment.se.Weld;
import pl.edu.agh.twitter.socialnetwork.SocialNetworkAnalyser;

public class EntryPoint {
    public static void main(String[] args) {
        Weld weld = new Weld();
        run(SocialNetworkAnalyser.class, weld);
        weld.shutdown();
    }

    private static void run(Class<? extends Runnable> itemClass, Weld weld) {
        weld.initialize().instance().select(itemClass).get().run();
    }

}
