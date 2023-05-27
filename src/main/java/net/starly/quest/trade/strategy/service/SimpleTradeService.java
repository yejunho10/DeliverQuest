package net.starly.quest.trade.strategy.service;

import net.citizensnpcs.api.npc.NPC;
import net.starly.core.util.InventoryUtil;
import net.starly.quest.deliver.manager.DeliverAssignManager;
import net.starly.quest.destination.Destination;
import net.starly.quest.message.MessageContext;
import net.starly.quest.message.enums.MessageType;
import net.starly.quest.trade.Trader;
import net.starly.quest.trade.strategy.TradeServiceBase;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SimpleTradeService implements TradeServiceBase {

    @Override
    public void execute(Player player, NPC npc, Destination destination) {
        MessageContext messageContext = MessageContext.getInstance();
        DeliverAssignManager assignManager = DeliverAssignManager.getInstance();
        if (!assignManager.getData(player.getUniqueId()).containsKey(destination)) {
            messageContext.get(MessageType.ERROR, "cannotTradeThisNPC").send(player);
            return;
        } else if (assignManager.isFinished(player.getUniqueId(), destination)) {
            messageContext.get(MessageType.ERROR, "alreadyClaimedReward").send(player);
            return;
        }


        Trader trader = destination.getTrader();

        boolean hasRequirements = true;
        Inventory inventory = player.getInventory();
        for (ItemStack requireItem : trader.getRequirements()) {
            if (!hasRequirements) continue;

            requireItem = requireItem.clone();
            requireItem.setAmount(1);
            if (!inventory.containsAtLeast(requireItem, requireItem.getAmount())) {
                hasRequirements = false;
            }
        }

        if (hasRequirements) {
            trader.getRequirements().forEach(requireItem -> InventoryUtil.removeItem(player, requireItem, requireItem.getAmount()));
            trader.getRewards().forEach(rewardItem -> {
                ItemStack[] beforeContents = Arrays.copyOf(inventory.getContents(), inventory.getSize());
                inventory.addItem(rewardItem);
                if (Arrays.equals(beforeContents, inventory.getContents())) {
                    player.getWorld().dropItem(player.getLocation(), rewardItem);
                }
            });

            messageContext.get(MessageType.NORMAL, "tradeSuccess").send(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 1f);
            assignManager.setFinished(player.getUniqueId(), destination, true);
        } else {
            messageContext.get(MessageType.ERROR, "noRequireItems").send(player);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
        }
    }
}
