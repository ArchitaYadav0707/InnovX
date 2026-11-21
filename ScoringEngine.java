public class ScoringEngine {
    // Unit 2: Jagged Array with example
    private double[][] historicalWeights = {
        {0.5, 0.5},                 
        {0.4, 0.4, 0.2},            
        {0.3, 0.3, 0.2, 0.2}        
    };

    public double calculateScore(double novelty, double feasibility, double impact) {
        double[] currentWeights = historicalWeights[1]; 
        return (novelty * currentWeights[0]) + (feasibility * currentWeights[1]) + (impact * currentWeights[2]);
    }
}