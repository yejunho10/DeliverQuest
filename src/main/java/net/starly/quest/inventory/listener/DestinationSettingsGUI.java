package net.starly.quest.inventory.listener;

import net.starly.core.builder.ItemBuilder;
import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.destination.Destination;
import net.starly.quest.destination.repo.DestinationRepository;
import net.starly.quest.dispatcher.ChatInputDispatcher;
import net.starly.quest.inventory.holder.DeliverySettingsGUI;
import net.starly.quest.inventory.listener.base.InventoryListenerBase;
import net.starly.quest.message.MessageContext;
import net.starly.quest.message.enums.MessageType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DestinationSettingsGUI extends InventoryListenerBase {

    private static DestinationSettingsGUI instance;
    public static DestinationSettingsGUI getInstance() {
        if (instance == null) instance = new DestinationSettingsGUI();
        return instance;
    }
    private DestinationSettingsGUI() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == player.getInventory()) return;

        Destination destination = ((DeliverySettingsGUI) event.getClickedInventory().getHolder()).destination();

        switch (event.getSlot()) {
            case 11 -> {
                MessageContext.getInstance().get(MessageType.NORMAL, "enterApproximateLocation").send(player);
                player.closeInventory();

                ChatInputDispatcher.attachConsumer(player.getUniqueId(), (chatEvent) -> {
                    String message = chatEvent.getMessage();

                    DestinationRepository destinationRepository = DestinationRepository.getInstance();
                    destinationRepository.getDestination(destination.getName()).setApproximateLocation(message);

                    MessageContext.getInstance().get(MessageType.NORMAL, "approximateLocationSet").send(player);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            DestinationSettingsGUI.getInstance().openInventory(chatEvent.getPlayer(), destination);
                        }
                    }.runTask(YDDailyQuestMain.getInstance());
                });
            }

            case 13 -> {
                TradeRequiredItemGUI.getInstance().openInventory(player, destination);
            }

            case 15 -> {
                TradeRewardItemGUI.getInstance().openInventory(player, destination);
            }
        }
    }

    @Override
    public void openInventory(Player player, Destination destination) {
        Inventory inventory = YDDailyQuestMain.getInstance().getServer().createInventory(new DeliverySettingsGUI(destination), 27, "배달 설정 [" + destination.getName() + "]");

        ItemStack emptySlot = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&r").build();
        fillItem(inventory, emptySlot, 0, 9);
        fillItem(inventory, emptySlot, 17, 26);

        ItemStack changeApproximateLocation = new ItemBuilder(Material.MAP).setDisplayName("&6위치 힌트 설정하기").build();
        inventory.setItem(11, changeApproximateLocation);

        ItemStack changeRequireItems = new ItemBuilder(Material.STICK).setDisplayName("&6배달 물품 설정하기").build();
        inventory.setItem(13, changeRequireItems);

        ItemStack changeRewardItems = new ItemBuilder(Material.EMERALD).setDisplayName("&6보상 아이템 설정하기").build();
        inventory.setItem(15, changeRewardItems);


        openInventoryAndRegisterListener(player, inventory);
    }


    private void fillItem(Inventory inventory, ItemStack itemStack, int startIndex, int lastIndex) {
        for (int slot = startIndex; slot <= lastIndex; slot++) {
            inventory.setItem(slot, itemStack);
        }
    }
}
