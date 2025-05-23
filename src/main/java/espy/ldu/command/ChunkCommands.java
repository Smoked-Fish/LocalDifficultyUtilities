package espy.ldu.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

public class ChunkCommands {
    public static LiteralArgumentBuilder<ServerCommandSource> buildChunk() {
        return CommandManager.literal("chunk")
        .then(CommandManager.literal("get")
        .then(CommandManager.argument("target", EntityArgumentType.player())
        .executes(ctx -> {
            ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "target");
            WorldChunk chunk = target.getServerWorld().getWorldChunk(target.getBlockPos());

            ctx.getSource().sendMessage(Text.literal("The inhabited time is " + chunk.getInhabitedTime() + " for chunk at " + chunk.getPos()));
            return 1;
        }))
        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
        .executes(ctx -> {
            BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
            ServerWorld world = ctx.getSource().getWorld();
            WorldChunk chunk = world.getWorldChunk(pos);

            ctx.getSource().sendMessage(Text.literal("The inhabited time is " + chunk.getInhabitedTime() + " for chunk at " + chunk.getPos()));
            return 1;
        })))

        .then(CommandManager.literal("set")
        .then(CommandManager.argument("target", EntityArgumentType.player())
        .then(CommandManager.argument("time", TimeArgumentType.time())
        .executes(ctx -> {
            ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "target");
            int time = IntegerArgumentType.getInteger(ctx, "time");
            WorldChunk chunk = target.getServerWorld().getWorldChunk(target.getBlockPos());

            chunk.setInhabitedTime(time);
            chunk.setNeedsSaving(true);
            ctx.getSource().sendMessage(Text.literal("Set inhabited time to " + time + " for chunk at " + chunk.getPos()));
            return 1;
        })))
        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
        .then(CommandManager.argument("time", TimeArgumentType.time())
        .executes(ctx -> {
            BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
            int time = IntegerArgumentType.getInteger(ctx, "time");
            ServerWorld world = ctx.getSource().getWorld();
            WorldChunk chunk = world.getWorldChunk(pos);

            chunk.setInhabitedTime(time);
            chunk.setNeedsSaving(true);
            ctx.getSource().sendMessage(Text.literal("Set inhabited time to " + time + " for chunk at " + chunk.getPos()));
            return 1;
        }))));
    }

    public static LiteralArgumentBuilder<ServerCommandSource> buildRegion() {
        return CommandManager.literal("region")
        .then(CommandManager.argument("x1", IntegerArgumentType.integer())
        .then(CommandManager.argument("z1", IntegerArgumentType.integer())
        .then(CommandManager.argument("x2", IntegerArgumentType.integer())
        .then(CommandManager.argument("z2", IntegerArgumentType.integer())
        .then(CommandManager.argument("dimension", DimensionArgumentType.dimension())
        .then(CommandManager.argument("time", TimeArgumentType.time())
        .executes(InhabitedTimeTask::run)))))));
    }
}
