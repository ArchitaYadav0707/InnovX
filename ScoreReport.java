import java.io.Serializable;

public class ScoreReport implements Serializable {
    private static final long serialVersionUID = 1L;
    public String message;
    public double duplicateScore;
    public boolean duplicate;
    public String matchingIdeaId;

    public ScoreReport(String message, double duplicateScore) {
        this(message, duplicateScore, false, null);
    }

    public ScoreReport(String message, double duplicateScore, boolean duplicate, String matchingIdeaId) {
        this.message = message;
        this.duplicateScore = duplicateScore;
        this.duplicate = duplicate;
        this.matchingIdeaId = matchingIdeaId;
    }
}