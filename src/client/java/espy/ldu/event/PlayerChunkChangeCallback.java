package espy.ldu.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.ChunkPos;

public interface PlayerChunkChangeCallback {
    Event<PlayerChunkChangeCallback> EVENT = EventFactory.createArrayBacked(PlayerChunkChangeCallback.class,
            (listeners) -> (player, from, to) -> {
                for (PlayerChunkChangeCallback listener : listeners) {
                    listener.onChunkChange(player, from, to);
                }
            });

    void onChunkChange(ClientPlayerEntity player, ChunkPos from, ChunkPos to);
}