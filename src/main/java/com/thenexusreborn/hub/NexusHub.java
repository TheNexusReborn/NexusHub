package com.thenexusreborn.hub;

import com.thenexusreborn.api.*;
import com.thenexusreborn.api.multicraft.MulticraftAPI;
import com.thenexusreborn.api.server.ServerInfo;
import com.thenexusreborn.hub.cmds.SetSpawnCmd;
import com.thenexusreborn.hub.listener.PlayerListener;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NexusHub extends JavaPlugin {
    
    private NexusCore nexusCore;
    
    private Location spawn;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.nexusCore = ((NexusCore) Bukkit.getPluginManager().getPlugin("NexusCore"));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("setspawn").setExecutor(new SetSpawnCmd(this));
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
        
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().getBlockY() < spawn.getBlockX() - 20) {
                        player.teleport(spawn);
                    }
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                }
    
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof Player)) {
                        entity.remove();
                    }
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    
        new BukkitRunnable() {
            @Override
            public void run() {
                ServerInfo serverInfo = NexusAPI.getApi().getServerManager().getCurrentServer();
                if (NexusAPI.getApi().getEnvironment() != Environment.DEVELOPMENT) {
                    serverInfo.setStatus(MulticraftAPI.getInstance().getServerStatus(serverInfo.getMulticraftId()).status);
                } else {
                    serverInfo.setStatus("online");
                }
                serverInfo.setPlayers(Bukkit.getOnlinePlayers().size());
                NexusAPI.getApi().getDataManager().pushServerInfo(serverInfo);
            }
        }.runTaskTimerAsynchronously(this, 20L, 20L);
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
