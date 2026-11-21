// Unit 4: Runnable Class
public class SearchTask implements Runnable {
    private TextEntity targetEntity;
    private String query;
    private SimilarityEngine engine;

    public SearchTask(TextEntity targetEntity, String query, SimilarityEngine engine) {
        this.targetEntity = targetEntity;
        this.query = query;
        this.engine = engine;
    }

    @Override
    public void run() {
        // Unit 4: Synchronizing Threads
        synchronized (engine) {
            double score = engine.computeSimilarity(query, targetEntity.getTextContent());
            if (score > 0.7) {
                // Unit 3: StringBuffer / StringBuilder
                StringBuilder sb = new StringBuilder();
                sb.append("Thread: ").append(Thread.currentThread().getName())
                  .append(" found match in ID ").append(targetEntity.getId())
                  .append(" with score: ").append(score);
                System.out.println(sb.toString());
            }
        }
    }
}