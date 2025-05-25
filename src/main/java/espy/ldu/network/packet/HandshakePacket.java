package espy.ldu.network.packet;

import espy.ldu.LocalDifficultyUtilities;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record HandshakePacket() implements CustomPayload {
    public static final Identifier HANDSHAKE = Identifier.of(LocalDifficultyUtilities.MOD_ID, "handshake");
    public static final Id<HandshakePacket> HANDSHAKE_ID = new Id<>(HANDSHAKE);

    public static final PacketCodec<PacketByteBuf, HandshakePacket> HANDSHAKE_CODEC = PacketCodec.unit(new HandshakePacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return HANDSHAKE_ID;
    }
}