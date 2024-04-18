package com.thenexusreborn.hub.menu;

import com.cryptomorin.xseries.XMaterial;
import com.stardevllc.starcore.gui.GuiManager;
import com.stardevllc.starcore.gui.element.button.Button;
import com.stardevllc.starcore.gui.gui.InventoryGUI;
import com.stardevllc.starcore.item.ItemBuilder;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GameBrowserMenu extends InventoryGUI {
    public GameBrowserMenu(NexusHub plugin) {
        super(1, "&d&lThe Nexus &r- Where to?");
        GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
        Button sgButton = new Button().clickSound(Sound.CLICK, 1L)
                .iconCreator(player -> ItemBuilder.of(XMaterial.DIAMOND_SWORD).displayName("&6Survival Games").build())
                .consumer(e -> Bukkit.getScheduler().runTaskLater(plugin, () -> manager.openGUI(new SGMenu(plugin), (Player) e.getWhoClicked()), 1L));
        setElement(0, 4, sgButton);
    }
}
