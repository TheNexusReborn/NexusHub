package com.thenexusreborn.hub.listener;

import com.thenexusreborn.api.util.NetworkType;
import com.thenexusreborn.hub.server.HubInstanceServer;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.server.HubVirtualServer;
import com.thenexusreborn.nexuscore.api.events.NexusServerSetupEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ServerListener implements Listener {
    
    private NexusHub plugin;

    public ServerListener(NexusHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerSetup(NexusServerSetupEvent e) {
        if (e.getNetworkType() == NetworkType.MULTI) {
            e.setServer(new HubInstanceServer("Hub")); //TODO Grab name from somewhere
            return;
        }

        Plugin survivalGames = plugin.getServer().getPluginManager().getPlugin("SurvivalGames");
        if (survivalGames == null) {
            e.setServer(new HubInstanceServer("Hub"));
        } else {
            e.addVirtualServer(new HubVirtualServer("Hub"));
        }
    }
}