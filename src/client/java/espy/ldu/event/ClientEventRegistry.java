package espy.ldu.event;

import espy.ldu.network.packet.ChunkSyncRequestPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import static espy.ldu.network.packet.ChunkSyncRequestPacket.SyncContext.NEW_CHUNK_UPDATE;

public class ClientEventRegistry {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ChunkChangeEvent::onEndClientTick);

        PlayerChunkChangeCallback.EVENT.register((player, from, to) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            DebugHud debugHud = client.inGameHud.getDebugHud();
            if (debugHud.shouldShowDebugHud()) return;

            World world = player.getWorld();
            Chunk chunk = world.getChunk(to.x, to.z);

            ClientPlayNetworking.send(new ChunkSyncRequestPacket(chunk.getPos().x, chunk.getPos().z, NEW_CHUNK_UPDATE));
        });
    }
}