package net.starly.quest.message;

import lombok.Getter;
import net.starly.quest.YDDailyQuestMain;
import org.bukkit.command.CommandSender;

public record STMessage(@Getter String prefix, @Getter String message) {

    public void send(CommandSender target) {
        if (message.isEmpty()) return;
        target.sendMessage(prefix + message);
    }

    public void broadcast() {
        if (message.isEmpty()) return;
        YDDailyQuestMain.getInstance().getServer().broadcastMessage(message);
    }
}
