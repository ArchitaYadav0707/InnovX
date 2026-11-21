import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Unit 4: I/O Stream (Character oriented)
public class FileManager {
    public static List<PastProject> loadPastProjects(String filePath) throws IOException {
        List<PastProject> projects = new ArrayList<>();
        
        // Unit 3: Multiple try-catch/Finally (Try-with-resources handles finally)
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && !"USER".equals(parts[0]) && !"SUBMISSION".equals(parts[0])) {
                    projects.add(new PastProject(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            throw e; // Unit 3: throw keyword
        }
        return projects;
    }

    public static Map<String, String> loadUserCredentials(String filePath) throws IOException {
        Map<String, String> credentials = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 3 && "USER".equals(parts[0])) credentials.put(parts[1], parts[2]);
            }
        }
        return credentials;
    }

    public static List<Idea> loadSubmittedIdeas(String filePath) throws IOException {
        List<Idea> ideas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 8 && "SUBMISSION".equals(parts[0])) {
                    Idea idea = new Idea(parts[1], parts[2], parts[3], parts[4]);
                    try { idea.setStatus(IdeaStatus.valueOf(parts[5])); idea.setFinalScore(Double.parseDouble(parts[6])); } catch (Exception ignored) { }
                    idea.setAnalysisSummary(parts[7]); ideas.add(idea);
                }
            }
        }
        return ideas;
    }

    public static synchronized void appendUserCredential(String filePath, String username, String credentialHash) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.newLine();
            writer.write("USER|" + clean(username) + "|" + clean(credentialHash));
        }
    }

    public static synchronized void appendSubmission(String filePath, Idea idea) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.newLine();
            writer.write("SUBMISSION|" + clean(idea.getId()) + "|" + clean(idea.getTitle()) + "|" + clean(idea.getDescription()) + "|" + clean(idea.getSubmitter()) + "|" + idea.getStatus() + "|" + idea.getFinalScore() + "|" + clean(idea.getAnalysisSummary()));
        }
    }

    private static String clean(String value) { return value == null ? "" : value.replace('|', ' ').replace('\n', ' ').replace('\r', ' '); }
}