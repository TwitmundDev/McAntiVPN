package fr.twitmund.mcantivpn.events;

import fr.twitmund.mcantivpn.Main;
import fr.twitmund.mcantivpn.utils.ApiRequester;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerConnectEvent implements Listener {
    static Logger logger = Main.getInstance().getLogger();

    /**
     * kick the player when he uses a vpn
     */
    @EventHandler
    public static void onPlayerConnect(AsyncPlayerPreLoginEvent e) {
        String playerIp = e.getAddress().toString();
        InetAddress playerIpInet = e.getAddress();
        System.out.println(String.valueOf(playerIpInet));
        if (isPlayerAlreadyRegistered(playerIpInet)){
            if (Main.getIpList().get(playerIpInet).equals(false)){
                notifyWhenVPNConnected(playerIp , e.getName());
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER ,Main.getGetKickMessage());
            } else if (Main.getIpList().get(playerIpInet).equals(true)){
            }
        }else {
            try {
                if(ApiRequester.requestApi(playerIp).equals("0")){
                    Main.addIpOnList(String.valueOf(playerIpInet), true);
                } else if (ApiRequester.requestApi(playerIp).equals("1")) {
                    Main.addIpOnList(String.valueOf(playerIpInet), false);
                    notifyWhenVPNConnected(playerIp , e.getName());
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER ,Main.getGetKickMessage());
                }
            }catch (Exception exception){
                logger.log(Level.SEVERE , "error : " + exception);
            }
        }
    }

    /**
     * Get if the player is already registered in the internal list
     * @param address InetAddress
     * @return boolean
     */
    public static boolean isPlayerAlreadyRegistered(InetAddress address){

        if ((Main.getIpList().containsKey(address))) {
            return true;
        }else {
            return  false;
        }
    }

    public static void notifyWhenVPNConnected(String ip , String username){
        String notifyPermission = Main.getPermissionNotify();
        String message = Main.getPlayerWithVPNTriedToConnectMessage().replace("%ip" , ip).replace("%username" , username);
        Bukkit.getOnlinePlayers().forEach(player ->{
            if (player.hasPermission(notifyPermission)){
                player.sendMessage(message);
            }else return;
        });
    }




}
