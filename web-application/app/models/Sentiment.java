package models;

public enum Sentiment {
    POS, NEG;

    public static final double AVERAGE_VALENCE = 0.4786984978198536;

    public static Sentiment getByValence(Double valence) {
        if (valence == null)
            return null;
        return valence > AVERAGE_VALENCE ? POS : NEG;
    }
}
