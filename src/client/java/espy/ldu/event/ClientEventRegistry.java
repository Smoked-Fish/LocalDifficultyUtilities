package espy.ldu.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientEventRegistry {
    public static void register() {
        ClientChunkEvents.CHUNK_LOAD.register(ClientChunkListener::onChunkLoad);
        ClientTickEvents.END_CLIENT_TICK.register(new SyncEvents());
    }
}