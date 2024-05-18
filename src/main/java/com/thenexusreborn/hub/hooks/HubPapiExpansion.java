package com.thenexusreborn.hub.hooks;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.NexusPlayer;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.hub.NexusHub;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HubPapiExpansion extends PlaceholderExpansion {
    
    private final NexusHub plugin;

    public HubPapiExpansion(NexusHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player,  String params) {
        NexusPlayer nexusPlayer = NexusAPI.getApi().getPlayerManager().getNexusPlayer(player.getUniqueId());
        if (params.equalsIgnoreCase("players")) {
            int onlinePlayers = 0;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                NexusPlayer onp = NexusAPI.getApi().getPlayerManager().getNexusPlayer(onlinePlayer.getUniqueId());
                if (onp == null) continue;
                if (onp.getToggleValue("incognito") || onp.getToggleValue("vanish")) {
                    if (nexusPlayer.getRank().ordinal() <= Rank.HELPER.ordinal()) {
                        onlinePlayers++;
                    }
                } else {
                    onlinePlayers++;
                }
            }
            return onlinePlayers + "";
        }
        
        return null;
    }

    @Override
    public String getIdentifier() {
        return "nexushub";
    }

    @Override
    public String getAuthor() {
        return "Firestar311";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
