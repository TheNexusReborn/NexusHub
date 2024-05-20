package com.thenexusreborn.hub.thread;

import com.stardevllc.starcore.utils.StarThread;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.World;

public class WorldThread extends StarThread<NexusHub> {
    
    public WorldThread(NexusHub plugin) {
        super(plugin, 20L, 0L, false);
    }
    
    public void onRun() {
        World world = getPlugin().getHubWorld();
        world.setThundering(false);
        world.setStorm(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setTime(6000L);
    }
}
