package com.thenexusreborn.hub.listener;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import com.thenexusreborn.nexuscore.api.events.NexusPlayerLoadEvent;
import org.bukkit.event.*;

public class PlayerListener implements Listener {
    
    private NexusHub plugin;
    
    public PlayerListener(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerLoad(NexusPlayerLoadEvent e) {
        e.getNexusPlayer().getScoreboard().setView(new HubScoreboard(e.getNexusPlayer().getScoreboard()));
    }
}
