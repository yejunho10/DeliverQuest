package net.starly.quest;

import lombok.Getter;
import net.starly.quest.command.DeliverExecutor;
import net.starly.quest.destination.repo.DestinationRepository;
import net.starly.quest.dispatcher.ChatInputDispatcher;
import net.starly.quest.message.MessageLoader;
import net.starly.quest.scheduler.DeliverQuestInitializeScheduler;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class YDDailyQuest extends JavaPlugin {

    @Getter private static YDDailyQuest instance;


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

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();

        File messageFile = new File(getDataFolder(), "message.yml");
        if (!messageFile.exists()) saveResource("message.yml", false);

        File dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) saveResource("data.yml", false);

        DestinationRepository.getInstance().loadAll();
        MessageLoader.load(YamlConfiguration.loadConfiguration(messageFile));

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        DeliverExecutor deliverExecutor = new DeliverExecutor();
        PluginCommand deliverCmd = getServer().getPluginCommand("배달");
        if (deliverCmd != null) {
            deliverCmd.setExecutor(deliverExecutor);
            deliverCmd.setTabCompleter(deliverExecutor);
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
