package com.thenexusreborn.hub.menu;

import com.stardevllc.starcore.api.itembuilder.ItemBuilder;
import com.stardevllc.starmclib.XMaterial;
import com.stardevllc.starui.GuiManager;
import com.stardevllc.starui.element.button.Button;
import com.stardevllc.starui.gui.InventoryGUI;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class GameBrowserMenu extends InventoryGUI {
    public GameBrowserMenu(NexusHub plugin) {
        super(1, "&d&lThe Nexus &r- Where to?");
        GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
        Button sgButton = new Button().clickSound(Sound.CLICK, 1L)
                .iconCreator(player -> ItemBuilder.of(XMaterial.DIAMOND_SWORD).displayName("&6Survival Games").build())
                .consumer(e -> Bukkit.getScheduler().runTaskLater(plugin, () -> manager.openGUI(new SGMenu(plugin, e.getWhoClicked().getUniqueId()), e.getWhoClicked()), 1L));
        setElement(0, 4, sgButton);
    }
}
