package net.starly.quest.npc.listener.base;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.destination.Destination;
import org.bukkit.Server;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class NPCListenerBase {

    public static final Map<String, Listener> listeners = new HashMap<>();


    public void onRightClick(NPCRightClickEvent event) {}


    public void registerNPCRightClickEvent(Destination destination) {
        Server server = YDDailyQuestMain.getInstance().getServer();

        Listener closeEventListener = new Listener() {};
        server.getPluginManager().registerEvent(NPCRightClickEvent.class, closeEventListener, EventPriority.LOWEST, (listeners, event) -> {
            if (!(event instanceof NPCRightClickEvent rightClickEvent)) return;

            NPC clickedNPC = rightClickEvent.getNPC();
            if (destination.getTrader().getTraderName().equals(clickedNPC.getName())) {
                Listener listener = NPCListenerBase.listeners.remove(destination.getTrader().getTraderName());
                if (listener != null) {
                    InventoryClickEvent.getHandlerList().unregister(listener);
                }
                InventoryCloseEvent.getHandlerList().unregister(closeEventListener);

                onRightClick(rightClickEvent);
            }
        }, YDDailyQuestMain.getInstance());
    }
}
