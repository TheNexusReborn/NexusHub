package com.thenexusreborn.hub.menu;

import com.google.common.io.*;
import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.helper.StringHelper;
import com.thenexusreborn.api.server.ServerInfo;
import com.thenexusreborn.api.tournament.Tournament;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.menu.element.button.Button;
import com.thenexusreborn.nexuscore.menu.gui.Menu;
import com.thenexusreborn.nexuscore.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SGMenu extends Menu {
    public SGMenu(NexusHub plugin) {
        super(plugin, "sg", "&6&lSurvival Games", 1);
    
        for (ServerInfo server : new ArrayList<>(NexusAPI.getApi().getServerManager().getServers())) {
            Material material;
            if (server.getType().equalsIgnoreCase("sg")) {
                if (server.getStatus().equalsIgnoreCase("online")) {
                    if (server.getState().equalsIgnoreCase("lobby:waiting") || server.getState().equalsIgnoreCase("lobby:countdown")) {
                        material = Material.EMERALD_BLOCK;
                    } else {
                        material = Material.GOLD_BLOCK;
                    }
                } else {
                    material = Material.BEDROCK;
                }
            } else {
                continue;
            }
    
            ItemBuilder itemBuilder = ItemBuilder.start(material).displayName("&6" + server.getName());
            
            if (material != Material.BEDROCK) {
                try {
                    String[] stateSplit = server.getState().split(":");
                    String stage = stateSplit[0];
                    String state = stateSplit[1];
                    List<String> lore = new LinkedList<>();
                    lore.add("&a" + StringHelper.capitalizeEveryWord(stage) + " &7- &e" + state);
                    lore.add("&d" + server.getPlayers() + "&5/24");
                    Tournament tournament = NexusAPI.getApi().getTournament();
                    if (tournament != null && tournament.isActive()) {
                        for (String tournamentServer : tournament.getServers()) {
                            if (tournamentServer.equalsIgnoreCase("all") || tournamentServer.equalsIgnoreCase("network") || tournamentServer.equalsIgnoreCase(NexusAPI.getApi().getServerManager().getCurrentServer().getName())) {
                                lore.add("");
                                lore.add("&a&lTOURNAMENT");
                                break;
                            }
                        }
                    }
                    itemBuilder.lore(lore);
                } catch (Exception e) {
                    itemBuilder.lore("&cError");
                }
            } else {
                itemBuilder.lore("&cOffline");
            }
    
            ItemStack itemStack = itemBuilder.build();
            Button button = new Button(itemStack);
            button.setLeftClickAction((player, menu, clickType) -> {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(server.getName());
                player.sendPluginMessage(NexusCore.getPlugin(NexusCore.class), "BungeeCord", out.toByteArray());
            });
            addElement(button);
        }
    }
}
