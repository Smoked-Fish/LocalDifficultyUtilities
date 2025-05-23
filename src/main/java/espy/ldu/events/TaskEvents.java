package espy.ldu.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;

import static espy.ldu.LocalDifficultyUtilities.CONFIG;

public class TaskEvents implements ServerTickEvents.EndTick {
    private static final Map<UUID, TaskState> activeTasks = new HashMap<>();

    public static void startTask(UUID playerId, ServerCommandSource source, ServerWorld world, List<ChunkPos> chunks, int time) {
        activeTasks.remove(playerId);

        source.sendMessage(Text.literal("Starting inhabited time update for " + chunks.size() + " chunks..."));
        activeTasks.put(playerId, new TaskState(source, world, chunks, time));
    }

    public static boolean cancel(UUID playerId, ServerCommandSource source) {
        if (activeTasks.containsKey(playerId)) {
            activeTasks.remove(playerId);
            source.sendMessage(Text.literal("Inhabited time update cancelled."));
            return true;
        } else {
            source.sendMessage(Text.literal("No active inhabited time update to cancel."));
            return false;
        }
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        for (Iterator<TaskState> it = activeTasks.values().iterator(); it.hasNext();) {
            TaskState task = it.next();
            task.tick();
            if (task.isComplete()) {
                it.remove();
            }
        }
    }

    static class TaskState {
        private final ServerCommandSource source;
        private final ServerWorld world;
        private final List<ChunkPos> chunks;
        private final int time;
        private int index = 0;
        private int updated = 0;

        TaskState(ServerCommandSource source, ServerWorld world, List<ChunkPos> chunks, int time) {
            this.source = source;
            this.world = world;
            this.chunks = chunks;
            this.time = time;
        }

        void tick() {
            int batchSize = CONFIG.serverConfig.regionBatchSize;

            for (int i = 0; i < batchSize && index < chunks.size(); i++, index++) {
                ChunkPos pos = chunks.get(index);
                WorldChunk chunk = world.getChunkManager().getWorldChunk(pos.x, pos.z, false);
                if (chunk != null) {
                    chunk.setInhabitedTime(time);
                    chunk.markNeedsSaving();
                    updated++;
                }
            }

            int percent = (int)((index / (float)chunks.size()) * 100);
            source.sendMessage(Text.literal("Progress: " + percent + "% (" + updated + "/" + chunks.size() + ")"));

            if (isComplete()) {
                source.sendMessage(Text.literal("Done! Updated inhabited time for " + updated + " chunks."));
            }
        }

        boolean isComplete() {
            return index >= chunks.size();
        }
    }
}