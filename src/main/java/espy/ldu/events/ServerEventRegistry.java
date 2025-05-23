package espy.ldu.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ServerEventRegistry {
    public static void  register() {
        ServerTickEvents.END_SERVER_TICK.register(new TaskEvents());
    }
}
