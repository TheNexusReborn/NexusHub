package com.thenexusreborn.hub.listener;

import com.stardevllc.starui.GuiManager;
import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.api.player.Toggle;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.menu.GameBrowserMenu;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import com.thenexusreborn.nexuscore.api.events.NexusPlayerLoadEvent;
import com.thenexusreborn.nexuscore.api.events.ToggleChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.getWorld().equals(plugin.getHubWorld())) {
            return;
        }
        
        if (e.getItem() == null) {
            return;
        }
        
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        
        if (e.getItem().getType() == Material.COMPASS) {
            GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
            manager.openGUI(new GameBrowserMenu(plugin), e.getPlayer());
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
        
        if (toggle.getInfo().getName().equalsIgnoreCase("vanish") || toggle.getInfo().getName().equalsIgnoreCase("incognito")) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!e.newValue()) {
                    other.showPlayer(player);
                } else {
                    NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(other.getUniqueId());
                    if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                        other.hidePlayer(player);
                    }
                }
            }
        } else if (toggle.getInfo().getName().equalsIgnoreCase("fly")) {
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
