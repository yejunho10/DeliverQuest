package net.starly.quest.inventory.listener;

import net.starly.core.builder.ItemBuilder;
import net.starly.core.data.Config;
import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.deliver.manager.DeliverAssignManager;
import net.starly.quest.destination.Destination;
import net.starly.quest.inventory.listener.base.InventoryListenerBase;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeliverStatusGUI extends InventoryListenerBase {

    private static DeliverStatusGUI instance;
    public static DeliverStatusGUI getInstance() {
        if (instance == null) instance = new DeliverStatusGUI();
        return instance;
    }
    private DeliverStatusGUI() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        FileConfiguration config = YDDailyQuestMain.getInstance().getConfig();

        DeliverAssignManager assignManager = DeliverAssignManager.getInstance();
        Map<Destination, Boolean> data = assignManager.getData(event.getWhoClicked().getUniqueId());
        List<Destination> deliverQuests = new ArrayList<>(data.keySet());

        int questNumber;
        if (event.getSlot() == config.getInt("gui.status.slots.first")) questNumber = 1;
        else if (event.getSlot() == config.getInt("gui.status.slots.second")) questNumber = 2;
        else if (event.getSlot() == config.getInt("gui.status.slots.third")) questNumber = 3;
        else return;

        ClickType clickType = event.getClick();
        if (clickType.isLeftClick()) {
            TradeRequiredItemGUI.getInstance().openInventory((Player) event.getWhoClicked(), deliverQuests.get(questNumber - 1));
        } else if (clickType.isRightClick()) {
            TradeRewardItemGUI.getInstance().openInventory((Player) event.getWhoClicked(), deliverQuests.get(questNumber - 1));
        }
    }

    @Override
    public void openInventory(Player player, @Nullable Destination destination) {
        FileConfiguration config = YDDailyQuestMain.getInstance().getConfig();

        DeliverAssignManager assignManager = DeliverAssignManager.getInstance();
        Map<Destination, Boolean> data = assignManager.getData(player.getUniqueId());
        List<Destination> deliverQuests = new ArrayList<>(data.keySet());


        Inventory inventory = new Config("config", YDDailyQuestMain.getInstance()).getInventory("gui.status.inventory");
        inventory.setItem(
                config.getInt("gui.status.slots.first"),
                new ItemBuilder(Material.END_CRYSTAL)
                        .setDisplayName("&r" + deliverQuests.get(0).getName())
                        .setLore("§e› §7좌클릭으로 배달 물품을 확인하실 수 있습니다.", "§e› §7우클릭으로 보상을 확인하실 수 있습니다.", "§e› §f위치 : " + deliverQuests.get(0).getApproximateLocation(), "§e› §f클리어 여부 : " + (assignManager.isFinished(player.getUniqueId(), deliverQuests.get(0)) ? "§aO" : "§cX"))
                        .build()
        );
        inventory.setItem(
                config.getInt("gui.status.slots.second"),
                new ItemBuilder(Material.END_CRYSTAL)
                        .setDisplayName("&r" + deliverQuests.get(1).getName())
                        .setLore("§e› §7좌클릭으로 배달 물품을 확인하실 수 있습니다.", "§e› §7우클릭으로 보상을 확인하실 수 있습니다.", "§e› §f위치 : " + deliverQuests.get(1).getApproximateLocation(), "§e› §f클리어 여부 : " + (assignManager.isFinished(player.getUniqueId(), deliverQuests.get(1)) ? "§aO" : "§cX"))
                        .build()
        );
        inventory.setItem(
                config.getInt("gui.status.slots.third"),
                new ItemBuilder(Material.END_CRYSTAL)
                        .setDisplayName("&r" + deliverQuests.get(2).getName())
                        .setLore("§e› §7좌클릭으로 배달 물품을 확인하실 수 있습니다.", "§e› §7우클릭으로 보상을 확인하실 수 있습니다.", "§e› §f위치 : " + deliverQuests.get(2).getApproximateLocation(), "§e› §f클리어 여부 : " + (assignManager.isFinished(player.getUniqueId(), deliverQuests.get(2)) ? "§aO" : "§cX"))
                        .build()
        );


        openInventoryAndRegisterListener(player, inventory);
    }
}
