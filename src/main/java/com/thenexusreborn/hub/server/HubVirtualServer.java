package com.thenexusreborn.hub.server;

import com.stardevllc.starchat.rooms.DefaultPermissions;
import com.stardevllc.starcore.color.ColorUtils;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.api.server.InstanceServer;
import com.thenexusreborn.api.server.VirtualServer;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        player.getInventory().clear();
        
        nexusPlayer.getScoreboard().setView(new HubScoreboard(nexusPlayer.getScoreboard(), plugin));

        if (nexusPlayer.getRank().ordinal() <= Rank.DIAMOND.ordinal()) {
            player.setAllowFlight(nexusPlayer.getToggleValue("fly"));
        }

        this.players.add(nexusPlayer.getUniqueId());
        plugin.getHubChatRoom().addMember(player.getUniqueId(), DefaultPermissions.VIEW_MESSAGES, DefaultPermissions.SEND_MESSAGES);
        plugin.getNexusCore().getStarChatPlugin().setPlayerFocus(player, plugin.getHubChatRoom());

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(ColorUtils.color("&e&lGAME SELECTOR &7&o(Right Click)"));
        compass.setItemMeta(meta);
        player.getInventory().setItem(4, compass);
    }

    @Override
    public void quit(NexusPlayer nexusPlayer) {
        this.players.remove(nexusPlayer.getUniqueId());
        plugin.getHubChatRoom().removeMember(nexusPlayer.getUniqueId());
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
