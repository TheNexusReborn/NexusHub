package com.thenexusreborn.hub.server;

import com.stardevllc.starchat.rooms.DefaultPermissions;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.api.server.InstanceServer;
import com.thenexusreborn.api.server.VirtualServer;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.scoreboard.HubScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

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
        player.getInventory().setArmorContents(null);
        
        nexusPlayer.setServer(this);
        nexusPlayer.getScoreboard().setView(new HubScoreboard(nexusPlayer.getScoreboard(), plugin));

        if (nexusPlayer.getRank().ordinal() <= Rank.DIAMOND.ordinal()) {
            player.setAllowFlight(nexusPlayer.getToggleValue("fly"));
        }

        this.players.add(nexusPlayer.getUniqueId());
        plugin.getHubChatRoom().addMember(player.getUniqueId(), DefaultPermissions.VIEW_MESSAGES, DefaultPermissions.SEND_MESSAGES);
        plugin.getNexusCore().getStarChatPlugin().setPlayerFocus(player, plugin.getHubChatRoom());

        ItemStack compass = plugin.getGameBrowserItem().toItemStack();
        player.getInventory().setItem(4, compass);
        player.getInventory().setHeldItemSlot(4);
    }
    
    public void teleportToSpawn(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        player.teleport(plugin.getSpawn());
    }

    @Override
    public void quit(NexusPlayer nexusPlayer) {
        if (nexusPlayer == null) {
            return;
        }
        
        quit(nexusPlayer.getUniqueId());
    }
    
    @Override
    public void quit(UUID uuid) {
        this.players.remove(uuid);
        plugin.getHubChatRoom().removeMember(uuid);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
