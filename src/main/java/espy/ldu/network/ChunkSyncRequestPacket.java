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


public record ChunkSyncRequestPacket(List<SyncedChunkData> chunkList) implements CustomPayload {
    public static final Identifier CHUNK_SYNC_REQUEST = Identifier.of(LocalDifficultyUtilities.MOD_ID, "chunk_sync_request");
    public static final CustomPayload.Id<ChunkSyncRequestPacket> REQUEST_ID = new CustomPayload.Id<>(CHUNK_SYNC_REQUEST);

    public static final PacketCodec<RegistryByteBuf, ChunkSyncRequestPacket> REQUEST_CODEC =
            PacketCodecs.collection((IntFunction<List<SyncedChunkData>>) ArrayList::new, SyncedChunkData.CODEC)
                    .xmap(ChunkSyncRequestPacket::new, ChunkSyncRequestPacket::chunkList);


    @Override
    public Id<? extends CustomPayload> getId() {
        return REQUEST_ID;
    }
}
