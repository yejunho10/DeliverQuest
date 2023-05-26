package net.starly.quest.event;

import lombok.Getter;
import net.starly.quest.destination.Destination;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DeliverFinishEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter private final Destination destination;
    @Getter private final Player player;
    private boolean cancel;


    public DeliverFinishEvent(Player player, Destination destination) {
        this.player = player;
        this.destination = destination;
        this.cancel = false;
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
