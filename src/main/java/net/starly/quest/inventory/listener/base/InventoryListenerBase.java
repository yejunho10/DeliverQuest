package net.starly.quest.inventory.listener.base;

import net.starly.quest.YDDailyQuest;
import net.starly.quest.destination.Destination;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryListenerBase {

    private static final Map<UUID, Listener> listeners = new HashMap<>();


    protected void onClose(InventoryCloseEvent event) {}
    protected void onClick(InventoryClickEvent event) {}
    public void openInventory(Player player, Destination destination) {}


    protected void openInventoryAndRegisterListener(Player player, Inventory inventory) {
        player.openInventory(inventory);
        Listener listener = registerInventoryClickEvent(player.getUniqueId());
        listeners.put(player.getUniqueId(), listener);
        registerInventoryCloseEvent(player.getUniqueId());
    }

    private void registerInventoryCloseEvent(UUID uuid) {
        Server server = YDDailyQuest.getInstance().getServer();

        Listener closeEventListener = new Listener() {};
        server.getPluginManager().registerEvent(InventoryCloseEvent.class, closeEventListener, EventPriority.LOWEST, (listeners, event) -> {
            if (!(event instanceof InventoryCloseEvent closeEvent)) return;

            Player player = (Player) closeEvent.getPlayer();
            if (uuid.equals(player.getUniqueId())) {
                Listener listener = InventoryListenerBase.listeners.remove(uuid);
                if (listener != null) {
                    InventoryClickEvent.getHandlerList().unregister(listener);
                }
                InventoryCloseEvent.getHandlerList().unregister(closeEventListener);

                onClose(closeEvent);
            }
        }, YDDailyQuest.getInstance());
    }

    private Listener registerInventoryClickEvent(UUID uuid) {
        Server server = YDDailyQuest.getInstance().getServer();
        Listener listener = new Listener() {};
        server.getPluginManager().registerEvent(InventoryClickEvent.class, listener, EventPriority.LOWEST, (listeners, event) -> {
            if (!(event instanceof InventoryClickEvent clickEvent)) return;

            Player player = (Player) clickEvent.getWhoClicked();
            if (uuid.equals(player.getUniqueId())) {
                onClick(clickEvent);
            }
        }, YDDailyQuest.getInstance());

        return listener;
    }
}
