package com.thenexusreborn.hub.menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stardevllc.starcore.gui.element.button.Button;
import com.stardevllc.starcore.gui.gui.InventoryGUI;
import com.stardevllc.starcore.item.ItemBuilder;
import com.stardevllc.starcore.xseries.XMaterial;
import com.stardevllc.starlib.time.TimeFormat;
import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.server.NexusServer;
import com.thenexusreborn.api.util.NetworkType;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.api.ServerSelectEvent;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SGMenu extends InventoryGUI {

    private static final Gson GSON = new GsonBuilder().create();

    public SGMenu(NexusHub plugin) {
        super(1, "&6&lSurvival Games");

        Map<String, NexusServer> sgServers = new TreeMap<>();

        for (NexusServer server : NexusAPI.getApi().getServerRegistry()) {
            if (server.getMode().equalsIgnoreCase("survivalgames")) {
                sgServers.put(server.getName(), server);
            }
        }

        for (NexusServer server : sgServers.values()) {
            ItemBuilder itemBuilder = ItemBuilder.of(XMaterial.AIR);

            if (server.getStatus().equalsIgnoreCase("offline")) {
                itemBuilder.material(XMaterial.BEDROCK);
                itemBuilder.displayName("&4" + server.getName());
                itemBuilder.addLoreLine("&4&lOFFLINE");
            } else {
                JsonObject stateObject = (JsonObject) new JsonParser().parse(server.getState());
                String map = stateObject.get("map").getAsString();
                String type = stateObject.get("type").getAsString();
                String state = stateObject.get("state").getAsString().toLowerCase();
                if (type.equalsIgnoreCase("lobby")) {
                    if (List.of("shutting_down", "setup").contains(state)) {
                        itemBuilder.material(XMaterial.REDSTONE_BLOCK);
                        itemBuilder.displayName("&c" + server.getName());
                        itemBuilder.addLoreLine("&cServer not ready for players.");
                    } else if (List.of("starting", "preparing_game", "game_prepared").contains(state)) {
                        itemBuilder.material(XMaterial.GOLD_BLOCK);
                        itemBuilder.displayName("&e" + server.getName());
                        itemBuilder.addLoreLine("&eServer is preparing a game, cannot join.");
                    } else if (List.of("waiting", "countdown").contains(state)) {
                        itemBuilder.material(XMaterial.EMERALD_BLOCK);
                        itemBuilder.displayName("&a" + server.getName());
                        JsonObject playersObject = stateObject.getAsJsonObject("players");
                        itemBuilder.setLore(List.of("&eMap: &b" + map,
                                "&eTime: &a" + stateObject.get("time").getAsInt() + "s",
                                "&d" + playersObject.get("playing").getAsInt() + "&e/&5" + server.getMaxPlayers()));
                    }
                } else if (type.equalsIgnoreCase("game")) {
                    if (List.of("undefined", "error", "shutting_down").contains(state)) {
                        itemBuilder.material(XMaterial.REDSTONE_BLOCK);
                        itemBuilder.displayName("&c" + server.getName());
                        itemBuilder.addLoreLine("&cError");
                    } else if (List.of("setting_up", "setup_complete", "assign_teams", "teams_assigned", "teleport_start", "teleport_start_done").contains(state)) {
                        itemBuilder.material(XMaterial.GOLD_BLOCK);
                        itemBuilder.displayName("&e" + server.getName());
                        itemBuilder.addLoreLine("&eServer is setting up a game, cannot join.");
                    } else {
                        itemBuilder.material(XMaterial.DIAMOND_BLOCK);
                        itemBuilder.displayName("&b" + server.getName());
                        
                        JsonObject timeObject = stateObject.getAsJsonObject("time");
                        JsonObject playersObject = stateObject.getAsJsonObject("players");
                        
                        if (state.startsWith("warmup")) {
                            itemBuilder.setLore(List.of("&e&lWARMUP", 
                                    "&eTime: " + timeObject.get("main").getAsInt() + "s",
                                    "&d" + (playersObject.get("tributes").getAsInt() + playersObject.get("spectators").getAsInt()) + "&e/&5" + server.getMaxPlayers()));
                        } else if (state.startsWith("ingame")) {
                            itemBuilder.setLore(List.of("&a&lINGAME", 
                                    "&aTributes: &f" + playersObject.get("tributes").getAsInt(), 
                                    "&cSpectators: &f" + playersObject.get("spectators").getAsInt(), 
                                    "&dMutations: &f" + playersObject.get("mutations").getAsInt(), 
                                    "", 
                                    "&3Time: &f" + new TimeFormat("%00m%%00s%").format(timeObject.get("main").getAsLong())));
                        } else if (state.contains("deathmatch")) {
                            itemBuilder.setLore(List.of("&c&lDEATHMATCH",
                                    "&aTributes: &f" + playersObject.get("tributes").getAsInt(),
                                    "&cSpectators: &f" + playersObject.get("spectators").getAsInt(),
                                    "",
                                    "&3Time: &f" + new TimeFormat("%00m%%00s%").format(timeObject.get("main").getAsLong())));
                        } else {
                            itemBuilder.setLore(List.of("&6&lENDING"));
                        }
                    }
                }
            }

            Button button = new Button().iconCreator(player -> itemBuilder.build())
                    .consumer(e -> {
                        if (NexusAPI.NETWORK_TYPE == NetworkType.SINGLE && Bukkit.getPluginManager().getPlugin("SurvivalGames") != null) {
                            plugin.getHubServer().getPlayers().remove(e.getWhoClicked().getUniqueId());
                            ServerSelectEvent serverSelectEvent = new ServerSelectEvent(NexusAPI.getApi().getPlayerManager().getNexusPlayer(e.getWhoClicked().getUniqueId()), server.getName());
                            Bukkit.getServer().getPluginManager().callEvent(serverSelectEvent);
                            if (serverSelectEvent.isCancelled()) {
                                e.getWhoClicked().sendMessage(MsgType.ERROR.format(serverSelectEvent.getErrorMessage()));
                                plugin.getHubServer().getPlayers().add(e.getWhoClicked().getUniqueId());
                            }
                        }
                    });
            addElement(button);
        }
    }
}
