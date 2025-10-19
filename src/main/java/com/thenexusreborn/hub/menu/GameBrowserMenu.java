package com.thenexusreborn.hub.menu;

import com.stardevllc.itembuilder.ItemBuilders;
import com.stardevllc.smaterial.SMaterial;
import com.stardevllc.starcore.api.ui.GuiManager;
import com.stardevllc.starcore.api.ui.element.button.Button;
import com.stardevllc.starcore.api.ui.gui.InventoryGUI;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class GameBrowserMenu extends InventoryGUI {
    public GameBrowserMenu(NexusHub plugin) {
        super("&d&lThe Nexus &r- Where to?", null, new String[] {
                "----S----"
        });
        GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
        Button sgButton = new Button().clickSound(Sound.CLICK, 1L)
                .iconCreator(player -> ItemBuilders.of(SMaterial.DIAMOND_SWORD).displayName("&6Survival Games").build())
                .consumer(e -> Bukkit.getScheduler().runTaskLater(plugin, () -> manager.openGUI(new SGMenu(plugin, e.getWhoClicked().getUniqueId()), e.getWhoClicked()), 1L));
        setElement('S', sgButton);
    }
}
