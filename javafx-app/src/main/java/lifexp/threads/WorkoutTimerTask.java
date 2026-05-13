package lifexp.threads;

/**
 * Runnable task for executing workout timer operations in background.
 * Tracks workout duration and updates progress.
 */
public class WorkoutTimerTask implements Runnable {
    private String workoutId;
    private long startTime;
    private volatile boolean running;
    private WorkoutTimerListener listener;
    private static final long UPDATE_INTERVAL = 1000; // 1 second

    public WorkoutTimerTask(String workoutId, WorkoutTimerListener listener) {
        this.workoutId = workoutId;
        this.listener = listener;
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    @Override
    public void run() {
        try {
            while (running) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long elapsedSeconds = elapsedTime / 1000;
                
                if (listener != null) {
                    listener.onTimerUpdate(workoutId, elapsedSeconds);
                }
                
                Thread.sleep(UPDATE_INTERVAL);
            }
            
            if (listener != null) {
                listener.onTimerStop(workoutId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stop the timer task
     */
    public void stop() {
        running = false;
    }

    /**
     * Get elapsed seconds
     */
    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    /**
     * Listener interface for timer updates
     */
    public interface WorkoutTimerListener {
        void onTimerUpdate(String workoutId, long elapsedSeconds);
        void onTimerStop(String workoutId);
    }
}
