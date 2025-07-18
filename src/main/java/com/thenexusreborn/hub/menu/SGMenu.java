package com.thenexusreborn.hub.menu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stardevllc.starcore.api.itembuilder.ItemBuilder;
import com.stardevllc.starmclib.XMaterial;
import com.stardevllc.starui.element.button.Button;
import com.stardevllc.starui.gui.InventoryGUI;
import com.stardevllc.starui.gui.UpdatingGUI;
import com.stardevllc.time.TimeFormat;
import com.thenexusreborn.api.NexusReborn;
import com.thenexusreborn.api.server.NexusServer;
import com.thenexusreborn.api.util.NetworkType;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.hub.api.ServerSelectEvent;
import com.thenexusreborn.nexuscore.util.MsgType;
import org.bukkit.Bukkit;

import java.util.*;

public class SGMenu extends InventoryGUI implements UpdatingGUI {

//    private static final Gson GSON = new GsonBuilder().create();
        
    private NexusHub plugin;
    
    public SGMenu(NexusHub plugin, UUID player) {
        super(1, "&6&lSurvival Games", player);
        this.plugin = plugin;
    }

    @Override
    public void createItems() {
        Map<String, NexusServer> sgServers = new TreeMap<>();

        for (NexusServer server : NexusReborn.getServerRegistry()) {
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
                        JsonObject playersObject = stateObject.getAsJsonObject("playerCounts");
                        itemBuilder.setLore(List.of("&eMap: &b" + map,
                                "&eTime: &a" + stateObject.get("time").getAsInt() + "s",
                                "&d" + playersObject.get("playing").getAsInt() + "&e/&5" + server.getMaxPlayers()));
                    } else if (state.equalsIgnoreCase("MAP_EDITING")) {
                        itemBuilder.material(XMaterial.MAP);
                        itemBuilder.displayName("&d" + server.getName());
                        itemBuilder.addLoreLine("&7Maps are being edited.");
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
                        JsonObject playersObject = stateObject.getAsJsonObject("teamCounts");

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
                        if (NexusReborn.NETWORK_TYPE == NetworkType.SINGLE && Bukkit.getPluginManager().getPlugin("SurvivalGames") != null) {
                            plugin.getHubServer().quit(e.getWhoClicked().getUniqueId());
                            plugin.getHubChatRoom().removeMember(e.getWhoClicked().getUniqueId());
                            ServerSelectEvent serverSelectEvent = new ServerSelectEvent(e.getWhoClicked().getUniqueId(), NexusReborn.getPlayerManager().getNexusPlayer(e.getWhoClicked().getUniqueId()), server.getName());
                            Bukkit.getServer().getPluginManager().callEvent(serverSelectEvent);
                            if (serverSelectEvent.isCancelled()) {
                                e.getWhoClicked().sendMessage(MsgType.ERROR.format(serverSelectEvent.getErrorMessage()));
                                plugin.getHubServer().join(e.getWhoClicked().getUniqueId());
                            }
                        }
                    });
            addElement(button);
        }
    }
}
