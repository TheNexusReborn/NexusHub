package com.thenexusreborn.hub.menu;

import com.stardevllc.starui.GuiManager;
import com.stardevllc.starui.element.button.Button;
import com.stardevllc.starui.gui.InventoryGUI;
import com.thenexusreborn.hub.NexusHub;
import com.thenexusreborn.nexuscore.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GameBrowserMenu extends InventoryGUI {
    public GameBrowserMenu(NexusHub plugin) {
        super(1, "&d&lThe Nexus &r- Where to?");
        GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
        Button sgButton = new Button().clickSound(Sound.CLICK, 1L)
                .iconCreator(player -> ItemBuilder.start(Material.DIAMOND_SWORD).displayName("&6Survival Games").build())
                .consumer(e -> manager.openGUI(this, (Player) e.getWhoClicked()));
        setElement(0, 4, sgButton);
    }
}
