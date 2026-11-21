import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

// Unit 1: Abstract Class
public abstract class SimilarityEngine {
    
    // Unit 3: StringTokenizer
    protected Set<String> tokenize(String text) {
        Set<String> tokens = new HashSet<>();
        if (text == null) return tokens;
        
        StringTokenizer st = new StringTokenizer(text.toLowerCase(), " ,.!?\n\t");
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens;
    }

    public abstract double computeSimilarity(String text1, String text2);
}