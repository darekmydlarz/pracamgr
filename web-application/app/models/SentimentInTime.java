package models;

public class SentimentInTime {
    public String dateTime;
    public int positives;
    public int negatives;

    public SentimentInTime(String dateTime, int positives, int negatives) {
        this.dateTime = dateTime;
        this.positives = positives;
        this.negatives = negatives;
    }
}
