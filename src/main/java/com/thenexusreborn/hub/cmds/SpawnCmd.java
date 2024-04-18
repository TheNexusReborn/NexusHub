package com.thenexusreborn.hub.cmds;

import com.stardevllc.starcore.color.ColorUtils;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {
    private NexusHub plugin;
    
    public SpawnCmd(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.color("&cOnly players can use that command."));
            return true;
        }
    
        player.teleport(plugin.getSpawn());
        player.sendMessage(ColorUtils.color(MsgType.INFO + "Teleported to the spawn."));
        return true;
    }
}
