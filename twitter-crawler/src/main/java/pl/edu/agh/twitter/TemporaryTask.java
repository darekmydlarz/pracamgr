package pl.edu.agh.twitter;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.IStemmer;
import edu.mit.jwi.morph.WordnetStemmer;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.sentiment.EmoticonClassifier;
import pl.edu.agh.twitter.sentiment.Sentiment;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TemporaryTask implements Startable {
    @Inject
    TweetDAO tweetDAO;

    @Override
    public void start() {
        final int size = tweetDAO.getAllWithEmoticons().size();
    }

    private void jwiDictionary() {
        try {
            IDictionary dict = new Dictionary(getClass().getClassLoader().getResource("dict"));
            dict.open();
            IStemmer stemmer = new WordnetStemmer(dict);
            for (POS pos : POS.values()) {
                final IIndexWord indexWord = dict.getIndexWord("swims", pos);
                if (indexWord != null) {
                    System.out.println(pos);
                    System.out.println(indexWord);
                    System.out.println("Lemma: == " + indexWord.getLemma());
                    System.out.println(indexWord.getWordIDs());
                    System.out.println(indexWord);
                    final List<String> stems = stemmer.findStems("swims", pos);
                    System.out.println(pos + ":" + stems);
                }
            }
            dict.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTweetsWithEmoticonAndNegation() {
        final List<Tweet> tweets = tweetDAO.getWithEmoticonsAndPattern(0, 100, "% not %");
        for (Tweet tweet : tweets) {
            Sentiment sentiment = EmoticonClassifier.getSentimentByEmoticon(tweet.getText());
            System.out.println(tweet.getId() + "\t" + tweet.getText().replaceAll("\\n", " ") + "\t" + sentiment);
        }
    }
}
