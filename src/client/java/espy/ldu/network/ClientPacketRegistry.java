package espy.ldu.network;

import espy.ldu.LocalDifficultyUtilitiesClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.chunk.Chunk;

import static espy.ldu.network.packet.ChunkSyncResponsePacket.CHUNK_SYNC_RESPONSE_ID;
import static espy.ldu.network.packet.HandshakePacket.HANDSHAKE_ID;

public class ClientPacketRegistry {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(HANDSHAKE_ID, (payload, context) ->
                MinecraftClient.getInstance().execute(() -> LocalDifficultyUtilitiesClient.serverHasMod = true));

        ClientPlayNetworking.registerGlobalReceiver(CHUNK_SYNC_RESPONSE_ID, (packet, context) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            assert client.player != null;
            assert client.world != null;

            Chunk chunk = client.world.getChunk(client.player.getBlockPos());
            chunk.setInhabitedTime(packet.inhabitedTime());
            chunk.setNeedsSaving(true);
        });
    }
}
