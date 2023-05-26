package net.starly.quest.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.quest.YDDailyQuestMain;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class STMessage {

    @Getter private final String prefix;
    @Getter private final String message;

    public String getText() {
        return prefix + message;
    }

    public void send(CommandSender target) {
        if (message.isEmpty()) return;
        target.sendMessage(prefix + message);
    }

    public void broadcast() {
        if (message.isEmpty()) return;
        YDDailyQuestMain.getInstance().getServer().broadcastMessage(message);
    }
}
