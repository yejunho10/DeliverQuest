package net.starly.quest.event;

import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.destination.Destination;
import net.starly.quest.trade.executor.TradeExecutor;
import net.starly.quest.trade.executor.impl.SimpleTradeService;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TradeRequestEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final Player player;
    @Getter private final NPC npc;
    @Getter private final Destination destination;
    @Getter @Setter private TradeExecutor executor;
    private boolean cancel;


    public TradeRequestEvent(Player player, NPC npc, Destination destination) {
        this.player = player;
        this.npc = npc;
        this.destination = destination;
        this.cancel = false;
        this.executor = new SimpleTradeService();
    }


    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
