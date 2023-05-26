package net.starly.quest.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Trader implements Serializable {

    @NotNull private List<ItemStack> requirements;
    @NotNull private List<ItemStack> rewards;
    @NotNull private String traderName;
}
