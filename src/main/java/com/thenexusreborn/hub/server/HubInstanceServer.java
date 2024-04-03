package com.thenexusreborn.hub.server;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.server.InstanceServer;
import com.thenexusreborn.hub.NexusHub;

@SuppressWarnings("DuplicatedCode")
public class HubInstanceServer extends InstanceServer {
    private NexusHub plugin;
    
    public HubInstanceServer(NexusHub plugin, String name) {
        super(name, "hub", 100);
        this.plugin = plugin;
        
        this.primaryVirtualServer = new HubVirtualServer(plugin, this, name + "1");
    }

    @Override
    public void join(NexusPlayer nexusPlayer) {
        this.primaryVirtualServer.join(nexusPlayer);
    }

    @Override
    public void quit(NexusPlayer nexusPlayer) {
        this.primaryVirtualServer.quit(nexusPlayer);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
