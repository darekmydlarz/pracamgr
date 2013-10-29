package pl.edu.agh.twitter.crawler;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterServiceProvider {
	
	private static TwitterStream twitterStream;
	private static Twitter twitter;

	static {
		twitterStream = new TwitterStreamFactory().getInstance();
		twitter = new TwitterFactory().getInstance();
	}

	public static TwitterStream getTwitterStream() {
		return twitterStream;
	}
	
	public static Twitter getTwitter() {
		return twitter;
	}
}
