import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.List;

public class SubmissionServer {
    private Repository<PastProject> projectRepository = new Repository<>();
    private Repository<Idea> submittedIdeas = new Repository<>();
    private SimilarityEngine similarityEngine = new JaccardSimilarityEngine();
    private static final double DUPLICATE_THRESHOLD = 0.65;

    public void startServer() {
        try {
            List<PastProject> savedProjects = FileManager.loadPastProjects("pastProjects.txt");
            for (PastProject p : savedProjects) {
                projectRepository.add(p);
            }
            for (Idea idea : FileManager.loadSubmittedIdeas("pastProjects.txt")) {
                submittedIdeas.add(idea);
            }
            System.out.println("Loaded " + savedProjects.size() + " projects from file.");
        } catch (Exception e) {
            System.out.println("Could not load past projects file.");
        }

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8080, 50, InetAddress.getLoopbackAddress())) {
                System.out.println("Server started on port 8080");
                while (true) {
                    Socket socket = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Object request = in.readObject();
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    
                    if ("ADMIN_LIST".equals(request)) {
                        out.writeObject(submittedIdeas.getAll());
                    } else if (request instanceof Idea) {
                        out.writeObject(analyzeAndStore((Idea) request));
                    } else {
                        out.writeObject(new ScoreReport("Unsupported request.", -1));
                    }
                    out.flush();
                    
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start(); 
    }

    private synchronized ScoreReport analyzeAndStore(Idea newIdea) {
        double highestRisk = 0.0;
        String matchingId = null;

        for (PastProject project : projectRepository.getAll()) {
            double score = similarityEngine.computeSimilarity(newIdea.getTextContent(), project.getTextContent());
            if (score > highestRisk) { highestRisk = score; matchingId = project.getId(); }
        }
        for (Idea submitted : submittedIdeas.getAll()) {
            double score = similarityEngine.computeSimilarity(newIdea.getTextContent(), submitted.getTextContent());
            if (score > highestRisk) { highestRisk = score; matchingId = submitted.getId(); }
        }

        boolean duplicate = highestRisk >= DUPLICATE_THRESHOLD;
        if (duplicate) {
            newIdea.setStatus(IdeaStatus.REJECTED);
            newIdea.setAnalysisSummary("Duplicate risk " + Math.round(highestRisk * 100) + "%" + (matchingId == null ? "" : "; closest record: " + matchingId));
        } else {
            newIdea.setStatus(IdeaStatus.SCREENING);
            newIdea.setFinalScore(1.0 - highestRisk);
            newIdea.setAnalysisSummary("Unique submission; duplicate risk " + Math.round(highestRisk * 100) + "%" + (matchingId == null ? "; no close record" : "; closest record: " + matchingId));
        }
        submittedIdeas.add(newIdea);
        
        try {
            FileManager.appendSubmission("pastProjects.txt", newIdea);
            String message = duplicate ? "Duplicate attempt recorded for your history." : "Idea accepted for screening and saved for future duplicate checks.";
            return new ScoreReport(message, highestRisk, duplicate, matchingId);
        } catch (Exception e) {
            return new ScoreReport("Submission recorded for this session, but could not be saved: " + e.getMessage(), highestRisk, duplicate, matchingId);
        }
    }
}
