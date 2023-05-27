package net.starly.quest.command;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.deliver.manager.DeliverAssignManager;
import net.starly.quest.destination.Destination;
import net.starly.quest.destination.repo.DestinationRepository;
import net.starly.quest.inventory.listener.DeliverStatusGUI;
import net.starly.quest.inventory.listener.DestinationSettingsGUI;
import net.starly.quest.message.MessageContext;
import net.starly.quest.message.enums.MessageType;
import net.starly.quest.trade.Trader;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeliverCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        MessageContext messageContext = MessageContext.getInstance();
        if (!(sender instanceof Player player)) {
            messageContext.get(MessageType.ERROR, "wrongPlatform").send(sender);
            return true;
        }

        if (args.length == 0) {
            DeliverStatusGUI.getInstance().openInventory(player, null);
            return true;
        } else if (!player.isOp()) {
            messageContext.get(MessageType.ERROR, "noPermission").send(player);
            return false;
        }

        switch (args[0]) {
            case "초기화" -> {
                // argument pre-check
                if (args.length == 1) {
                    messageContext.get(MessageType.ERROR, "noTarget").send(player);
                    return false;
                } else if (args.length > 2) {
                    messageContext.get(MessageType.ERROR, "wrongCommand").send(player);

                    return false;
                }

                // `getOfflinePlayer`로 발생하는 랙을 줄이기 위해 비동기로 실행
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        OfflinePlayer target = YDDailyQuestMain.getInstance().getServer().getOfflinePlayer(args[1]);
                        if (!(target.hasPlayedBefore() || target.isOnline())) {
                            messageContext.get(MessageType.ERROR, "playerNotFound").send(player);
                            return;
                        }

                        DeliverAssignManager assignManager = DeliverAssignManager.getInstance();
                        assignManager.getData(target.getUniqueId()).keySet().forEach(destination -> assignManager.setFinished(player.getUniqueId(), destination, false));
                        messageContext.get(MessageType.NORMAL, "resetFinish").send(player);
                    }
                }.runTaskAsynchronously(YDDailyQuestMain.getInstance());
                return true;
            }

            case "생성" -> {
                // argument pre-check
                if (args.length == 1) {
                    messageContext.get(MessageType.ERROR, "noDestinationName").send(player);
                    return false;
                } else if (args.length > 2) {
                    messageContext.get(MessageType.ERROR, "wrongCommand").send(player);
                    return false;
                }


                NPC selectedNPC = CitizensAPI.getDefaultNPCSelector().getSelected(player);
                if (selectedNPC == null) {
                    messageContext.get(MessageType.ERROR, "noSelectedNPC").send(player);
                    return false;
                }
                String traderName = selectedNPC.getName();
                String destinationName = args[1];

                DestinationRepository destinationRepository = DestinationRepository.getInstance();
                if (destinationRepository.getDestination(destinationName) != null) {
                    messageContext.get(MessageType.ERROR, "nameAlreadyTaken").send(player);
                    return false;
                }

                destinationRepository.putDestination(new Destination(destinationName, "해당 값을 변경해주세요.", new Trader(new ArrayList<>(), new ArrayList<>(), traderName)));
                messageContext.get(MessageType.NORMAL, "destinationCreated").send(player);
                return true;
            }

            case "설정" -> {
                // argument pre-check
                if (args.length == 1) {
                    messageContext.get(MessageType.ERROR, "noDestinationName").send(player);
                    return false;
                } else if (args.length > 2) {
                    messageContext.get(MessageType.ERROR, "wrongCommand").send(player);
                    return false;
                }

                String destinationName = args[1];
                Destination destination = DestinationRepository.getInstance().getDestination(destinationName);
                if (destination == null) {
                    messageContext.get(MessageType.ERROR, "destinationNotExists").send(player);
                    return false;
                }

                DestinationSettingsGUI.getInstance().openInventory(player, destination);
                return true;
            }

            default -> {
                messageContext.get(MessageType.ERROR, "wrongCommand").send(player);
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.isOp()) completions.addAll(List.of("초기화", "생성", "설정"));
        } else if (args.length == 2) {
            switch (args[0]) {
                case "초기화" -> {
                    completions.add("<플레이어>");
                    List<Player> onlinePlayers = new ArrayList<>(YDDailyQuestMain.getInstance().getServer().getOnlinePlayers());
                    completions.addAll(onlinePlayers
                            .stream()
                            .map(Player::getDisplayName)
                            .toList());
                }

                case "생성" -> {
                    completions.add("<이름>");
                }

                case "설정" -> {
                    completions.add("<이름>");
                    List<Destination> destinations = DestinationRepository.getInstance().getAllDestination();
                    completions.addAll(destinations
                            .stream()
                            .map(Destination::getName)
                            .toList());
                }
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
