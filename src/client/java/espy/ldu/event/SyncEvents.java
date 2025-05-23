package espy.ldu.event;

import espy.ldu.LocalDifficultyUtilitiesClient;
import espy.ldu.network.ChunkSyncManager;
import espy.ldu.network.ChunkSyncRequestPacket;
import espy.ldu.network.SyncedChunkData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class SyncEvents implements ClientTickEvents.EndTick {
    private int tickCounter = 0;

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (MinecraftClient.getInstance().isInSingleplayer()) return;
        if (!LocalDifficultyUtilitiesClient.serverHasMod) return;

        tickCounter++;
        if (tickCounter >= 40) {
            tickCounter = 0;

            String dimensionKey = String.valueOf(client.player.clientWorld.getRegistryKey().getValue());
            WorldChunk currentWorldChunk = client.player.clientWorld.getWorldChunk(client.player.getBlockPos());

            SyncedChunkData current = new SyncedChunkData(
                    currentWorldChunk.getPos().x,
                    currentWorldChunk.getPos().z,
                    dimensionKey,
                    currentWorldChunk.getInhabitedTime()
            );

            ChunkSyncManager.chunksToSync.add(current);

            if (!ChunkSyncManager.chunksToSync.isEmpty()) {
                List<SyncedChunkData> toSend = new ArrayList<>(ChunkSyncManager.chunksToSync);
                ClientPlayNetworking.send(new ChunkSyncRequestPacket(toSend));
                ChunkSyncManager.chunksToSync.clear();
            }
        }
    }
}
