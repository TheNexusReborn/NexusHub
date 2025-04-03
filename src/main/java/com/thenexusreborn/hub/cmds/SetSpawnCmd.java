package com.thenexusreborn.hub.cmds;

import com.stardevllc.starcore.StarColors;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.MCUtils;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetSpawnCmd implements CommandExecutor {
    
    private final NexusHub plugin;
    
    public SetSpawnCmd(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Rank senderRank = MCUtils.getSenderRank(sender);
        if (senderRank.ordinal() > Rank.ADMIN.ordinal()) {
            sender.sendMessage(StarColors.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        if (!(sender instanceof Player player)) {
            sender.sendMessage(StarColors.color("&cOnly players can use that command."));
            return true;
        }
        
        if (!player.getWorld().equals(plugin.getHubWorld())) {
            StarColors.coloredMessage(sender, MsgType.WARN + "&cYou are not in the hub world.");
            return true;
        }
    
        plugin.setSpawn(player.getLocation());
        player.sendMessage(StarColors.color(MsgType.INFO + "You set the spawn to your current location."));
        return true;
    }
}
