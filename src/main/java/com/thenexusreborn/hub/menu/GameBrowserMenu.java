package com.thenexusreborn.hub.menu;

import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.menu.element.button.Button;
import com.thenexusreborn.nexuscore.menu.gui.Menu;
import com.thenexusreborn.nexuscore.util.builder.ItemBuilder;
import org.bukkit.*;

public class GameBrowserMenu extends Menu {
    public GameBrowserMenu(NexusHub plugin) {
        super(plugin, "gamebrowser", "&d&lThe Nexus &r- Where to?", 1);
        Button sgButton = new Button(ItemBuilder.start(Material.DIAMOND_SWORD).displayName("&6Survival Games").build(), Sound.CLICK);
        sgButton.setLeftClickAction((player, menu, click) -> player.openInventory(new SGMenu(plugin).getInventory()));
        setElement(4, sgButton);
    }
}
