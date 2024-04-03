package com.thenexusreborn.hub.server;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.api.server.InstanceServer;
import com.thenexusreborn.api.server.VirtualServer;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.api.NexusSpigotPlugin;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("DuplicatedCode")
public class HubVirtualServer extends VirtualServer {
    private NexusHub plugin;
    
    public HubVirtualServer(NexusHub plugin, InstanceServer parent, String name) {
        super(parent, name, "hub", 100);
        this.plugin = plugin;
    }

    public HubVirtualServer(NexusHub plugin, String name) {
        super(name, "hub", 100);
        this.plugin = plugin;
    }

    @Override
    public void join(NexusPlayer nexusPlayer) {
        Player player = Bukkit.getPlayer(nexusPlayer.getUniqueId());
        player.teleport(plugin.getSpawn());
        player.setGameMode(GameMode.ADVENTURE);

        boolean incognito = nexusPlayer.getToggleValue("incognito");
        boolean vanish = nexusPlayer.getToggleValue("vanish");

        if (nexusPlayer.getRank().ordinal() <= Rank.DIAMOND.ordinal()) {
            player.setAllowFlight(nexusPlayer.getToggleValue("fly"));
        }

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

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            nexusPlayer.sendMessage("&6&l>> &5Welcome to &d&lThe Nexus Reborn&5!");
            //nexusPlayer.sendMessage("&6&l>> &5Shop to support us: &ehttps://shop.thenexusreborn.com/");
            nexusPlayer.sendMessage("&6&l>> &5Please use the &aGame Selector &5to navigate.");

            nexusPlayer.sendMessage("");
            nexusPlayer.sendMessage("");

            nexusPlayer.sendMessage("&6&l>> &eWelcome to the &bALPHA &ephase of &5The Nexus Reborn");
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

            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName(MCUtils.color("&e&lGAME SELECTOR &7&o(Right Click)"));
            compass.setItemMeta(meta);
            player.getInventory().setItem(4, compass);
        }, 20L);
    }

    @Override
    public void quit(NexusPlayer nexusPlayer) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
