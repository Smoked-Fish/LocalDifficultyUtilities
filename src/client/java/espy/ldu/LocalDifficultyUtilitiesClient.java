package espy.ldu;

import espy.ldu.event.ClientEventRegistry;
import espy.ldu.network.ClientPacketRegistry;
import net.fabricmc.api.ClientModInitializer;

public class LocalDifficultyUtilitiesClient implements ClientModInitializer {
	public static boolean serverHasMod = false;

	@Override
	public void onInitializeClient() {
		ClientPacketRegistry.register();
		ClientEventRegistry.register();
	}
}