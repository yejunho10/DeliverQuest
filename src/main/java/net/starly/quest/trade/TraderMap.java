package net.starly.quest.trade;

import net.starly.quest.destination.Destination;
import net.starly.quest.destination.repo.DestinationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraderMap {

    private static TraderMap instance;

    public static TraderMap getInstance() {
        if (instance == null) instance = new TraderMap();
        return instance;
    }

    private TraderMap() {}


    private final Map<String, Destination> cache = new HashMap<>();

    public void fetch() {
        DestinationRepository destinationRepo = DestinationRepository.getInstance();

        clear();
        List<Destination> destinations = destinationRepo.getAllDestination();
        destinations.forEach(destination -> cache.put(destination.getTrader().getTraderName(), destination));
    }

    public Destination getDestination(String npcName) {
        return cache.get(npcName);
    }

    public void clear() {
        cache.clear();
    }
}
