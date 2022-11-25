package com.thenexusreborn.hub.tasks;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.api.NexusTask;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public class PlayerAndEntityTask extends NexusTask<NexusHub> {
    
    public PlayerAndEntityTask(NexusHub plugin) {
        super(plugin, 1L, 1L, false);
    }
    
    public void onRun() {
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
}
