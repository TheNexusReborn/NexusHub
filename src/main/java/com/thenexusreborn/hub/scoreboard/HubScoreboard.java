package com.thenexusreborn.hub.scoreboard;

import com.stardevllc.starcore.api.StarColors;
import com.thenexusreborn.api.player.Rank;
import com.thenexusreborn.api.scoreboard.NexusScoreboard;
import com.thenexusreborn.api.scoreboard.TeamBuilder;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.scoreboard.SpigotScoreboardView;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HubScoreboard extends SpigotScoreboardView {

    private NexusHub plugin;
    
    public HubScoreboard(NexusScoreboard scoreboard, NexusHub plugin) {
        super(scoreboard, "lobby", StarColors.color("&5&lTHE NEXUS"));
        this.plugin = plugin;
    }

    @Override
    public void registerTeams() {
        createTeam(new TeamBuilder("rankLabel").entry("&6&lRANK:").score(15));
        createTeam(new TeamBuilder("rankValue").entry(ChatColor.GREEN.toString()).score(14).valueUpdater((player, team) -> {
            Rank rank = player.getEffectiveRank();
            team.setPrefix(rank.getColor() + rank.name());
        }));
        createTeam(new TeamBuilder("blank1").entry(ChatColor.GOLD.toString()).score(13));
        createTeam(new TeamBuilder("balanceLabel").entry("&6&lBALANCE:").score(12));
        createTeam(new TeamBuilder("creditsValue").entry("&fCredits: ").score(11).valueUpdater((player, team) -> team.setSuffix("§3" + formatBalance(player.getBalance().getCredits()))));
        createTeam(new TeamBuilder("nexites").entry("&fNexites: ").score(10).valueUpdater((player, team) -> team.setSuffix("§9" + formatBalance(player.getBalance().getNexites()))));
        createTeam(new TeamBuilder("blank2").entry(ChatColor.BLACK.toString()).score(9));
        createTeam(new TeamBuilder("playersLabel").entry("&6&lPLAYERS").score(8));
        createTeam(new TeamBuilder("playersValue").entry(ChatColor.YELLOW.toString()).score(7).valueUpdater((player, team) -> {
            int onlinePlayers = 0;
            
            Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());

            for (UUID uuid : plugin.getHubServer().getPlayers()) {
                Player onlinePlayer = Bukkit.getPlayer(uuid);
                if (onlinePlayer == null) {
                    continue;
                }
                if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    onlinePlayers++;
                    continue;
                }
                
                if (bukkitPlayer.canSee(onlinePlayer)) {
                    onlinePlayers++;
                }
            }
            
            team.setPrefix(StarColors.color("&f" + onlinePlayers + "/" + Bukkit.getServer().getMaxPlayers()));
        }));
        createTeam(new TeamBuilder("blank3").entry(ChatColor.DARK_BLUE.toString()).score(6));
        createTeam(new TeamBuilder("serverLabel").entry("&6&lSERVER:").score(5));
        createTeam(new TeamBuilder("serverValue").entry("&fNexus"));
    }

    private String formatBalance(double balance) {
        if (balance < 1000) {
            return MCUtils.formatNumber(balance);
        }
        return MCUtils.formatNumber(balance / 1000) + "k";
    }
}