package com.thenexusreborn.hub;

import com.thenexusreborn.hub.listener.PlayerListener;
import com.thenexusreborn.nexuscore.NexusCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NexusHub extends JavaPlugin {
    
    private NexusCore nexusCore;
    
    @Override
    public void onEnable() {
        this.nexusCore = ((NexusCore) Bukkit.getPluginManager().getPlugin("NexusCore"));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
    
    public NexusCore getNexusCore() {
        return nexusCore;
    }
}
