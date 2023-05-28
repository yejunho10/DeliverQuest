package net.starly.quest.trade.executor;

import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.destination.Destination;
import org.bukkit.entity.Player;

public interface TradeExecutor {

    void execute(Player player, NPC npc, Destination destination);
}