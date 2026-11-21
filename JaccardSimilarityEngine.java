import java.util.HashSet;
import java.util.Set;

// Unit 2: Inheritance & Overriding
public class JaccardSimilarityEngine extends SimilarityEngine {

    @Override
    public double computeSimilarity(String text1, String text2) {
        Set<String> set1 = tokenize(text1);
        Set<String> set2 = tokenize(text2);
        
        if (set1.isEmpty() && set2.isEmpty()) return 1.0;
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }
}