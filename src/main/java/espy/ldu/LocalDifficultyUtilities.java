package espy.ldu;

import espy.ldu.command.CommandRegistry;
import espy.ldu.config.ModConfig;
import espy.ldu.events.ServerEventRegistry;
import espy.ldu.network.ServerPacketRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDifficultyUtilities implements ModInitializer {
	public static final String MOD_ID = "ldu";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ModConfig CONFIG;

	@Override
	public void onInitialize() {
		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		CommandRegistry.register();
		ServerPacketRegistry.register();
		ServerEventRegistry.register();





	}
}