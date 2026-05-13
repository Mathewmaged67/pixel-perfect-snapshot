package lifexp.threads;

/**
 * Thread pool for managing background tasks in the application.
 * Handles workout timers, leaderboard updates, and other async operations.
 */
public class BackgroundTaskPool {
    private static final int POOL_SIZE = 5;
    private java.util.concurrent.ExecutorService executorService;

    public BackgroundTaskPool() {
        this.executorService = java.util.concurrent.Executors.newFixedThreadPool(POOL_SIZE);
    }

    /**
     * Submit a task to the pool
     */
    public java.util.concurrent.Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    /**
     * Submit a callable task to the pool
     */
    public <T> java.util.concurrent.Future<T> submit(java.util.concurrent.Callable<T> task) {
        return executorService.submit(task);
    }

    /**
     * Schedule a task with delay
     */
    public void scheduleWithDelay(Runnable task, long delayMs) {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, delayMs);
    }

    /**
     * Schedule a recurring task
     */
    public void scheduleAtFixedRate(Runnable task, long periodMs) {
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, 0, periodMs);
    }

    /**
     * Shutdown the pool
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Check if pool is shutdown
     */
    public boolean isShutdown() {
        return executorService.isShutdown();
    }
}
