package net.starly.quest.scheduler;

import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.deliver.manager.DeliverAssignManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class DeliverQuestInitializeScheduler extends BukkitRunnable {

    private static BukkitRunnable instance = null;

    public static void start() {
        final LocalDateTime now = LocalDateTime.now();
        final int TICK_PER_DAY = 24 * 60 * 60 * 20;
        final int TICK_PER_SECOND = 20;
        final int TICK_PER_MINUTE = 60 * TICK_PER_SECOND;
        final int TICK_PER_HOUR = 60 * TICK_PER_MINUTE;
        final int DELAY = ((24 - now.getHour()) * TICK_PER_HOUR) + ((60 - now.getMinute()) * TICK_PER_MINUTE) + ((60 - now.getSecond()) * TICK_PER_SECOND);

        // ※ 24시 0분 0초에 실행할 시, 그 즉시는 실행안됨 ※
        instance = new DeliverQuestInitializeScheduler();
        instance.runTaskTimerAsynchronously(YDDailyQuestMain.getInstance(), DELAY, TICK_PER_DAY);
    }

    public static void stop() {
        if (instance != null) instance.cancel();
    }


    @Override
    public void run() {
        DeliverAssignManager.getInstance().clear();
    }
}
