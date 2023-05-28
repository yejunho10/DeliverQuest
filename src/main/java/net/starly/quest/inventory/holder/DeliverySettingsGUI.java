package net.starly.quest.inventory.holder;

import net.starly.quest.destination.Destination;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

public record DeliverySettingsGUI(Destination destination) implements InventoryHolder {

    @Override
    public @Nullable Inventory getInventory() {
        return null;
    }
}
