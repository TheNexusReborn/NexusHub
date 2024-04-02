package com.thenexusreborn.hub;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.util.NetworkType;
import com.thenexusreborn.hub.cmds.*;
import com.thenexusreborn.hub.hooks.HubPapiExpansion;
import com.thenexusreborn.hub.listener.PlayerListener;
import com.thenexusreborn.hub.listener.ServerListener;
import com.thenexusreborn.hub.thread.*;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.api.NexusSpigotPlugin;
import com.thenexusreborn.nexuscore.util.ServerProperties;
import org.bukkit.*;

public class NexusHub extends NexusSpigotPlugin {

    private NexusCore nexusCore;
    private Location spawn;
    private String hubWorldName;
    private World hubWorld;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.nexusCore = (NexusCore) Bukkit.getPluginManager().getPlugin("NexusCore");
        if (nexusCore == null) {
            getLogger().severe("Could not find NexusCore, disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (NexusAPI.NETWORK_TYPE == NetworkType.SINGLE) {
            this.hubWorldName = getConfig().getString("worldname");
            if (this.hubWorldName == null || this.hubWorldName.isEmpty()) {
                getLogger().severe("NexusAPI is configured to be a single server, but no world name is set for the hub world.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        this.nexusCore.addNexusPlugin(this);
        
        new HubPapiExpansion(this).register();
        
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ServerListener(this), this);
        
        getCommand("setspawn").setExecutor(new SetSpawnCmd(this));
        getCommand("spawn").setExecutor(new SpawnCmd(this));
        World world = getHubWorld();
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

        new PlayerAndEntityThread(this).start();
        new WorldThread(this).start();
    }

    public World getHubWorld() {
        if (this.hubWorld == null) {
            if (NexusAPI.NETWORK_TYPE == NetworkType.SINGLE) {
                this.hubWorld = Bukkit.getWorld(this.hubWorldName);
            } else {
                this.hubWorld = Bukkit.getWorld(ServerProperties.getLevelName());
            }
        }
        return this.hubWorld;
    }

    @Override
    public void onDisable() {
        if (nexusCore == null) {
            return;
        }
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
