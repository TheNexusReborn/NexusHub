package com.thenexusreborn.hub.cmds;

import com.stardevllc.starcore.api.StarColors;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {
    private NexusHub plugin;
    
    public SpawnCmd(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(StarColors.color("&cOnly players can use that command."));
            return true;
        }
        
        if (!plugin.getHubServer().getPlayers().contains(player.getUniqueId())) {
            sender.sendMessage(StarColors.color("&cYou cannot use that command right now"));
            return true;
        }

        player.teleport(plugin.getSpawn());
        player.sendMessage(StarColors.color(MsgType.INFO + "Teleported to the spawn."));
        return true;
    }
}
