package com.thenexusreborn.hub.menu;

import com.stardevllc.starcore.api.itembuilder.ItemBuilders;
import com.stardevllc.starcore.api.ui.GuiManager;
import com.stardevllc.starcore.api.ui.element.Element;
import com.stardevllc.starcore.api.ui.element.button.Button;
import com.stardevllc.starcore.api.ui.gui.InventoryGUI;
import com.stardevllc.starcore.api.ui.gui.UpdatingGUI;
import com.stardevllc.starmclib.XMaterial;
import com.thenexusreborn.api.NexusReborn;
import com.thenexusreborn.api.server.NexusServer;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.*;

public class SGMenu extends InventoryGUI implements UpdatingGUI {

//    private static final Gson GSON = new GsonBuilder().create();
        
    public SGMenu(NexusHub plugin, UUID player) {
        super("&6&lSurvival Games", player, new String[] {
                "B---M----", 
                "SSSSSSSSS", 
                "SSSSSSSSS", 
                "SSSSSSSSS", 
                "SSSSSSSSS",
                "SSSSSSSSS"
        });
        
        this.setDynamicChar('S');
        
        GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
        setElement('B', new Button(
                p -> ItemBuilders.of(XMaterial.ARROW).displayName("&6&l<< Back").addLoreLine("&6&lClick &fto go back to the game selector!").build(),
                e -> Bukkit.getScheduler().runTaskLater(plugin, () -> manager.openGUI(new GameBrowserMenu(plugin), e.getWhoClicked()), 1L),
                Sound.CLICK,
                0.5f
        ));
        
        setElement('M', new Element(p -> {
            int playersInSG = 0;
            for (NexusServer nexusServer : NexusReborn.getServerRegistry()) {
                if (nexusServer.getMode().equalsIgnoreCase("survivalgames")) {
                    playersInSG += nexusServer.getPlayers().size();
                }
            }
            return ItemBuilders.of(XMaterial.DIAMOND_SWORD).displayName("&a&lSurvival Games").setLore(List.of("&7Drop into the arena, gear up,", "&7and outplay your oponents", "&7in this high-stakes PvP showdown.", "&d&lPlayers &f" + playersInSG)).build();
        }));
        
        for (NexusServer nexusServer : NexusReborn.getServerRegistry()) {
            if (nexusServer.getMode().equalsIgnoreCase("survivalgames")) {
                this.addElement(new ServerElement(plugin, nexusServer));
            }
        }
        
        NexusReborn.getServerRegistry().addRegisterListener((name, nexusServer) -> {
            if (nexusServer.getMode().equalsIgnoreCase("survivalgames")) {
                addElement(new ServerElement(plugin, nexusServer));
            }
        });
        
        NexusReborn.getServerRegistry().addUnregisterListener((name, nexusServer) -> {
            if (nexusServer.getMode().equalsIgnoreCase("survivalgames")) {
                //This should work because I have an equals and hashcode method on the name of the server in the ServerElement class
                dynamicElements.remove(new ServerElement(plugin, nexusServer));
            }
        });
    }
}