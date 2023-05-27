package net.starly.quest.trade.strategy;

import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.destination.Destination;
import org.bukkit.entity.Player;

public interface TradeServiceBase {

    void execute(Player player, NPC npc, Destination destination);
}