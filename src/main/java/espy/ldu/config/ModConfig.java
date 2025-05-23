package espy.ldu.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ldu")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public Client clientConfig = new Client();

    @ConfigEntry.Gui.CollapsibleObject
    public Server serverConfig = new Server();


    public static class Client {
        public boolean restoreLocalDifficultly = true;
    }

    public static class Server {
        public boolean syncInhabited = true;
        public int regionMaxChunks = 250001;
        public int regionBatchSize = 100;
    }

}

