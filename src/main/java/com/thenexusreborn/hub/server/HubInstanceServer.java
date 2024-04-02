package com.thenexusreborn.hub.server;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.server.InstanceServer;

public class HubInstanceServer extends InstanceServer {
    public HubInstanceServer(String name) {
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
