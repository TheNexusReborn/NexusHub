package com.thenexusreborn.hub.menu;

import com.stardevllc.starui.element.button.Button;
import com.stardevllc.starui.gui.InventoryGUI;
import com.thenexusreborn.api.NexusAPI;
import com.thenexusreborn.api.helper.StringHelper;
import com.thenexusreborn.api.server.ServerInfo;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SGMenu extends InventoryGUI {
    public SGMenu(NexusHub plugin) {
        super(1, "&6&lSurvival Games");

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
                    itemBuilder.lore(lore);
                } catch (Exception e) {
                    itemBuilder.lore("&cError");
                }
            } else {
                itemBuilder.lore("&cOffline");
            }

            ItemStack itemStack = itemBuilder.build();
            Button button = new Button()
                    .iconCreator(player -> itemStack)
                    .consumer(e -> {
                       //TODO
                    });
            addElement(button);
        }
    }
}
