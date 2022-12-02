package com.thenexusreborn.hub.listener;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.helper.StringHelper;
import com.thenexusreborn.api.player.*;
import com.thenexusreborn.api.server.Phase;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.menu.GameBrowserMenu;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.api.NexusSpigotPlugin;
import com.thenexusreborn.nexuscore.api.events.*;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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

        boolean incognito = nexusPlayer.getToggleValue("incognito");
        boolean vanish = nexusPlayer.getToggleValue("vanish");
    
        if (nexusPlayer.getRank().ordinal() <= Rank.DIAMOND.ordinal()) {
            Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
            player.setAllowFlight(nexusPlayer.getToggleValue("fly"));
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
                    
                    boolean keepJoiningHidden = false, keepOtherHidden = false;
                    
                    if (incognito) {
                        if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            keepJoiningHidden = true;
                        }
                    }
    
                    if (np.getToggleValue("incognito")) {
                        if (nexusPlayer.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            keepOtherHidden = true;
                        }
                    }
    
                    if (vanish) {
                        if (np.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            keepJoiningHidden = true;
                        }
                    }
    
                    if (np.getToggleValue("vanish")) {
                        if (nexusPlayer.getRank().ordinal() > Rank.HELPER.ordinal()) {
                            keepOtherHidden = true;
                        }
                    }
                    
                    if (!keepJoiningHidden) {
                        other.showPlayer(player);
                    }
                    
                    if (!keepOtherHidden) {
                        player.showPlayer(other);
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
            //nexusPlayer.sendMessage("&6&l>> &5Shop to support us: &ehttps://shop.thenexusreborn.com/");
            nexusPlayer.sendMessage("&6&l>> &5Please use the &aGame Selector &dto navigate.");

            nexusPlayer.sendMessage("");
            nexusPlayer.sendMessage("");

            if (NexusAPI.PHASE != Phase.RELEASE) {
                nexusPlayer.sendMessage("&6&l>> &eWelcome to the &b" + StringHelper.capitalizeEveryWord(NexusAPI.PHASE.name()) + " &ephase of &5The Nexus Reborn");
                nexusPlayer.sendMessage("&6&l>> &eDue to the phase above, versions of plugins are below");
                nexusPlayer.sendMessage("&6&l>> &ePlease use these versions in bug reports");
                nexusPlayer.sendMessage("&6&l>> &eYou can access this list with &b/nexusversion");
                nexusPlayer.sendMessage("&6&l>> &eThis message is only shown in the hub");
                nexusPlayer.sendMessage("&6&l>> &eNexusAPI v" + NexusAPI.getApi().getVersion().toString());
                NexusCore nexusCore = (NexusCore) Bukkit.getServer().getPluginManager().getPlugin("NexusCore");
                nexusPlayer.sendMessage("&6&l>> &eNexusCore v" + nexusCore.getDescription().getVersion());
                for (NexusSpigotPlugin nexusPlugin : nexusCore.getNexusPlugins()) {
                    nexusPlayer.sendMessage("&6&l>> &e" + nexusPlugin.getName() + " v" + nexusPlugin.getDescription().getVersion());
                }
            }
    
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName(MCUtils.color("&e&lGAME SELECTOR &7&o(Right Click)"));
            compass.setItemMeta(meta);
            player.getInventory().setItem(4, compass);
        }, 20L);
    }
    
    @EventHandler
    public void onToggleChange(ToggleChangeEvent e) {
        NexusPlayer nexusPlayer = e.getNexusPlayer();
        Toggle toggle = e.getToggle();
        Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
        if (player == null) return;
        
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
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }
}
