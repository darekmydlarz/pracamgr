package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.MatchEventGephiDAO;
import pl.edu.agh.twitter.entities.MatchEventGephi;
import pl.edu.agh.twitter.entities.UserGroupDAO;
import pl.edu.agh.twitter.entities.UserGroup;

import javax.inject.Inject;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Utility class. It is reading Gephi output files for each match, and converts them to objects. After all
 * converted data objects are stored into database.
 */
public class GephiImporter implements Startable {
    Logger logger = Logger.getLogger(getClass());

    public enum UserGroupInput {
        ALL("gephi - Workspace 1 [Nodes].csv"),
        LEFT("gephi-all-left.csv");

        private final String path;

        UserGroupInput(String path) {
            this.path = path;
        }
    }

    @Inject
    UserGroupDAO userGroupDAO;

    @Inject
    MatchEventGephiDAO matchEventGephiDAO;

    @Override
    public void start() {
        try {
            File f = new File(getClass().getClassLoader().getResource("gephi/249-manunited-westham.csv").toURI());
            importMatchEventsGephi(f, 249l);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Long getMatchEventIdFromFile(File file) {
        return Long.valueOf(file.getName().split("-")[0]);
    }

    private File[] getGephiFiles() {
        try {
            final URL resource = getClass().getClassLoader().getResource("gephi");
            File directory = new File(resource.toURI());
            final File[] files = directory.listFiles();
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            return new File[]{};
        }
    }

    private void importMatchEventsGephi(File file, Long matchEventId) {
        try {
            List<MatchEventGephi> items = Lists.newArrayList();
            int lineCounter = 1;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            Map<String, Integer> headers = getHeadersPositions(line);
            while ((line = br.readLine()) != null) {
                final String[] row = line.split(",");
                MatchEventGephi meg = new MatchEventGephi();
                meg.setAuthority(Double.valueOf(row[headers.get("Authority")]));
                meg.setBetweennessCentrality(Double.valueOf(row[headers.get("Betweenness Centrality")]));
                meg.setClosenessCentrality(Double.valueOf(row[headers.get("Closeness Centrality")]));
                meg.setClusteringCoefficient(Double.valueOf(row[headers.get("Clustering Coefficient")]));
                meg.setDegree(Long.valueOf(row[headers.get("Degree")]));
                meg.setEccentricity(Double.valueOf(row[headers.get("Eccentricity")]));
                meg.setEigenvectorCentrality(Double.valueOf(row[headers.get("Eigenvector Centrality")]));
                meg.setHub(Double.valueOf(row[headers.get("Hub")]));
                meg.setInDegree(Long.valueOf(row[headers.get("In-Degree")]));
                meg.setMatchEventId(matchEventId);
                meg.setModularityClass(Long.valueOf(row[headers.get("Modularity Class")]));
                meg.setOutDegree(Long.valueOf(row[headers.get("Out-Degree")]));
                meg.setUserId(Long.valueOf(row[headers.get("Id")]));
                meg.setWeightedDegree(Double.valueOf(row[headers.get("Weighted Degree")]));
                meg.setWeightedInDegree(Double.valueOf(row[headers.get("Weighted In-Degree")]));
                meg.setWeightedOutDegree(Double.valueOf(row[headers.get("Weighted Out-Degree")]));
                if (meg.getDegree() > 0) {
                    logger.info(matchEventId + " : " + lineCounter);
                    items.add(meg);
                }
            }
            matchEventGephiDAO.persistAll(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> getHeadersPositions(String line) {
        Map<String, Integer> headers = Maps.newHashMap();
        final String[] headersRow = line.split(",");
        for (int i = 0; i < headersRow.length; ++i) {
            headers.put(headersRow[i], i);
        }
        return headers;
    }

    private void importUserGroupsAll() {
        try {
            final InputStream is = getClass().getClassLoader().getResourceAsStream(UserGroupInput.LEFT.path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int counter = 0;
            List<UserGroup> items = Lists.newArrayList();
            while ((line = br.readLine()) != null) {
                final String[] split = line.split(",");
                if (!StringUtils.isNumeric(split[0])) {
                    continue;
                }
                UserGroup userGroup = new UserGroup();
                userGroup.setUserId(Long.valueOf(split[0]));
                userGroup.setModularityClass(Long.valueOf(split[2]));
                userGroup.setInDegree(Long.valueOf(split[3]));
                userGroup.setOutDegree(Long.valueOf(split[4]));
                userGroup.setDegree(Long.valueOf(split[5]));
                userGroup.setWeightedDegree(Double.valueOf(split[6]));
                userGroup.setWeightedInDegree(Double.valueOf(split[7]));
                userGroup.setWeightedOutDegree(Double.valueOf(split[8]));
                userGroup.setEccentricity(Double.valueOf(split[9]));
                userGroup.setClosenessCentrality(Double.valueOf(split[10]));
                userGroup.setBetweennessCentrality(Double.valueOf(split[11]));
                userGroup.setComponentId(Double.valueOf(split[12]));
                userGroup.setStronglyConnectedId(Double.valueOf(split[13]));
                userGroup.setClusteringCoefficient(Double.valueOf(split[14]));
                userGroup.setEigenvectorCentrality(Double.valueOf(split[15]));

                items.add(userGroup);
                logger.info("COUNT: " + ++counter + " of 62101");
            }
            userGroupDAO.persistAll(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
