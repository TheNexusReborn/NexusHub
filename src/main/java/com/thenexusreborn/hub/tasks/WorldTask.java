package com.thenexusreborn.hub.tasks;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldTask extends BukkitRunnable {
    
    private NexusHub plugin;
    
    public WorldTask(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
        World world = Bukkit.getWorld(ServerProperties.getLevelName());
        world.setThundering(false);
        world.setStorm(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setTime(6000L);
    }
    
    public void start() {
        runTaskTimer(plugin, 1L, 20L);
    }
}
