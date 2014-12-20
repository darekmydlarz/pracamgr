package pl.edu.agh.twitter;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.Coordinates;
import pl.edu.agh.twitter.entities.Geodata;
import pl.edu.agh.twitter.entities.GeodataDAO;
import pl.edu.agh.twitter.entities.TweetDAO;
import pl.edu.agh.twitter.entities.Tweet;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Utility class. It is fetching data for all geotagged tweets from OpenStreeMap.
 * It is georeversing places from where tweet were posted.
 * After all Address data for each tweets are stored into database.
 */
@Singleton
public class GeoReversing implements Startable {
    Logger logger = Logger.getLogger(getClass());
    @Inject
    TweetDAO tweetDAO;

    @Inject
    GeodataDAO geodataDAO;

    public static class Address {
        public String city;
        public String county;
        public String state;
        public String country;
        public String country_code;

        @Override
        public String toString() {
            return "Address{" +
                    "city='" + city + '\'' +
                    ", county='" + county + '\'' +
                    ", state='" + state + '\'' +
                    ", country='" + country + '\'' +
                    ", country_code='" + country_code + '\'' +
                    '}';
        }
    }

    public static class DataObject {
        String place_id;
        Address address;

        @Override
        public String toString() {
            return place_id + "::" + address;
        }
    }

    @Override
    public void start() {
        final List<Tweet> tweets = tweetDAO.getGeotagged();
        logger.info("GEOTAGGED NUMBER == " + tweets.size());
        int i = 0;
        for(Tweet tweet : tweets) {
            try {
                if(geodataDAO.isAlredyPersisted(tweet.getId())) {
                    ++i;
                } else {
                    Address address = restCall(tweet.getCoordinates());
                    Geodata geodata = new Geodata(tweet, address);
                    geodataDAO.persist(geodata);
                    System.out.println("Persisted " + (++i) + " of " + tweets.size());
                }
            } catch (Exception e) {
                logger.error(e);
                logger.error(tweet);
            }
        }
    }

    private Address restCall(Coordinates coords) throws IOException {
        Gson gson = new Gson();
        URL url = new URL("http://nominatim.openstreetmap.org/reverse?format=json&zoom=18&addressdetails=1" +
                "&email=youremail@gmail.com&accept-language=en" +
                "&lat=" + coords.getLatitude() +
                "&lon=" + coords.getLongitude());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        final DataObject fromJson = gson.fromJson(br, DataObject.class);
        conn.disconnect();
        return fromJson.address;
    }
}
