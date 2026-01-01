package com.thenexusreborn.hub.menu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stardevllc.itembuilder.ItemBuilders;
import com.stardevllc.itembuilder.common.ItemBuilder;
import com.stardevllc.smaterial.SMaterial;
import com.stardevllc.starcore.api.ui.element.button.Button;
import com.stardevllc.starlib.helper.StringHelper;
import com.stardevllc.starlib.time.TimeFormat;
import com.thenexusreborn.api.NexusReborn;
import com.thenexusreborn.api.server.NexusServer;
import com.thenexusreborn.api.util.NetworkType;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.api.ServerSelectEvent;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.Bukkit;

import java.util.*;

public class ServerElement extends Button {
    private NexusServer server;
    
    public ServerElement(NexusHub plugin, NexusServer server) {
        this.server = server;
        
        this.iconCreator = player -> {
            ItemBuilder itemBuilder = ItemBuilders.of(SMaterial.AIR);
            
            if (server.getStatus().equalsIgnoreCase("offline")) {
                itemBuilder.material(SMaterial.BEDROCK);
                itemBuilder.displayName("&4" + server.getName());
                itemBuilder.addLoreLine("&4&lOFFLINE");
            } else {
                JsonObject stateObject = (JsonObject) new JsonParser().parse(server.getState());
                String mode = StringHelper.titlize(stateObject.get("mode").getAsString());
                String map = stateObject.get("map").getAsString();
                String type = stateObject.get("type").getAsString();
                String state = stateObject.get("state").getAsString().toLowerCase();
                if (type.equalsIgnoreCase("lobby")) {
                    if (List.of("shutting_down", "setup").contains(state)) {
                        itemBuilder.material(SMaterial.REDSTONE_BLOCK);
                        itemBuilder.displayName("&c" + server.getName());
                        itemBuilder.addLoreLine("&cServer not ready for players.");
                    } else if (List.of("starting", "preparing_game", "game_prepared").contains(state)) {
                        itemBuilder.material(SMaterial.GOLD_BLOCK);
                        itemBuilder.displayName("&e" + server.getName());
                        itemBuilder.addLoreLine("&eServer is preparing a game, cannot join.");
                    } else if (List.of("waiting", "countdown").contains(state)) {
                        itemBuilder.material(SMaterial.EMERALD_BLOCK);
                        itemBuilder.displayName("&a" + server.getName());
                        JsonObject playersObject = stateObject.getAsJsonObject("playerCounts");
                        itemBuilder.setLore(List.of("&eMode: &b" + mode,
                                "&eMap: &b" + map,
                                "&eTime: &a" + stateObject.get("time").getAsInt() + "s",
                                "&d" + playersObject.get("playing").getAsInt() + "&e/&5" + server.getMaxPlayers()));
                    } else if (state.equalsIgnoreCase("MAP_EDITING")) {
                        itemBuilder.material(SMaterial.MAP);
                        itemBuilder.displayName("&d" + server.getName());
                        itemBuilder.addLoreLine("&7Maps are being edited.");
                    }
                } else if (type.equalsIgnoreCase("game")) {
                    if (List.of("undefined", "error", "shutting_down").contains(state)) {
                        itemBuilder.material(SMaterial.REDSTONE_BLOCK);
                        itemBuilder.displayName("&c" + server.getName());
                        itemBuilder.addLoreLine("&cError");
                    } else if (List.of("setting_up", "setup_complete", "assign_teams", "teams_assigned", "teleport_start", "teleport_start_done").contains(state)) {
                        itemBuilder.material(SMaterial.GOLD_BLOCK);
                        itemBuilder.displayName("&e" + server.getName());
                        itemBuilder.addLoreLine("&eServer is setting up a game, cannot join.");
                    } else {
                        itemBuilder.material(SMaterial.DIAMOND_BLOCK);
                        itemBuilder.displayName("&b" + server.getName());
                        
                        JsonObject timeObject = stateObject.getAsJsonObject("time");
                        JsonObject playersObject = stateObject.getAsJsonObject("teamCounts");
                        
                        List<String> lore = new LinkedList<>();
                        lore.add("&eMode: &b:" + mode);
                        
                        if (state.startsWith("warmup")) {
                            itemBuilder.setLore(List.of("&e&lWARMUP",
                                    "&eMode: &b:" + mode, 
                                    "&eTime: " + timeObject.get("main").getAsInt() + "s",
                                    "&d" + (playersObject.get("tributes").getAsInt() + playersObject.get("spectators").getAsInt()) + "&e/&5" + server.getMaxPlayers()));
                        } else if (state.startsWith("ingame")) {
                            itemBuilder.setLore(List.of("&a&lINGAME",
                                    "&eMode: &b:" + mode,
                                    "&aTributes: &f" + playersObject.get("tributes").getAsInt(),
                                    "&cSpectators: &f" + playersObject.get("spectators").getAsInt(),
                                    "&dMutations: &f" + playersObject.get("mutations").getAsInt(),
                                    "",
                                    "&3Time: &f" + new TimeFormat("%00m%%00s%").format(timeObject.get("main").getAsLong())));
                        } else if (state.contains("deathmatch")) {
                            itemBuilder.setLore(List.of("&c&lDEATHMATCH",
                                    "&eMode: &b:" + mode,
                                    "&aTributes: &f" + playersObject.get("tributes").getAsInt(),
                                    "&cSpectators: &f" + playersObject.get("spectators").getAsInt(),
                                    "",
                                    "&3Time: &f" + new TimeFormat("%00m%%00s%").format(timeObject.get("main").getAsLong())));
                        } else {
                            itemBuilder.setLore(List.of("&6&lENDING"));
                        }
                        itemBuilder.setLore(lore);
                    }
                }
            }
            
            return itemBuilder.build();
        };
        
        this.eventConsumer =  e -> {
            if (NexusReborn.NETWORK_TYPE == NetworkType.SINGLE && Bukkit.getPluginManager().getPlugin("SurvivalGames") != null) {
                plugin.getHubServer().quit(e.getWhoClicked().getUniqueId());
                plugin.getHubChatRoom().removeMember(e.getWhoClicked().getUniqueId());
                ServerSelectEvent serverSelectEvent = new ServerSelectEvent(e.getWhoClicked().getUniqueId(), NexusReborn.getPlayerManager().getNexusPlayer(e.getWhoClicked().getUniqueId()), server.getName());
                Bukkit.getServer().getPluginManager().callEvent(serverSelectEvent);
                if (serverSelectEvent.isCancelled()) {
                    e.getWhoClicked().sendMessage(MsgType.ERROR.format(serverSelectEvent.getErrorMessage()));
                    plugin.getHubServer().join(e.getWhoClicked().getUniqueId());
                }
            } //TODO BungeeCord Transfer / Transfer Packet in newer versions
        };
    }
    
    public NexusServer getServer() {
        return server;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        ServerElement that = (ServerElement) o;
        if (that == null) {
            return false;
        }
        return Objects.equals(server.getName(), that.server.getName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(server.getName());
    }
}