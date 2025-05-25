package espy.ldu.network.packet;

import espy.ldu.LocalDifficultyUtilities;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;


public record ChunkSyncRequestPacket(int chunkPosX, int chunkPosZ, SyncContext syncContext) implements CustomPayload {
    public static final Identifier CHUNK_SYNC_REQUEST = Identifier.of(LocalDifficultyUtilities.MOD_ID, "chunk_sync_request");
    public static final Id<ChunkSyncRequestPacket> CHUNK_SYNC_REQUEST_ID = new CustomPayload.Id<>(CHUNK_SYNC_REQUEST);

    public enum SyncContext {
        NEW_CHUNK_UPDATE,
        F3_DEBUG_UPDATE
    }

    public static final PacketCodec<RegistryByteBuf, SyncContext> CONTEXT_CODEC =
            new PacketCodec<>() {
                @Override
                public void encode(RegistryByteBuf buf, SyncContext context) {
                    buf.writeEnumConstant(context);
                }

                @Override
                public SyncContext decode(RegistryByteBuf buf) {
                    return buf.readEnumConstant(SyncContext.class);
                }
            };


    public static final PacketCodec<RegistryByteBuf, ChunkSyncRequestPacket> CHUNK_SYNC_REQUEST_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, ChunkSyncRequestPacket::chunkPosX,
                    PacketCodecs.INTEGER, ChunkSyncRequestPacket::chunkPosZ,
                    CONTEXT_CODEC, ChunkSyncRequestPacket::syncContext,
                    ChunkSyncRequestPacket::new
            );


    @Override
    public Id<? extends CustomPayload> getId() {
        return CHUNK_SYNC_REQUEST_ID;
    }
}