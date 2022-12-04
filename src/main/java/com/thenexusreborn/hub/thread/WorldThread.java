package com.thenexusreborn.hub.thread;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.api.NexusThread;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldThread extends NexusThread<NexusHub> {
    
    public WorldThread(NexusHub plugin) {
        super(plugin, 20L, 0L, false);
    }
    
    public void onRun() {
        World world = Bukkit.getWorld(ServerProperties.getLevelName());
        world.setThundering(false);
        world.setStorm(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setTime(6000L);
    }
}
