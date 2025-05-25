package espy.ldu.network;

import espy.ldu.LocalDifficultyUtilities;
import espy.ldu.network.packet.ChunkSyncResponsePacket;
import espy.ldu.network.packet.HandshakePacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.math.ChunkPos;

import static espy.ldu.network.packet.ChunkSyncRequestPacket.CHUNK_SYNC_REQUEST_ID;
import static espy.ldu.network.packet.ChunkSyncRequestPacket.CHUNK_SYNC_REQUEST_CODEC;

import static espy.ldu.network.packet.ChunkSyncRequestPacket.SyncContext.F3_DEBUG_UPDATE;
import static espy.ldu.network.packet.ChunkSyncRequestPacket.SyncContext.NEW_CHUNK_UPDATE;
import static espy.ldu.network.packet.ChunkSyncResponsePacket.CHUNK_SYNC_RESPONSE_ID;
import static espy.ldu.network.packet.ChunkSyncResponsePacket.CHUNK_SYNC_RESPONSE_CODEC;

import static espy.ldu.network.packet.HandshakePacket.HANDSHAKE_ID;
import static espy.ldu.network.packet.HandshakePacket.HANDSHAKE_CODEC;


public class ServerPacketRegistry {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(HANDSHAKE_ID, HANDSHAKE_CODEC);
        PayloadTypeRegistry.playS2C().register(CHUNK_SYNC_RESPONSE_ID, CHUNK_SYNC_RESPONSE_CODEC);
        PayloadTypeRegistry.playC2S().register(CHUNK_SYNC_REQUEST_ID, CHUNK_SYNC_REQUEST_CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ServerPlayNetworking.send(handler.player, new HandshakePacket()));

        ServerPlayNetworking.registerGlobalReceiver(CHUNK_SYNC_REQUEST_ID, (packet, context) -> {
            if (packet.syncContext() == NEW_CHUNK_UPDATE && LocalDifficultyUtilities.CONFIG.serverConfig.syncInhabitedOnNewChunk) {
                sendChunkSyncResponse(context);
            }

            if (packet.syncContext() == F3_DEBUG_UPDATE && LocalDifficultyUtilities.CONFIG.serverConfig.syncInhabitedOnF3) {
                sendChunkSyncResponse(context);
            }
        });

    }

    private static void sendChunkSyncResponse(ServerPlayNetworking.Context context) {
        ChunkPos chunkPos = context.player().getChunkPos();
        long inhabitedTime = context.player().getServerWorld().getWorldChunk(chunkPos.getStartPos()).getInhabitedTime();

        ChunkSyncResponsePacket response = new ChunkSyncResponsePacket(inhabitedTime);
        ServerPlayNetworking.send(context.player(), response);
    }
}
