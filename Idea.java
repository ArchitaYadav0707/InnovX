import java.io.Serializable;

// Unit 1 & 2: Object, Encapsulation, Polymorphism (implements TextEntity)
public class Idea implements TextEntity, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String title;
    private String description;
    private String submitter;
    private IdeaStatus status;
    private double finalScore;
    private String analysisSummary = "Awaiting analysis";

    public Idea(String id, String title, String description) {
        this(id, title, description, "user");
    }

    public Idea(String id, String title, String description, String submitter) {
        this.id = id; // Unit 1: 'this' keyword
        this.title = title;
        this.description = description;
        this.submitter = submitter;
        this.status = IdeaStatus.SUBMITTED;
    }

    @Override
    public String getId() { return this.id; }

    @Override
    public String getTextContent() { return this.title + " " + this.description; }
    
    public IdeaStatus getStatus() { return status; }
    public void setStatus(IdeaStatus status) { this.status = status; }
    
    public double getFinalScore() { return finalScore; }
    public void setFinalScore(double finalScore) { this.finalScore = finalScore; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubmitter() { return submitter; }
    public String getAnalysisSummary() { return analysisSummary; }
    public void setAnalysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; }
}