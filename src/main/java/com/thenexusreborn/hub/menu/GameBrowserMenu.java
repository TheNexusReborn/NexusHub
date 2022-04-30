package com.thenexusreborn.hub.menu;

import com.google.common.io.*;
import com.thenexusreborn.nexuscore.NexusCore;
import com.thenexusreborn.nexuscore.menu.element.button.Button;
import com.thenexusreborn.nexuscore.menu.gui.Menu;
import com.thenexusreborn.nexuscore.util.builder.ItemBuilder;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class GameBrowserMenu extends Menu {
    public GameBrowserMenu(JavaPlugin plugin) {
        super(plugin, "gamebrowser", "&d&lThe Nexus &r- Where to?", 1);
        Button sgButton = new Button(ItemBuilder.start(Material.DIAMOND_SWORD).displayName("&6Survival Games").build(), Sound.CLICK);
        sgButton.setLeftClickAction((player, menu, click) -> {
            //TODO proper server detection
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("SG1");
            player.sendPluginMessage(NexusCore.getPlugin(NexusCore.class), "BungeeCord", out.toByteArray());
        });
        setElement(4, sgButton);
    }
}
