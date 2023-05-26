package net.starly.quest.inventory.holder;

import net.starly.quest.destination.Destination;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public record DeliverySettingsGUI(Destination destination) implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }
}
