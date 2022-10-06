package com.thenexusreborn.hub.scoreboard;

import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.helper.StringHelper;
import com.thenexusreborn.api.player.*;
import com.thenexusreborn.api.scoreboard.NexusScoreboard;
import com.thenexusreborn.api.scoreboard.wrapper.*;
import com.thenexusreborn.nexuscore.scoreboard.SpigotScoreboardView;
import com.thenexusreborn.nexuscore.util.MCUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class HubScoreboard extends SpigotScoreboardView {
    
    private final String rankLabelName = "rankLabel", rankValueName = "rankValue", blank1Name = "blank1", balanceLabelName = "balanceLabel", 
    creditsValueName = "creditsValue", nexitesValueName = "nexitesValue", blank2Name = "blank2", playersLabelName = "playersLabel", playersValueName = "playersValue", 
    blank3Name = "blank3", serverLabelName = "serverLabel", serverValueName = "serverValue";
    
    public HubScoreboard(NexusScoreboard scoreboard) {
        super(scoreboard, "lobby", MCUtils.color("&5&lTHE NEXUS"));
    }
    
    @Override
    public void registerTeams() {
        NexusPlayer player = this.scoreboard.getPlayer();
        IScoreboard scoreboard = this.scoreboard.getScoreboard();
        ITeam rankLabel = scoreboard.registerNewTeam(rankLabelName);
        addEntry(objective, rankLabel, MCUtils.color("&6&lRANK:"), 15);
        
        ITeam rankValue = scoreboard.registerNewTeam(rankValueName);
        rankValue.setPrefix(MCUtils.color(player.getRanks().get().getColor() + StringHelper.capitalizeEveryWord(player.getRanks().get().name())));
        addEntry(objective, rankValue, ChatColor.GREEN.toString(), 14);
        
        ITeam blank1 = scoreboard.registerNewTeam(blank1Name);
        addEntry(objective, blank1, ChatColor.GOLD.toString(), 13);
        
        ITeam balanceLabel = scoreboard.registerNewTeam(balanceLabelName);
        addEntry(objective, balanceLabel, MCUtils.color("&6&lBALANCE:"), 12);
        
        ITeam creditsValue = scoreboard.registerNewTeam(creditsValueName);
        creditsValue.setPrefix(MCUtils.color("&fCredits: "));
        double credits = (double) player.getStats().getValue("credits");
        creditsValue.setSuffix(MCUtils.color("&3" + formatBalance(credits)));
        addEntry(objective, creditsValue, ChatColor.DARK_RED.toString(), 11);
        
        ITeam nexitesValue = scoreboard.registerNewTeam(nexitesValueName);
        nexitesValue.setPrefix(MCUtils.color("&fNexites: "));
        nexitesValue.setSuffix(MCUtils.color("&9" + formatBalance((double) player.getStats().getValue("nexites"))));
        addEntry(objective, nexitesValue, ChatColor.AQUA.toString(), 10);
    
        ITeam blank2 = scoreboard.registerNewTeam(blank2Name);
        addEntry(objective, blank1, ChatColor.BLACK.toString(), 9);
        
        ITeam playersLabel = scoreboard.registerNewTeam(playersLabelName);
        addEntry(objective, playersLabel, MCUtils.color("&6&lPLAYERS:"), 8);
        
        ITeam playersValue = scoreboard.registerNewTeam(playersValueName);
        playersValue.setPrefix(MCUtils.color("&f" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers()));
        addEntry(objective, playersValue, ChatColor.YELLOW.toString(), 7);
        
        ITeam blank3 = scoreboard.registerNewTeam(blank3Name);
        addEntry(objective, blank3, ChatColor.DARK_BLUE.toString(), 6);
        
        ITeam serverLabel = scoreboard.registerNewTeam(serverLabelName);
        addEntry(objective, serverLabel, MCUtils.color("&6&lSERVER:"), 5);
    
        ITeam serverValue = scoreboard.registerNewTeam(serverValueName);
        addEntry(objective, serverValue, MCUtils.color("&f" + NexusAPI.getApi().getServerManager().getCurrentServer().getName()), 4);
    }
    
    private String formatBalance(double balance) {
        if (balance < 100) {
            return MCUtils.formatNumber(balance);
        }
        return MCUtils.formatNumber(balance / 1000) + "k";
    }
    
    @Override
    public void update() {
        NexusPlayer player = this.scoreboard.getPlayer();
        IScoreboard scoreboard = this.scoreboard.getScoreboard();
        String rankName;
        if (player.getRanks().get() == Rank.MEMBER) {
            rankName = "Member";
        } else {
            rankName = player.getRanks().get().name();
        }
        scoreboard.getTeam(this.rankValueName).setPrefix(MCUtils.color(player.getRanks().get().getColor() + rankName));
        scoreboard.getTeam(this.creditsValueName).setSuffix(MCUtils.color("&3" + formatBalance((double) player.getStats().getValue("credits"))));
        scoreboard.getTeam(this.nexitesValueName).setSuffix(MCUtils.color("&9" + formatBalance((double) player.getStats().getValue("nexites"))));
        int onlinePlayers = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            NexusPlayer onp = NexusAPI.getApi().getPlayerManager().getNexusPlayer(onlinePlayer.getUniqueId());
            if (onp == null) continue;
            if (onp.getToggles().getValue("incognito") || onp.getToggles().getValue("vanish")) {
                if (player.getRanks().get().ordinal() <= Rank.HELPER.ordinal()) {
                    onlinePlayers++;
                }
            } else {
                onlinePlayers++;
            }
        }
        scoreboard.getTeam(this.playersValueName).setPrefix(MCUtils.color("&f" + onlinePlayers + "/" + Bukkit.getServer().getMaxPlayers()));
    }
    
    @Override
    public List<String> getTeams() {
        return Arrays.asList(rankLabelName, rankValueName, blank1Name, balanceLabelName, creditsValueName, nexitesValueName, blank2Name, playersLabelName, playersValueName, 
                blank3Name, serverLabelName, serverValueName);
    }
}
