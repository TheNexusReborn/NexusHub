package com.thenexusreborn.hub.cmds;

import com.stardevllc.colors.StarColors;
import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.server.VirtualServer;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCmd implements CommandExecutor {
    private NexusHub plugin;

    public HubCmd(NexusHub plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(StarColors.color("&cOnly players can use that command."));
            return true;
        }

        NexusPlayer nexusPlayer = NexusAPI.getApi().getPlayerManager().getNexusPlayer(player.getUniqueId());
        
        NexusAPI.getApi().getServerRegistry().forEach(server -> {
            if (server instanceof VirtualServer virtualServer) {
                virtualServer.quit(nexusPlayer);
            }
        });
        
        plugin.getHubServer().join(nexusPlayer);

        player.sendMessage(StarColors.color(MsgType.INFO + "Teleported to the hub."));
        return true;
    }
}