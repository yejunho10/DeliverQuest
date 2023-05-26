package net.starly.quest.destination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.starly.quest.trade.Trader;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Destination implements Serializable {

    @NotNull private String name;
    @NotNull private String approximateLocation;
    @NotNull private Trader trader;
}
