package pl.edu.agh.twitter.sentiment.cleaner;

import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;

/**
 * Class which keeps words defined as stop-words. They are read from files
 */
public enum StopList {
    ENGLISH("englishtoplist.csv"),
    DATABSE("stopdatabasekeywords.csv"),
    ALL("englishtoplist.csv", "stopdatabasekeywords.csv");

    private static final String[] NO_KEYWORDS = new String[0];
    private final Set<String> keywords = Sets.newHashSet();

    StopList(String ... fileNames) {
        for(String fileName : fileNames) {
            keywords.addAll(buildKeywordsSet(fileName));
        }
    }

    private Set<String> buildKeywordsSet(String fileName) {
        Set<String> keywords = Sets.newHashSet();
        final String[] keywordsFromFile = readKeywordsFromFile(fileName);
        Collections.addAll(keywords, keywordsFromFile);
        return keywords;
    }

    private String[] readKeywordsFromFile(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.startsWith("#"))
                    return line.split(",");
            }
            return NO_KEYWORDS;
        } catch (IOException e) {
            return NO_KEYWORDS;
        }
    }

    public Set<String> getWords() {
        return Sets.newHashSet(keywords);
    }
}
