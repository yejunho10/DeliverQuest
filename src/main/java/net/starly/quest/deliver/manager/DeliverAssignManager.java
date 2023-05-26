package net.starly.quest.deliver.manager;

import net.starly.quest.destination.Destination;
import net.starly.quest.destination.repo.DestinationRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DeliverAssignManager {

    private static DeliverAssignManager instance;
    public static DeliverAssignManager getInstance() {
        if (instance == null) instance = new DeliverAssignManager();
        return instance;
    }
    private DeliverAssignManager() {}


    private final Map<UUID, Map<Destination, Boolean>> data = new HashMap<>();

    public void putData(UUID uniqueId, Map<Destination, Boolean> data) {
        this.data.put(uniqueId, data);
    }

    @NotNull
    public Map<Destination, Boolean> getData(UUID uniqueId) {
        if (!data.containsKey(uniqueId)) {
            DestinationRepository destinationRepository = DestinationRepository.getInstance();
            List<Destination> destinations = destinationRepository.getAllDestination();

            Map<Destination, Boolean> data = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                Destination picked = pickOne(destinations);
                if (data.containsKey(picked)) {
                    i--;
                    continue;
                }

                data.put(picked, false);
            }

            putData(uniqueId, data);
        }

        return data.get(uniqueId);
    }

    public void setFinished(UUID uniqueId, Destination destination, boolean finished) {
        Map<Destination, Boolean> data = this.data.getOrDefault(uniqueId, new HashMap<>());
        data.put(destination, finished);
        this.data.put(uniqueId, data);
    }

    @Nullable
    public boolean isFinished(UUID uniqueId, Destination destination) {
        return getData(uniqueId).getOrDefault(destination, null);
    }

    public void clear() {
        data.clear();
    }


    private <T> T pickOne(List<T> list) {
        return list.get(new Random().nextInt(list.size() - 1));
    }
}
