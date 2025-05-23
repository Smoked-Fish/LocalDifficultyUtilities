package espy.ldu.network;

import espy.ldu.LocalDifficultyUtilities;
import espy.ldu.LocalDifficultyUtilitiesClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import static espy.ldu.network.ChunkSyncResponsePacket.RESPONSE_ID;
import static espy.ldu.network.HandshakePacket.HANDSHAKE_ID;

public class ClientPacketRegistry {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(HANDSHAKE_ID, (payload, context) ->
                MinecraftClient.getInstance().execute(() -> LocalDifficultyUtilitiesClient.serverHasMod = true));

        ClientPlayNetworking.registerGlobalReceiver(RESPONSE_ID, (packet, context) -> {
            if (MinecraftClient.getInstance().isInSingleplayer()) return;
            if (!LocalDifficultyUtilitiesClient.serverHasMod) return;

            for (SyncedChunkData chunkData : packet.chunkList()) {
                LocalDifficultyUtilities.LOGGER.info(
                        "Inhabited Time [{},{}]: {}",
                        chunkData.chunkPosX(),
                        chunkData.chunkPosZ(),
                        chunkData.inhabitedTime()
                );

                int blockPosX = chunkData.chunkPosX() << 4;
                int blockPosZ = chunkData.chunkPosZ() << 4;
                BlockPos packetBlockPos = new BlockPos(blockPosX, 0, blockPosZ);

                context.player().getWorld().getWorldChunk(packetBlockPos).setInhabitedTime(chunkData.inhabitedTime());
                context.player().getWorld().getWorldChunk(packetBlockPos).setNeedsSaving(true);
            }
        });
    }
}
