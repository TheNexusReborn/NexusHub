package com.thenexusreborn.hub.server;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.server.InstanceServer;
import com.thenexusreborn.hub.NexusHub;

import java.util.UUID;

@SuppressWarnings("DuplicatedCode")
public class HubInstanceServer extends InstanceServer {
    private NexusHub plugin;
    
    public HubInstanceServer(NexusHub plugin, String name) {
        super(name, "hub", 100);
        this.plugin = plugin;
        
        this.primaryVirtualServer.set(new HubVirtualServer(plugin, this, name + "1"));
    }

    @Override
    public void join(NexusPlayer nexusPlayer) {
        this.primaryVirtualServer.get().join(nexusPlayer);
    }

    @Override
    public void quit(NexusPlayer nexusPlayer) {
        this.primaryVirtualServer.get().quit(nexusPlayer);
    }
    
    @Override
    public void quit(UUID uuid) {
        this.primaryVirtualServer.get().quit(uuid);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
