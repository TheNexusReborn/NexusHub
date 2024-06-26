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
            HubInstanceServer hub = new HubInstanceServer(plugin, "Hub");
            plugin.setHubServer(hub);
            e.setServer(hub);
            return;
        }

        Plugin survivalGames = plugin.getServer().getPluginManager().getPlugin("SurvivalGames");
        if (survivalGames == null) {
            HubInstanceServer hub = new HubInstanceServer(plugin, "Hub");
            plugin.setHubServer(hub);
            e.setServer(hub);
        } else {
            HubVirtualServer hubServer = new HubVirtualServer(plugin, "Hub");
            plugin.setHubServer(hubServer);
            e.addVirtualServer(hubServer);
            e.setPrimaryVirtualServer(hubServer);
        }
    }
}