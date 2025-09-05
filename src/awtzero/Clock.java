package awtzero;

import java.util.*;
import java.time.*;

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

    public static void schedule(Runnable task, float delaySeconds) {
        tasks.add(new ScheduledTask(task, (long)(delaySeconds * 1000), false, 0));
    }

    public static void scheduleInterval(Runnable task, float intervalSeconds) {
        long intervalMillis = (long)(intervalSeconds * 1000);
        tasks.add(new ScheduledTask(task, intervalMillis, true, intervalMillis));
    }

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