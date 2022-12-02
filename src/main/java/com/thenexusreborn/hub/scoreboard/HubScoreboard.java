package com.thenexusreborn.hub.scoreboard;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.player.*;
import com.thenexusreborn.api.scoreboard.*;
import com.thenexusreborn.nexuscore.scoreboard.SpigotScoreboardView;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class HubScoreboard extends SpigotScoreboardView {
    
    public HubScoreboard(NexusScoreboard scoreboard) {
        super(scoreboard, "lobby", MCUtils.color("&5&lTHE NEXUS"));
    }
    
    @Override
    public void registerTeams() {
        createTeam(new TeamBuilder("rankLabel").entry("&6&lRANK:").score(15));
        createTeam(new TeamBuilder("rankValue").entry(ChatColor.GREEN.toString()).score(14).valueUpdater((player, team) -> {
            Rank rank = player.getRank();
            team.setPrefix(rank.getColor() + rank.name());
        }));
        createTeam(new TeamBuilder("blank1").entry(ChatColor.GOLD.toString()).score(13));
        createTeam(new TeamBuilder("balanceLabel").entry("&6&lBALANCE:").score(12));
        createTeam(new TeamBuilder("creditsValue").entry("&fCredits: &3").score(11).valueUpdater((player, team) -> team.setSuffix(formatBalance(player.getStatValue("credits").getAsDouble()))));
        createTeam(new TeamBuilder("nexites").entry("&fNexites: &9").score(10).valueUpdater((player, team) -> team.setSuffix(formatBalance(player.getStatValue("nexites").getAsDouble()))));
        createTeam(new TeamBuilder("blank2").entry(ChatColor.BLACK.toString()).score(9));
        createTeam(new TeamBuilder("playersLabel").entry("&6&lPLAYERS").score(8));
        createTeam(new TeamBuilder("playersValue").entry(ChatColor.YELLOW.toString()).score(7).valueUpdater((player, team) -> {
            int onlinePlayers = 0;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                NexusPlayer onp = NexusAPI.getApi().getPlayerManager().getNexusPlayer(onlinePlayer.getUniqueId());
                if (onp == null) continue;
                if (onp.getToggleValue("incognito") || onp.getToggleValue("vanish")) {
                    if (player.getRank().ordinal() <= Rank.HELPER.ordinal()) {
                        onlinePlayers++;
                    }
                } else {
                    onlinePlayers++;
                }
            }
            team.setPrefix(MCUtils.color("&f" + onlinePlayers + "/" + Bukkit.getServer().getMaxPlayers()));
        }));
        createTeam(new TeamBuilder("blank3").entry(ChatColor.DARK_BLUE.toString()).score(6));
        createTeam(new TeamBuilder("serverLabel").entry("&6&lSERVER:").score(5));
        createTeam(new TeamBuilder("serverValue").entry("&f" + NexusAPI.getApi().getServerManager().getCurrentServer().getName()).score(4));  
    }
    
    private String formatBalance(double balance) {
        if (balance < 100) {
            return MCUtils.formatNumber(balance);
        }
        return MCUtils.formatNumber(balance / 1000) + "k";
    }
}