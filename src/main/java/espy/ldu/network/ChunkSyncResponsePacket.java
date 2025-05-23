package espy.ldu.network;

import espy.ldu.LocalDifficultyUtilities;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public record ChunkSyncResponsePacket(List<SyncedChunkData> chunkList) implements CustomPayload {
    public static final Identifier CHUNK_SYNC_RESPONSE = Identifier.of(LocalDifficultyUtilities.MOD_ID, "chunk_sync_response");
    public static final CustomPayload.Id<ChunkSyncResponsePacket> RESPONSE_ID = new CustomPayload.Id<>(CHUNK_SYNC_RESPONSE);

    public static final PacketCodec<RegistryByteBuf, ChunkSyncResponsePacket> RESPONSE_CODEC =
            PacketCodecs.collection((IntFunction<List<SyncedChunkData>>) ArrayList::new, SyncedChunkData.CODEC)
                    .xmap(ChunkSyncResponsePacket::new, ChunkSyncResponsePacket::chunkList);

    @Override
    public Id<? extends CustomPayload> getId() {
        return RESPONSE_ID;
    }
}