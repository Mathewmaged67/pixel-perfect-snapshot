package lifexp.threads;

import lifexp.models.LeaderboardEntry;
import java.util.List;

/**
 * Background task for refreshing leaderboard data at intervals.
 * Periodically updates leaderboard rankings and stats.
 */
public class LeaderboardRefreshTask implements Runnable {
    private volatile boolean running;
    private static final long REFRESH_INTERVAL = 30000; // 30 seconds
    private LeaderboardRefreshListener listener;

    public LeaderboardRefreshTask(LeaderboardRefreshListener listener) {
        this.listener = listener;
        this.running = true;
    }

    @Override
    public void run() {
        try {
            while (running) {
                if (listener != null) {
                    listener.onLeaderboardRefresh();
                }
                Thread.sleep(REFRESH_INTERVAL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stop the refresh task
     */
    public void stop() {
        running = false;
    }

    /**
     * Listener interface for leaderboard updates
     */
    public interface LeaderboardRefreshListener {
        void onLeaderboardRefresh();
    }
}
