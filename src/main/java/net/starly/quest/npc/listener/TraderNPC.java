package net.starly.quest.npc.listener;

import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.trade.TraderMap;
import net.starly.quest.destination.Destination;
import net.starly.quest.npc.listener.base.NPCListenerBase;
import net.starly.quest.trade.strategy.TradeStrategyBase;
import org.bukkit.entity.Player;

public class TraderNPC extends NPCListenerBase {

    private static TraderNPC instance;
    public static TraderNPC getInstance() {
        if (instance == null) instance = new TraderNPC();
        return instance;
    }
    private TraderNPC() {}


    @Getter @Setter private TradeStrategyBase tradeServiceProvider;

    @Override
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        Destination destination = TraderMap.getInstance().getDestination(npc.getName());

        tradeServiceProvider.execute(player, npc, destination);
    }
}
