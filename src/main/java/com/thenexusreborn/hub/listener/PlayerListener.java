package com.thenexusreborn.hub.listener;

import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import com.thenexusreborn.nexuscore.api.events.NexusPlayerLoadEvent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    
    private NexusHub plugin;
    
    public PlayerListener(NexusHub plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(plugin.getSpawn());
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        HumanEntity whoClicked = e.getWhoClicked();
        if (whoClicked.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerLoad(NexusPlayerLoadEvent e) {
        NexusPlayer nexusPlayer = e.getNexusPlayer();
        nexusPlayer.getScoreboard().setView(new HubScoreboard(e.getNexusPlayer().getScoreboard()));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            nexusPlayer.sendMessage("&6&l>> &dWelcome to &5&lThe Nexus Reborn&5!");
            nexusPlayer.sendMessage("&6&l>> &dThis server is a project to bring back TheNexusMC, as least, some of it.");
            nexusPlayer.sendMessage("&6&l>> &dWe are currently in &aPre-Alpha &dso expect some bugs and instability, as well as a lack of features.");
            nexusPlayer.sendMessage("&6&l>> &dIf you would like to support us, please go to &eshop.thenexusreborn.com &dThat would mean a lot to us.");
        }, 20L);
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}
