package com.thenexusreborn.hub.menu;

import com.google.common.io.*;
import com.thenexusreborn.api.*;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.menu.element.button.Button;
import com.thenexusreborn.nexuscore.menu.gui.Menu;
import com.thenexusreborn.nexuscore.util.builder.ItemBuilder;
import org.bukkit.*;

public class GameBrowserMenu extends Menu {
    public GameBrowserMenu(NexusHub plugin) {
        super(plugin, "gamebrowser", "&d&lThe Nexus &r- Where to?", 1);
        Button sgButton = new Button(ItemBuilder.start(Material.DIAMOND_SWORD).displayName("&6Survival Games").build(), Sound.CLICK);
        sgButton.setLeftClickAction((player, menu, click) -> {
            if (NexusAPI.getApi().getEnvironment() == Environment.DEVELOPMENT) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("SG1");
                player.sendPluginMessage(NexusCore.getPlugin(NexusCore.class), "BungeeCord", out.toByteArray());
            } else {
                player.openInventory(new SGMenu(plugin).getInventory());
            }
        });
        setElement(4, sgButton);
    }
}
