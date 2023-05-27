package net.starly.quest.message;

import net.starly.core.util.PreCondition;
import net.starly.quest.message.enums.MessageType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Objects;

public class MessageLoader {

    private static boolean loaded = false;

    public static void load(FileConfiguration config) {
        if (loaded) {
            MessageContext.getInstance().clear();
            loaded = false;
        }

        Arrays.stream(MessageType.values()).forEach(type -> loadMessageSection(Objects.requireNonNull(config.getConfigurationSection(type.getPath())), type));

        loaded = true;
    }

    private static void loadMessageSection(ConfigurationSection section, MessageType type) {
        PreCondition.nonNull(section, "메세지를 로드할 수 없습니다. : " + type.name());

        MessageContext msgContext = MessageContext.getInstance();
        section.getKeys(true).forEach(key -> {
            msgContext.set(type, key, section.isList(key) ? String.join("\n&r{prefix}", section.getStringList(key)) : section.getString(key));
        });
    }
}