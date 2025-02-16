package com.thenexusreborn.hub.listener;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Toggle;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.api.events.ToggleChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final NexusHub plugin;
    
    public PlayerListener(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        
        if (!player.getWorld().equals(plugin.getHubWorld())) {
            return;
        }
        
        if (player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onToggleChange(ToggleChangeEvent e) {
        NexusPlayer nexusPlayer = e.getNexusPlayer();
        Toggle toggle = e.getToggle();
        Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
        if (player == null) {
            return;
        }

        if (!player.getWorld().equals(plugin.getHubWorld())) {
            return;
        }
        
        if (toggle.getInfo().getName().equalsIgnoreCase("fly")) {
            player.setAllowFlight(e.newValue());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (!player.getWorld().equals(plugin.getHubWorld())) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (!e.getPlayer().getWorld().equals(plugin.getHubWorld())) {
            return;
        }
        e.setCancelled(true);
    }
}
