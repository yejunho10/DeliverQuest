package net.starly.quest.dispatcher;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInputDispatcher implements Listener {

    private static final Map<UUID, Consumer<AsyncPlayerChatEvent>> consumers = new HashMap<>();

    public static void attachConsumer(UUID uniqueId, Consumer<AsyncPlayerChatEvent> callback) {
        consumers.put(uniqueId, callback);
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        dispatch(event);
    }

    private void dispatch(AsyncPlayerChatEvent event) {
        Consumer<AsyncPlayerChatEvent> callback = consumers.remove(event.getPlayer().getUniqueId());
        if (callback != null) {
            event.setCancelled(true);
            callback.accept(event);
        }
    }
}
