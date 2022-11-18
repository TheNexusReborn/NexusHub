package com.thenexusreborn.hub.cmds;

import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetSpawnCmd implements CommandExecutor {
    
    private final NexusHub plugin;
    
    public SetSpawnCmd(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Rank senderRank = MCUtils.getSenderRank(plugin.getNexusCore(), sender);
        if (senderRank.ordinal() > Rank.ADMIN.ordinal()) {
            sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players can use that command."));
            return true;
        }
    
        plugin.setSpawn(player.getLocation());
        player.sendMessage(MCUtils.color("&eYou set the spawn to your current location."));
        return true;
    }
}
