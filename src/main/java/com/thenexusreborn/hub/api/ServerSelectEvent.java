package com.thenexusreborn.hub.api;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.nexuscore.api.events.NexusPlayerEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ServerSelectEvent extends NexusPlayerEvent implements Cancellable {
    protected static final HandlerList handlers = new HandlerList();
    
    private String serverName;
    private boolean cancelled;
    private String errorMessage;

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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
