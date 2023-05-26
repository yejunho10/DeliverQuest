package net.starly.quest.inventory.listener;

import net.starly.core.util.collection.STList;
import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.destination.Destination;
import net.starly.quest.destination.repo.DestinationRepository;
import net.starly.quest.inventory.holder.DeliverySettingsGUI;
import net.starly.quest.inventory.listener.base.InventoryListenerBase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TradeRequiredItemGUI extends InventoryListenerBase {

    private static TradeRequiredItemGUI instance;
    public static TradeRequiredItemGUI getInstance() {
        if (instance == null) instance = new TradeRequiredItemGUI();
        return instance;
    }
    private TradeRequiredItemGUI() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().isOp()) {
            event.setCancelled(true);
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        Destination destination = ((DeliverySettingsGUI) event.getInventory().getHolder()).destination();
        List<ItemStack> requiredItems = Arrays.stream(event.getInventory().getContents()).filter(Objects::nonNull).toList();
        destination.getTrader().setRequirements(requiredItems);

        new BukkitRunnable() {

            @Override
            public void run() {
                DestinationSettingsGUI.getInstance().openInventory((Player) event.getPlayer(), destination);
            }
        }.runTaskLater(YDDailyQuestMain.getInstance(), 1L);
    }

    @Override
    public void openInventory(Player player, Destination destination) {
        Inventory inventory = YDDailyQuestMain.getInstance().getServer().createInventory(new DeliverySettingsGUI(destination), 36, "배달 물품 [" + destination.getName() + "]");

        List<ItemStack> requirements = destination.getTrader().getRequirements();
        for (int i = 0; i < requirements.size(); i++) {
            inventory.setItem(i, requirements.get(i));
        }

        openInventoryAndRegisterListener(player, inventory);
    }
}
