package espy.ldu.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                registerCommands(dispatcher));
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> lduRoot = CommandManager.literal("ldu")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("inhabited")
                        .then(ChunkCommands.buildChunk())
                        .then(ChunkCommands.buildRegion())
                        .then(CommandManager.literal("cancel").executes(InhabitedTimeTask::cancel))
                )

                .build();

        dispatcher.getRoot().addChild(lduRoot);
        dispatcher.getRoot().addChild(CommandManager.literal("localDifficulty").redirect(lduRoot).build());
    }
}
