package net.starly.quest.command.tabcomplete;

import net.starly.quest.YDDailyQuestMain;
import net.starly.quest.destination.Destination;
import net.starly.quest.destination.repo.DestinationRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeliverTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.isOp()) {
                completions.addAll(List.of("초기화", "생성", "설정"));
            }
        } else if (args.length == 2) {
            switch (args[0]) {
                case "초기화" -> {
                    completions.add("<플레이어>");
                    completions.addAll(
                            YDDailyQuestMain.getInstance()
                                    .getServer()
                                    .getOnlinePlayers()
                                    .stream()
                                    .map(Player::getDisplayName)
                                    .toList()
                    );
                }

                case "생성" -> {
                    completions.add("<이름>");
                }

                case "설정" -> {
                    completions.add("<이름>");
                    completions.addAll(
                            DestinationRepository.getInstance()
                                    .getAllDestination()
                                    .stream()
                                    .map(Destination::getName)
                                    .toList()
                    );
                }
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
