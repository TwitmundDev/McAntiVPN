package fr.twitmund.mcantivpn.commands;

import fr.twitmund.mcantivpn.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;

public class ShowIpList implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;


        if (arg.equalsIgnoreCase("showIpList")){
            AtomicReference<ChatColor> color = new AtomicReference<>(ChatColor.WHITE);
            if (!player.hasPermission(Main.getPermissionCommand())){
                player.sendMessage(Main.getPermissionMessage());
                return false;
            }
            StringBuilder sb = new StringBuilder();
            Main.getIpList().forEach((k,v) -> {
                if (v.equals(false)) color.set(ChatColor.RED);
                if (v.equals(true)) color.set(ChatColor.GREEN);


                sb.append("ip : ").append(k.toString()).append(" authorised : " +color).append(v);
            });
            player.sendMessage(String.valueOf(sb));

        }

        return false;
    }
}
