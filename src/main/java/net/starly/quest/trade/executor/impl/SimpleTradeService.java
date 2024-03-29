package net.starly.quest.trade.executor.impl;

import net.citizensnpcs.api.npc.NPC;
import net.starly.core.util.InventoryUtil;
import net.starly.quest.deliver.manager.DeliverAssignManager;
import net.starly.quest.destination.Destination;
import net.starly.quest.message.MessageContext;
import net.starly.quest.message.enums.MessageType;
import net.starly.quest.trade.Trader;
import net.starly.quest.trade.executor.TradeExecutor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SimpleTradeService implements TradeExecutor {

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

            if (!inventory.containsAtLeast(requireItem, requireItem.getAmount())) {
                hasRequirements = false;
            }
        }

        if (hasRequirements) {
            trader.getRequirements().forEach(requireItem -> InventoryUtil.removeItem(player, requireItem, requireItem.getAmount()));
            trader.getRewards().forEach(rewardItem -> {
                if (InventoryUtil.getSpace(inventory) - 5 < 1) {
                    player.getWorld().dropItem(player.getLocation(), rewardItem);
                } else inventory.addItem(rewardItem);
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
