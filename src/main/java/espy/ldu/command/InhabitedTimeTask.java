package espy.ldu.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import espy.ldu.LocalDifficultyUtilities;
import espy.ldu.events.TaskEvents;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;

public class InhabitedTimeTask {

    public static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        int x1 = IntegerArgumentType.getInteger(ctx, "x1");
        int z1 = IntegerArgumentType.getInteger(ctx, "z1");
        int x2 = IntegerArgumentType.getInteger(ctx, "x2");
        int z2 = IntegerArgumentType.getInteger(ctx, "z2");
        ServerWorld world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
        int time = IntegerArgumentType.getInteger(ctx, "time");

        ServerCommandSource source = ctx.getSource();
        UUID playerId = Objects.requireNonNull(source.getPlayer()).getUuid();

        int minChunkX = Math.min(x1, x2) >> 4;
        int maxChunkX = Math.max(x1, x2) >> 4;
        int minChunkZ = Math.min(z1, z2) >> 4;
        int maxChunkZ = Math.max(z1, z2) >> 4;

        int chunkWidth = maxChunkX - minChunkX + 1;
        int chunkHeight = maxChunkZ - minChunkZ + 1;
        int totalChunks = chunkWidth * chunkHeight;

        if (totalChunks > LocalDifficultyUtilities.CONFIG.serverConfig.regionMaxChunks) {
            source.sendError(Text.literal("Too many chunks selected (" + totalChunks + "). Please select a smaller area."));
            return 0;
        }

        Set<ChunkPos> chunks = new HashSet<>();
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                WorldChunk chunk = world.getChunkManager().getWorldChunk(cx, cz, false);
                if (chunk != null && !chunk.isEmpty()) {
                    chunks.add(new ChunkPos(cx, cz));
                }
            }
        }

        LocalDifficultyUtilities.LOGGER.info("Total Chunks Selected: {}", chunks.size());

        TaskEvents.startTask(playerId, source, world, new ArrayList<>(chunks), time);
        return 1;
    }

    public static int cancel(CommandContext<ServerCommandSource> ctx) {
        UUID playerId = Objects.requireNonNull(ctx.getSource().getPlayer()).getUuid();
        return TaskEvents.cancel(playerId, ctx.getSource()) ? 1 : 0;
    }
}
