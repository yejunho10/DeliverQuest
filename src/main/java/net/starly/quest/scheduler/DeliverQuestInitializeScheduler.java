package net.starly.quest.scheduler;

import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.deliver.manager.DeliverAssignManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.*;

public class DeliverQuestInitializeScheduler extends BukkitRunnable {

    private static BukkitRunnable instance = null;

    public static void start() {
        LocalTime now = LocalTime.now();
        long passedSecondOfDay = now.toSecondOfDay();
        final long SECONDS_OF_DAY = 24 * 60 * 60;
        final long TICK_PER_DAY = SECONDS_OF_DAY * 20;
        final long DELAY = (SECONDS_OF_DAY - passedSecondOfDay) * 20L;

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
