package com.thenexusreborn.hub.listener;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.*;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.menu.GameBrowserMenu;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import com.thenexusreborn.nexuscore.api.events.*;
import com.thenexusreborn.nexuscore.player.SpigotNexusPlayer;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("DuplicatedCode")
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        
        if (e.getItem().getType() == Material.COMPASS) {
            e.getPlayer().openInventory(new GameBrowserMenu(NexusHub.getPlugin(NexusHub.class)).getInventory());
        }
    }
    
    @EventHandler
    public void onPlayerLoad(NexusPlayerLoadEvent e) {
        SpigotNexusPlayer nexusPlayer = (SpigotNexusPlayer) e.getNexusPlayer();
        nexusPlayer.getScoreboard().setView(new HubScoreboard(e.getNexusPlayer().getScoreboard()));
    
        Preference incognito = nexusPlayer.getPreferences().get("incognito");
        Preference vanish = nexusPlayer.getPreferences().get("vanish");
    
        for (Player player : Bukkit.getOnlinePlayers()) {
            NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(player.getUniqueId());
            if (incognito.getValue()) {
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    player.hidePlayer(nexusPlayer.getPlayer());
                }
            }
            
            if (np.getPreferences().get("incognito").getValue()) {
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    nexusPlayer.getPlayer().hidePlayer(player);
                }
            }
            
            if (vanish.getValue()) {
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    player.hidePlayer(nexusPlayer.getPlayer());
                }
            }
    
            if (np.getPreferences().get("vanish").getValue()) {
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    nexusPlayer.getPlayer().hidePlayer(player);
                }
            }
        }
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            nexusPlayer.sendMessage("&6&l>> &5Welcome to &d&lThe Nexus Reborn&5!");
            nexusPlayer.sendMessage("&6&l>> &5We are currently in &aAlpha&d.");
            nexusPlayer.sendMessage("&6&l>> &5Shop to support us: &ehttps://shop.thenexusreborn.com/");
            nexusPlayer.sendMessage("&6&l>> &5Please use the &aGame Selector &dto navigate.");
    
            Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName(MCUtils.color("&e&lGAME SELECTOR &7&o(Right Click)"));
            compass.setItemMeta(meta);
            player.getInventory().setItem(4, compass);
            e.setJoinMessage(null);
        }, 20L);
    }
    
    @EventHandler
    public void onIncognitoToggle(IncognitoToggleEvent e) {
        SpigotNexusPlayer nexusPlayer = (SpigotNexusPlayer) e.getNexusPlayer();
    
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!e.getNewValue()) {
                player.showPlayer(nexusPlayer.getPlayer());
            } else {
                NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(player.getUniqueId());
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    player.hidePlayer(nexusPlayer.getPlayer());
                }
            }
        }
    }
    
    @EventHandler
    public void onVanishToggle(VanishToggleEvent e) {
        SpigotNexusPlayer nexusPlayer = (SpigotNexusPlayer) e.getNexusPlayer();
    
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!e.getNewValue()) {
                player.showPlayer(nexusPlayer.getPlayer());
            } else {
                NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(player.getUniqueId());
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    player.hidePlayer(nexusPlayer.getPlayer());
                }
            }
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}
