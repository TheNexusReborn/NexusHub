package com.thenexusreborn.hub.cmds;

import com.stardevllc.starcore.color.ColorHandler;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {
    private NexusHub plugin;
    
    public SpawnCmd(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorHandler.getInstance().color("&cOnly players can use that command."));
            return true;
        }
        
        if (!plugin.getHubServer().getPlayers().contains(player.getUniqueId())) {
            sender.sendMessage(ColorHandler.getInstance().color("&cYou cannot use that command right now"));
            return true;
        }

        player.teleport(plugin.getSpawn());
        player.sendMessage(ColorHandler.getInstance().color(MsgType.INFO + "Teleported to the spawn."));
        return true;
    }
}
