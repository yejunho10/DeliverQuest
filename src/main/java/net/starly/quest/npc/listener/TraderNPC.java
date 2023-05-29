package net.starly.quest.npc.listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.YDDailyQuest;
import net.starly.quest.event.TradeRequestEvent;
import net.starly.quest.trade.TraderMap;
import net.starly.quest.destination.Destination;
import net.starly.quest.npc.listener.base.NPCListenerBase;
import net.starly.quest.trade.executor.TradeExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class TraderNPC extends NPCListenerBase {

    private static TraderNPC instance;

    public static TraderNPC getInstance() {
        if (instance == null) instance = new TraderNPC();
        return instance;
    }

    private TraderNPC() {}


    @Override
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        Destination destination = TraderMap.getInstance().getDestination(npc.getName());
        TradeRequestEvent tradeRequestEvent = new TradeRequestEvent(player, npc, destination);

        PluginManager pluginManager = YDDailyQuest.getInstance().getServer().getPluginManager();
        pluginManager.callEvent(tradeRequestEvent);
        if (tradeRequestEvent.isCancelled()) return;

        TradeExecutor tradeExecutor = tradeRequestEvent.getExecutor();
        tradeExecutor.execute(player, npc, destination);
    }
}
