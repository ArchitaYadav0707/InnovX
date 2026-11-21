import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

// Unit 4: Client-side connection
public class SubmissionClient {
    public ScoreReport sendIdea(Idea idea) {
        Object response = send(idea);
        return response instanceof ScoreReport ? (ScoreReport) response : new ScoreReport("Invalid server response", -1);
    }

    @SuppressWarnings("unchecked")
    public List<Idea> fetchSubmittedIdeas() {
        Object response = send("ADMIN_LIST");
        return response instanceof List ? (List<Idea>) response : Collections.emptyList();
    }

    private Object send(Object request) {
        try (Socket socket = new Socket("localhost", 8080)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            out.flush();
            
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch (Exception e) {
            return new ScoreReport("Error connecting to server: " + e.getMessage(), -1);
        }
    }
}