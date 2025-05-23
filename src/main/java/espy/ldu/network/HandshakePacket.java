package espy.ldu.network;

import espy.ldu.LocalDifficultyUtilities;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record HandshakePacket() implements CustomPayload {
    public static final Identifier LDU_HANDSHAKE = Identifier.of(LocalDifficultyUtilities.MOD_ID, "handshake");
    public static final Id<HandshakePacket> HANDSHAKE_ID = new Id<>(LDU_HANDSHAKE);

    public static final PacketCodec<net.minecraft.network.PacketByteBuf, HandshakePacket> HANDSHAKE_CODEC = PacketCodec.unit(new HandshakePacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return HANDSHAKE_ID;
    }
}