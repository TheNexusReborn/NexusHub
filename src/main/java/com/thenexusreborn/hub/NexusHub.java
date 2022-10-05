package com.thenexusreborn.hub;

import com.thenexusreborn.hub.cmds.*;
import com.thenexusreborn.hub.listener.PlayerListener;
import com.thenexusreborn.hub.tasks.*;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.api.NexusSpigotPlugin;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.*;

public class NexusHub extends NexusSpigotPlugin {
    
    private NexusCore nexusCore;
    
    private Location spawn;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.nexusCore = ((NexusCore) Bukkit.getPluginManager().getPlugin("NexusCore"));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("setspawn").setExecutor(new SetSpawnCmd(this));
        getCommand("spawn").setExecutor(new SpawnCmd(this));
        World world = Bukkit.getWorld(ServerProperties.getLevelName());
        if (this.getConfig().contains("spawn")) {
            String worldName = this.getConfig().getString("spawn.world");
            int x = Integer.parseInt(this.getConfig().getString("spawn.x"));
            int y = Integer.parseInt(this.getConfig().getString("spawn.y"));
            int z = Integer.parseInt(this.getConfig().getString("spawn.z"));
            float yaw = Float.parseFloat(this.getConfig().getString("spawn.yaw"));
            float pitch = Float.parseFloat(this.getConfig().getString("spawn.pitch"));
            
            spawn = new Location(Bukkit.getWorld(worldName), x + 0.5, y, z + 0.5, yaw, pitch);
        } else {
            spawn = world.getSpawnLocation().add(0.5, 0, 0.5);
        }
        world.setDifficulty(Difficulty.PEACEFUL);
        
        new PlayerAndEntityTask(this).start();
        new WorldTask(this).start();
    }
    
    @Override
    public void onDisable() {
        getConfig().set("spawn.world", spawn.getWorld().getName());
        getConfig().set("spawn.x", spawn.getBlockX() + "");
        getConfig().set("spawn.y", spawn.getBlockY() + "");
        getConfig().set("spawn.z", spawn.getBlockZ() + "");
        getConfig().set("spawn.yaw", spawn.getYaw() + "");
        getConfig().set("spawn.pitch", spawn.getPitch() + "");
        saveConfig();
    }
    
    public NexusCore getNexusCore() {
        return nexusCore;
    }
    
    public void setSpawn(Location location) {
        this.spawn = location;
    }
    
    public Location getSpawn() {
        return spawn;
    }
}
