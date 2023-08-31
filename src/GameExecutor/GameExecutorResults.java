package GameExecutor;

public record GameExecutorResults(int totalGain, int totalRounds) {
    public void print(){
        System.out.println("Total Social Welfare (SW): " + totalGain);
        System.out.println("Total number of rounds: " + totalRounds);
    }
}
