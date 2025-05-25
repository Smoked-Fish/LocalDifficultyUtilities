package espy.ldu.mixin.client;

import espy.ldu.LocalDifficultyUtilities;
import espy.ldu.network.packet.ChunkSyncRequestPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

import static espy.ldu.network.packet.ChunkSyncRequestPacket.SyncContext.F3_DEBUG_UPDATE;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Unique private final MinecraftClient client = MinecraftClient.getInstance();
    @Shadow private CompletableFuture<WorldChunk> chunkFuture;

    @Inject(method = "getChunk", at = @At("HEAD"), cancellable = true)
    private void onGetChunk(CallbackInfoReturnable<WorldChunk> cir) {
        if (client.isInSingleplayer()) return;
        if (!LocalDifficultyUtilities.CONFIG.clientConfig.restoreLocalDifficultly) return;

        if (client.player == null || client.player.getWorld() == null) {
            cir.setReturnValue(null);
        }

        if (this.chunkFuture == null) {
            World world = client.player.getWorld();
            BlockPos playerPos = client.player.getBlockPos();
            WorldChunk chunk = world.getWorldChunk(playerPos);
            this.chunkFuture = CompletableFuture.completedFuture(chunk);

            ClientPlayNetworking.send(new ChunkSyncRequestPacket(chunk.getPos().x, chunk.getPos().z, F3_DEBUG_UPDATE));
        }

        cir.setReturnValue(this.chunkFuture.getNow(null));
    }
}