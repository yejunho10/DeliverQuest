package net.starly.quest;

import net.starly.quest.command.DeliverCmd;
import net.starly.quest.command.tabcomplete.DeliverTab;
import net.starly.quest.destination.repo.DestinationRepository;
import net.starly.quest.dispatcher.ChatInputDispatcher;
import net.starly.quest.npc.listener.TraderNPC;
import net.starly.quest.scheduler.DeliverQuestInitializeScheduler;
import net.starly.quest.trade.strategy.service.SimpleTradeService;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YDDailyQuestMain extends JavaPlugin {

    private static YDDailyQuestMain instance;
    public static YDDailyQuestMain getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        /* DEPENDENCY
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        if (!isPluginEnabled("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (!isPluginEnabled("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] Citizens 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttps://www.spigotmc.org/resources/citizens.13811/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        instance = this;

        TraderNPC.getInstance().setTradeServiceProvider(new SimpleTradeService());

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        reloadConfig();

        File message = new File(getDataFolder(), "message.yml");
        if (!message.exists()) saveResource("message.yml", false);

        File destinations = new File(getDataFolder(), "data.yml");
        if (!destinations.exists()) saveResource("data.yml", false);

        DestinationRepository.getInstance().$initialize();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        PluginCommand deliverCmd = getServer().getPluginCommand("배달");
        if (deliverCmd != null) {
            deliverCmd.setExecutor(new DeliverCmd());
            deliverCmd.setTabCompleter(new DeliverTab());
        }

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getServer().getPluginManager().registerEvents(new ChatInputDispatcher(), instance);

        /* SCHEDULER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        DeliverQuestInitializeScheduler.start();
    }

    @Override
    public void onDisable() {
        DestinationRepository.getInstance().saveAll();
        DeliverQuestInitializeScheduler.stop();
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}
