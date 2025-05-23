package espy.ldu.event;

import espy.ldu.network.ChunkSyncManager;
import espy.ldu.network.SyncedChunkData;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.WorldChunk;

public class ClientChunkListener {
    public static void onChunkLoad(ClientWorld clientWorld, WorldChunk worldChunk) {
        String dimensionKey = String.valueOf(clientWorld.getRegistryKey().getValue());
        SyncedChunkData packet = new SyncedChunkData(
                worldChunk.getPos().x,
                worldChunk.getPos().z,
                dimensionKey,
                worldChunk.getInhabitedTime()
        );
        ChunkSyncManager.chunksToSync.add(packet);
    }
}