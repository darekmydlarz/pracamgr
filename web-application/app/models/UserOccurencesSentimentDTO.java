package models;

public class UserOccurencesSentimentDTO {
    public long teamId;
    public long matches;
    public long users;
    public long positives;
    public long negatives;

    public UserOccurencesSentimentDTO(long teamId, long matches, long users, long positives, long negatives) {
        this.teamId = teamId;
        this.matches = matches;
        this.users = users;
        this.positives = positives;
        this.negatives = negatives;
    }
}
