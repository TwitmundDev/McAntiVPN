package fr.twitmund.mcAntiVPN;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.twitmund.mcAntiVPN.commands.ShowIpList;
import fr.twitmund.mcAntiVPN.events.PlayerConnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private final Path pathOfJson = this.getDataFolder().toPath().resolve("savedIps.json");
    private static final Logger log = Logger.getLogger("mcAntiVPN");
    private static final HashMap<String, Boolean> ipList = new HashMap<>();

    private static String API_KEY;
    private static String getKickMessage;
    private static String permissionCommand;
    private static String dontHavePermissionMessage;
    private static String permissionNotify;
    private static String playerWithVPNTriedToConnectMessage;


    private static Main instance;


    @Override
    public void onEnable() {


        instance = this;
        log.log(Level.WARNING, """
                If its your first load of the plugin please please add your api key and reload the plugin or restart the server
                """);
        saveDefaultConfig();
        API_KEY = this.getConfig().getString("API_KEY");
        getKickMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("KickMessage", "error"));
        permissionCommand = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("CommandPermission", "error"));
        permissionNotify = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("PermissionNotify", "error"));
        dontHavePermissionMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("DontHavePermissionMessage", "error"));
        playerWithVPNTriedToConnectMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("PlayerWithVPNTriedToConnect", "error"));

        if (Files.exists(pathOfJson)) {
            try {
                JsonObject obj = JsonParser.parseReader(Files.newBufferedReader(pathOfJson)).getAsJsonObject();

                for (String key : obj.keySet()) {
                    Boolean keyValue = obj.get(key).getAsBoolean();
                    ipList.put(key, keyValue);
                }
                log.log(Level.INFO, "File successfully read and written into the hashmap");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PlayerConnectEvent(), this);

        log.info("Plugin enabled");
        getCommand("showIpList").setExecutor(new ShowIpList());
    }

    @Override
    public void onDisable() {
        JsonObject savingAllIps = new JsonObject();
        ipList.forEach(savingAllIps::addProperty);
        try {
            Files.writeString(pathOfJson, savingAllIps.toString(), StandardCharsets.UTF_8, StandardOpenOption.WRITE);
            log.log(Level.INFO, "The file has been wiritten and saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Main getInstance() {
        return instance;
    }

    public static String getGetKickMessage() {
        return getKickMessage;
    }

    public static HashMap<String, Boolean> getIpList() {
        return ipList;
    }

    public static String getAPI_KEY() {
        return API_KEY;
    }

    public static String getPermissionCommand() {
        return permissionCommand;
    }

    public static String getPermissionMessage() {
        return dontHavePermissionMessage;
    }

    public static String getPermissionNotify() {
        return permissionNotify;
    }

    public static String getPlayerWithVPNTriedToConnectMessage() {
        return playerWithVPNTriedToConnectMessage;
    }

    public static void addIpOnList(String address, Boolean value) {
        ipList.put(address, value);
    }
}
