package com.thenexusreborn.hub.tasks;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.api.NexusTask;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldTask extends NexusTask<NexusHub> {
    
    public WorldTask(NexusHub plugin) {
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
