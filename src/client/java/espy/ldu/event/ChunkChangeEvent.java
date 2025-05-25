package espy.ldu.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

public class ChunkChangeEvent {
    private static ChunkPos playerChunkCache = null;

    public static void onEndClientTick(MinecraftClient client) {
        if (client.isInSingleplayer()) return;
        if (client.player == null) return;

        ChunkPos currentChunk = new ChunkPos(client.player.getBlockPos());
        ChunkPos lastChunk = playerChunkCache;

        if (lastChunk == null || !lastChunk.equals(currentChunk)) {
            if (lastChunk != null) {
                PlayerChunkChangeCallback.EVENT.invoker().onChunkChange(client.player, lastChunk, currentChunk);
            }
            playerChunkCache = currentChunk;
        }
    }
}
