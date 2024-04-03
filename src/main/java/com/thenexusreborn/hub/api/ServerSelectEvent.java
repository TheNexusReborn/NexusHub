package com.thenexusreborn.hub.api;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.nexuscore.api.events.NexusPlayerEvent;
import org.bukkit.event.HandlerList;

public class ServerSelectEvent extends NexusPlayerEvent {
    protected static final HandlerList handlers = new HandlerList();
    
    private String serverName;

    public ServerSelectEvent(NexusPlayer nexusPlayer, String serverName) {
        super(nexusPlayer);
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
