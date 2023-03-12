package fr.twitmund.mcantivpn;

import fr.twitmund.mcantivpn.commands.ShowIpList;
import fr.twitmund.mcantivpn.manager.EventManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static String apiKey;
    private String pathOfJson =  this.getDataFolder().getAbsolutePath()+"/savedIps.json";
    private static final Logger log = Logger.getLogger("MaliseAntiVpn");

    private static final HashMap<String,Boolean> ipList = new HashMap<>();

    private static String API_KEY;
    private static String getKickMessage;
    private static String PermissionCommand;
    private static String dontHavePermissionMessage;
    private static String PermissionNotify;
    private static String PlayerWithVPNTriedToConnectMessage;





    private static Main instance;


    @Override
    public void onEnable() {
        instance = this;
        log.log(Level.WARNING, """
                If its your first load of the plugin please please add your api key and reload the plugin or restart the server
                """);
        saveDefaultConfig();
        API_KEY = this.getConfig().getString("API_KEY").replace("&", "§");
        getKickMessage = this.getConfig().getString("KickMessage").replace("&", "§");
        PermissionCommand = this.getConfig().getString("CommandPermission").replace("&", "§");
        PermissionNotify = this.getConfig().getString("PermissionNotify").replace("&", "§");
        dontHavePermissionMessage = this.getConfig().getString("DontHavePermissionMessage").replace("&", "§");
        PlayerWithVPNTriedToConnectMessage = this.getConfig().getString("PlayerWithVPNTriedToConnect").replace("&", "§");
        apiKey = this.getConfig().getString("API_KEY");

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(pathOfJson));
            JSONObject jsonObject = (JSONObject) obj;

            for (Object key : jsonObject.keySet()) {
                String keyStr = (String) key;
                Boolean keyvalue = (Boolean) jsonObject.get(keyStr);
                ipList.put(keyStr, keyvalue);
            }
            log.log(Level.INFO, "File successfully read and written into the hashmap");
        } catch (Exception e) {
            if (e.getCause() instanceof FileNotFoundException ){
                log.log(Level.SEVERE, "The File Does not exist if its your first load its normal");
            }
            e.printStackTrace();
        }


        EventManager.RegisterEvents();
        log.info("Plugin enabled");
        log.info(this.getDataFolder().getAbsolutePath());
        log.info(pathOfJson);
        getCommand("showIpList").setExecutor(new ShowIpList());



    }

    @Override
    public void onDisable() {
        JSONObject savingAllIps = new JSONObject();
        ipList.forEach((k,v) ->{
            savingAllIps.put(k, v);
        });
        try {
            FileWriter file = new FileWriter(pathOfJson);
            file.write(savingAllIps.toJSONString());
            file.close();
            log.log(Level.INFO,"The file has been wiritten and saved");
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
        return PermissionCommand;
    }
    public static String getPermissionMessage() {
        return dontHavePermissionMessage;
    }
    public static String getPermissionNotify() {
        return PermissionNotify;
    }

    public static String getPlayerWithVPNTriedToConnectMessage() {
        return PlayerWithVPNTriedToConnectMessage;
    }

    public static void addIpOnList(String address , Boolean value) {
        ipList.put(address, value);
    }
}
