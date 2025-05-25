package espy.ldu.network.packet;

import espy.ldu.LocalDifficultyUtilities;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChunkSyncResponsePacket(long inhabitedTime) implements CustomPayload {
    public static final Identifier CHUNK_SYNC_RESPONSE = Identifier.of(LocalDifficultyUtilities.MOD_ID, "chunk_sync_response");
    public static final Id<ChunkSyncResponsePacket> CHUNK_SYNC_RESPONSE_ID = new CustomPayload.Id<>(CHUNK_SYNC_RESPONSE);

    public static final PacketCodec<RegistryByteBuf, ChunkSyncResponsePacket> CHUNK_SYNC_RESPONSE_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_LONG, ChunkSyncResponsePacket::inhabitedTime,
                    ChunkSyncResponsePacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return CHUNK_SYNC_RESPONSE_ID;
    }
}