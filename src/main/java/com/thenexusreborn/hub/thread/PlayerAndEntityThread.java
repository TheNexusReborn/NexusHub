package com.thenexusreborn.hub.thread;

import com.stardevllc.starcore.utils.StarThread;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public class PlayerAndEntityThread extends StarThread<NexusHub> {
    
    public PlayerAndEntityThread(NexusHub plugin) {
        super(plugin, 1L, 1L, false);
    }
    
    public void onRun() {
        World world = getPlugin().getHubWorld();
        Location spawn = plugin.getSpawn();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().equals(world)) {
                continue;
            }
            
            if (player.getLocation().getBlockY() < spawn.getBlockY() - 40) {
                player.teleport(spawn);
            }
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
    
        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof Player) && !(entity instanceof ItemFrame)) {
                if (entity.getWorld().equals(getPlugin().getHubWorld())) {
                    entity.remove();
                }
            }
        }
    }
}
