package ikhwan.hanif.alienshooter;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    private String userId;
    private long score;

    public LeaderboardEntry(String userId, long score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public long getScore() {
        return score;
    }

    @Override
    public int compareTo(LeaderboardEntry other) {
        return Long.compare(this.score, other.score);
    }
}

