public class PastProject implements TextEntity {
    private String projectId;
    private String abstractText;

    public PastProject(String projectId, String abstractText) {
        this.projectId = projectId;
        this.abstractText = abstractText;
    }

    @Override
    public String getId() { return projectId; }

    @Override
    public String getTextContent() { return abstractText; }
}