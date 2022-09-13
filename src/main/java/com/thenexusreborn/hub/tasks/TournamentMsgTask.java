package com.thenexusreborn.hub.tasks;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.tournament.Tournament;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentMsgTask extends BukkitRunnable {
    
    private JavaPlugin plugin;
    
    public TournamentMsgTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
        Tournament tournament = NexusAPI.getApi().getTournament();
        if (tournament != null && tournament.isActive()) {
            Bukkit.broadcastMessage(MCUtils.color("&6&l>> &aThere is an active tournament going on right now."));
            Bukkit.broadcastMessage(MCUtils.color("&6&l> &aLook for the servers that have tournament in the button description"));
        }
    }
    
    public void start() {
        runTaskTimer(plugin, 20L, 2400L);
    }
}
