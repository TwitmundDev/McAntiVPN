package fr.twitmund.mcantivpn.manager;

import fr.twitmund.mcantivpn.Main;
import fr.twitmund.mcantivpn.events.PlayerConnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    public static void RegisterEvents(){
        PluginManager pm =  Bukkit.getServer().getPluginManager();

        pm.registerEvents(new PlayerConnectEvent(),Main.getInstance());
    }

}
