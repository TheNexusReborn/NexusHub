package com.thenexusreborn.hub.server;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.server.InstanceServer;
import com.thenexusreborn.api.server.VirtualServer;

public class HubVirtualServer extends VirtualServer {
    public HubVirtualServer(InstanceServer parent, String name) {
        super(parent, name, "hub");
    }

    public HubVirtualServer(String name) {
        super(name, "hub");
    }

    @Override
    public void join(NexusPlayer nexusPlayer) {
        
    }

    @Override
    public void quit(NexusPlayer nexusPlayer) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
