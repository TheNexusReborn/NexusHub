package com.thenexusreborn.hub.tasks;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerAndEntityTask extends BukkitRunnable {
    
    private NexusHub plugin;
    
    public PlayerAndEntityTask(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
        World world = Bukkit.getWorld(ServerProperties.getLevelName());
        Location spawn = plugin.getSpawn();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getBlockY() < spawn.getBlockY() - 40) {
                player.teleport(spawn);
            }
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
    
        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof Player) && !(entity instanceof ItemFrame)) {
                entity.remove();
            }
        }
    }
    
    public void start() {
        runTaskTimer(plugin, 1L, 1L);
    }
}
