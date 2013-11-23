package pl.edu.agh.twitter.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema = "mgr", name = "match_events", uniqueConstraints = @UniqueConstraint(name = "date_home_away", columnNames = {"startDate", "home_team", "away_team"}))
public class MatchEvent {
    @Id @GeneratedValue
    private Long id;
    private Date startDate;
    @OneToOne(cascade = CascadeType.ALL)
    private Team homeTeam;
    @OneToOne(cascade = CascadeType.ALL)
    private Team awayTeam;
    @OneToOne(cascade = CascadeType.ALL)
    private Competition competition;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "mgr", name = "matchevent_keywords")
    @Column(name = "keyword")
    private Set<String> additionalKeywords = new HashSet<String>();

    public MatchEvent() {
    }

    public MatchEvent(Date startDate, Team homeTeam, Team awayTeam, Competition competition, String ... keywords) {
        this.startDate = startDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.competition = competition;
        additionalKeywords.addAll(Arrays.asList(keywords));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Set<String> getAdditionalKeywords() {
        return additionalKeywords;
    }

    public void setAdditionalKeywords(Set<String> additionalKeywords) {
        this.additionalKeywords = additionalKeywords;
    }

    public Set<String> getKeywords() {
        Set<String> keywords = Sets.newHashSet(additionalKeywords);
        keywords.addAll(awayTeam.getKeywords());
        keywords.addAll(homeTeam.getKeywords());
        return keywords;
    }

    public boolean isKeywordInString(String givenString) {
        String givenLower = givenString.toLowerCase();
        for(String keyword : getKeywords()) {
            if(givenLower.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
