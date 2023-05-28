package net.starly.quest.destination.repo;

import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.destination.Destination;
import net.starly.quest.npc.listener.TraderNPC;
import net.starly.quest.util.EncodeUtil;
import net.starly.quest.trade.TraderMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DestinationRepository {

    private static DestinationRepository instance;

    public static DestinationRepository getInstance() {
        if (instance == null) instance = new DestinationRepository();
        return instance;
    }

    private DestinationRepository() {}


    private static final File DATA_CONFIG = new File(YDDailyQuestMain.getInstance().getDataFolder(), "data.yml");
    private final Map<String, Destination> data = new HashMap<>();

    public void putDestination(Destination destination) {
        data.put(destination.getName(), destination);
        TraderNPC.getInstance().registerNPCRightClickEvent(destination);
        TraderMap.getInstance().fetch();
    }

    public Destination getDestination(String name) {
        return data.get(name);
    }

    public List<Destination> getAllDestination() {
        return new ArrayList<>(data.values());
    }

    public void clear() {
        data.clear();
    }


    @Deprecated
    public void loadAll() {
        clear();

        TraderNPC traderNPC = TraderNPC.getInstance();
        FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_CONFIG);
        ConfigurationSection section = config.getConfigurationSection("destination");

        section.getKeys(false).forEach(name -> {
            Destination destination = EncodeUtil.decode((byte[]) section.get(name), Destination.class);
            traderNPC.registerNPCRightClickEvent(destination);
            this.data.put(destination.getName(), destination);
        });

        try {
            config.save(DATA_CONFIG);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        TraderMap.getInstance().fetch();
    }

    public void saveAll() {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(DATA_CONFIG);
            ConfigurationSection section = config.createSection("destination");
            getAllDestination().forEach(destination -> section.set(destination.getName(), EncodeUtil.encode(destination)));

            config.save(DATA_CONFIG);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
