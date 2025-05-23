package espy.ldu.mixin.client;

import espy.ldu.LocalDifficultyUtilities;
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
        }

        cir.setReturnValue(this.chunkFuture.getNow(null));
    }
}