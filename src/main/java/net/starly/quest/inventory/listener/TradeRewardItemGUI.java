package net.starly.quest.inventory.listener;

import net.starly.core.util.collection.STList;
import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.destination.Destination;
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

public class TradeRewardItemGUI extends InventoryListenerBase {

    private static TradeRewardItemGUI instance;
    public static TradeRewardItemGUI getInstance() {
        if (instance == null) instance = new TradeRewardItemGUI();
        return instance;
    }
    private TradeRewardItemGUI() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().isOp()) {
            event.setCancelled(true);
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        if (!event.getPlayer().isOp()) return;

        Destination destination = ((DeliverySettingsGUI) event.getInventory().getHolder()).destination();
        List<ItemStack> requiredItems = Arrays.stream(event.getInventory().getContents()).filter(Objects::nonNull).toList();
        destination.getTrader().setRewards(requiredItems);

        new BukkitRunnable() {

            @Override
            public void run() {
                DestinationSettingsGUI.getInstance().openInventory((Player) event.getPlayer(), destination);
            }
        }.runTaskLater(YDDailyQuestMain.getInstance(), 1L);
    }

    @Override
    public void openInventory(Player player, Destination destination) {
        Inventory inventory = YDDailyQuestMain.getInstance().getServer().createInventory(new DeliverySettingsGUI(destination), 36, "보상 [" + destination.getName() + "]");

        List<ItemStack> rewards = destination.getTrader().getRewards();
        for (int i = 0; i < rewards.size(); i++) {
            inventory.setItem(i, rewards.get(i));
        }

        openInventoryAndRegisterListener(player, inventory);
    }
}
