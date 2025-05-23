package espy.ldu.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;


public record SyncedChunkData(int chunkPosX, int chunkPosZ, String dimensionKey, long inhabitedTime) {
    public static final PacketCodec<RegistryByteBuf, SyncedChunkData> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SyncedChunkData::chunkPosX,
            PacketCodecs.INTEGER, SyncedChunkData::chunkPosZ,
            PacketCodecs.STRING, SyncedChunkData::dimensionKey,
            PacketCodecs.VAR_LONG, SyncedChunkData::inhabitedTime,
            SyncedChunkData::new
    );
}
