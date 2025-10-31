package awtzero;

import java.util.*;
import java.time.*;

 /**
 * Provides a simple scheduling mechanism for tasks to be executed after a delay or at regular intervals.
 * Tasks are checked and executed in the update() method, which should be called regularly (e.g., in a game loop).
 * @see #schedule(Runnable, float)
 */

public class Clock {

    private static final List<ScheduledTask> tasks = new ArrayList<>();

    private static class ScheduledTask {
        final Runnable runnable;
        final Instant scheduledTime; 
        final boolean repeating;
        final long intervalMillis;
        Instant nextRun;

        ScheduledTask(Runnable runnable, long delayMillis, boolean repeating, long intervalMillis) {
            this.runnable = runnable;
            this.scheduledTime = Instant.now().plusMillis(delayMillis);
            this.repeating = repeating;
            this.intervalMillis = intervalMillis;
            this.nextRun = scheduledTime;
        }

        boolean shouldRun(Instant now) {
            return !now.isBefore(nextRun);
        }

        void run() {
            runnable.run();
            if (repeating) {
                nextRun = Instant.now().plusMillis(intervalMillis);
            }
        }
    }

    /**
     * Schedules a task to be executed after a specified delay in seconds.
     * @param task the task to be executed
     * @param delaySeconds the delay in seconds before executing the task
     */
    public static void schedule(Runnable task, float delaySeconds) {
        tasks.add(new ScheduledTask(task, (long)(delaySeconds * 1000), false, 0));
    }

    /**
     * Schedules a task to be executed at regular intervals.
     * @param task the task to be executed
     * @param intervalSeconds the interval in seconds between executions
     */
    public static void scheduleInterval(Runnable task, float intervalSeconds) {
        long intervalMillis = (long)(intervalSeconds * 1000);
        tasks.add(new ScheduledTask(task, intervalMillis, true, intervalMillis));
    }

    /**
     * Updates the Clock, checking for and executing any scheduled tasks that are due to run.
     * This method should be called regularly, e.g., in a game loop.
     */
    public static void update() {
        Instant now = Instant.now();
        Iterator<ScheduledTask> iter = tasks.iterator();

        while (iter.hasNext()) {
            ScheduledTask task = iter.next();
            if (task.shouldRun(now)) {
                task.run();
                if (!task.repeating) iter.remove();
            }
        }
    }
}