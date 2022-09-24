package com.thenexusreborn.hub.listener;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.menu.GameBrowserMenu;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import com.thenexusreborn.nexuscore.api.events.IncognitoToggleEvent;
import com.thenexusreborn.nexuscore.api.events.NexusPlayerLoadEvent;
import com.thenexusreborn.nexuscore.api.events.VanishToggleEvent;
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
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("DuplicatedCode")
public class PlayerListener implements Listener {
    
    private final NexusHub plugin;
    
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
        NexusPlayer nexusPlayer = e.getNexusPlayer();
        e.setScoreboardView(new HubScoreboard(e.getNexusPlayer().getScoreboard()));

        boolean incognito = nexusPlayer.getPreferenceValue("incognito");
        boolean vanish = nexusPlayer.getPreferenceValue("vanish");
    
        if (nexusPlayer.getRank().ordinal() <= Rank.DIAMOND.ordinal()) {
            Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
            player.setAllowFlight(nexusPlayer.getPreferenceValue("fly"));
        }
    
        Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
        for (Player other : Bukkit.getOnlinePlayers()) {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getPlayer(other.getUniqueId()) == null) {
                        cancel();
                        return;
                    }
                    
                    NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(other.getUniqueId());
                    if (np == null) {
                        return;
                    }
                    if (incognito) {
                        if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            other.hidePlayer(player);
                        }
                    }
    
                    if (np.getPreferenceValue("incognito")) {
                        if (nexusPlayer.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            player.hidePlayer(other);
                        }
                    }
    
                    if (vanish) {
                        if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            other.hidePlayer(player);
                        }
                    }
    
                    if (np.getPreferenceValue("vanish")) {
                        if (nexusPlayer.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            player.hidePlayer(other);
                        }
                    }
                    
                    cancel();
                }
            };
            
            if (NexusAPI.getApi().getPlayerManager().getNexusPlayer(other.getUniqueId()) == null) {
                runnable.runTaskTimer(plugin, 1L, 10L);
            } else {
                runnable.runTask(plugin);
            }
            
        }
        e.setJoinMessage(null);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            nexusPlayer.sendMessage("&6&l>> &5Welcome to &d&lThe Nexus Reborn&5!");
            nexusPlayer.sendMessage("&6&l>> &5We are currently in &aAlpha&d.");
            nexusPlayer.sendMessage("&6&l>> &5Shop to support us: &ehttps://shop.thenexusreborn.com/");
            nexusPlayer.sendMessage("&6&l>> &5Please use the &aGame Selector &dto navigate.");
    
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName(MCUtils.color("&e&lGAME SELECTOR &7&o(Right Click)"));
            compass.setItemMeta(meta);
            player.getInventory().setItem(4, compass);
        }, 20L);
    }
    
    @EventHandler
    public void onIncognitoToggle(IncognitoToggleEvent e) {
        NexusPlayer nexusPlayer = e.getNexusPlayer();
        Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!e.getNewValue()) {
                other.showPlayer(player);
            } else {
                NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(other.getUniqueId());
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    other.hidePlayer(player);
                }
            }
        }
    }
    
    @EventHandler
    public void onVanishToggle(VanishToggleEvent e) {
        NexusPlayer nexusPlayer = e.getNexusPlayer();
        Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
    
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!e.getNewValue()) {
                other.showPlayer(player);
            } else {
                NexusPlayer np = NexusAPI.getApi().getPlayerManager().getNexusPlayer(other.getUniqueId());
                if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                    other.hidePlayer(player);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}
