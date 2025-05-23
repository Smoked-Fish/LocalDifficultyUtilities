package espy.ldu.network;

import espy.ldu.LocalDifficultyUtilities;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static espy.ldu.network.ChunkSyncRequestPacket.REQUEST_CODEC;
import static espy.ldu.network.ChunkSyncRequestPacket.REQUEST_ID;
import static espy.ldu.network.ChunkSyncResponsePacket.RESPONSE_CODEC;
import static espy.ldu.network.ChunkSyncResponsePacket.RESPONSE_ID;
import static espy.ldu.network.HandshakePacket.HANDSHAKE_CODEC;
import static espy.ldu.network.HandshakePacket.HANDSHAKE_ID;

public class ServerPacketRegistry {
    public static void register() {
        if (!LocalDifficultyUtilities.CONFIG.serverConfig.syncInhabited) return;

        PayloadTypeRegistry.playS2C().register(HANDSHAKE_ID, HANDSHAKE_CODEC);
        PayloadTypeRegistry.playS2C().register(RESPONSE_ID, RESPONSE_CODEC);
        PayloadTypeRegistry.playC2S().register(REQUEST_ID, REQUEST_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(REQUEST_ID, (packet, context) -> {
            ServerPlayerEntity player = context.player();
            List<SyncedChunkData> updatedChunks = new ArrayList<>();

            for (SyncedChunkData chunkData : packet.chunkList()) {
                ServerWorld world = getServerWorldFromKey(chunkData.dimensionKey(), player);

                if (world == null) {
                    LocalDifficultyUtilities.LOGGER.error("Failed to resolve world for dimensionKey: {}", chunkData.dimensionKey());
                    continue;
                }

                int blockPosX = chunkData.chunkPosX() << 4;
                int blockPosZ = chunkData.chunkPosZ() << 4;
                BlockPos packetBlockPos = new BlockPos(blockPosX, 0, blockPosZ);

                long inhabitedTime = world.getWorldChunk(packetBlockPos).getInhabitedTime();

                if (chunkData.inhabitedTime() == inhabitedTime) continue;

                SyncedChunkData updatedData = new SyncedChunkData(
                        chunkData.chunkPosX(),
                        chunkData.chunkPosZ(),
                        chunkData.dimensionKey(),
                        inhabitedTime
                );

                updatedChunks.add(updatedData);
            }

            if (!updatedChunks.isEmpty()) {
                ChunkSyncResponsePacket response = new ChunkSyncResponsePacket(updatedChunks);
                ServerPlayNetworking.send(player, response);
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(handler.player, new HandshakePacket());
        });
    }

    private static ServerWorld getServerWorldFromKey(String key, ServerPlayerEntity player) {
        Identifier dimensionId = Identifier.tryParse(key);
        if (dimensionId == null) return null;

        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, dimensionId);

        if (player.getServer() != null) {
            return player.getServer().getWorld(worldKey);
        }

        return null;
    }
}
