package com.thenexusreborn.hub.menu;

import com.stardevllc.smaterial.SMaterial;
import com.stardevllc.staritems.ItemBuilders;
import com.stardevllc.ui.GuiManager;
import com.stardevllc.ui.element.Element;
import com.stardevllc.ui.element.button.Button;
import com.stardevllc.ui.gui.InventoryGUI;
import com.stardevllc.ui.gui.UpdatingGUI;
import com.thenexusreborn.api.NexusReborn;
import com.thenexusreborn.api.server.NexusServer;
import com.thenexusreborn.hub.NexusHub;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.*;

public class SGMenu extends InventoryGUI implements UpdatingGUI {

//    private static final Gson GSON = new GsonBuilder().create();
    
    private NexusHub plugin;
        
    public SGMenu(NexusHub plugin, UUID player) {
        super("&6&lSurvival Games", player, new String[] {
                "B---M----", 
                "SSSSSSSSS", 
                "SSSSSSSSS", 
                "SSSSSSSSS", 
                "SSSSSSSSS",
                "SSSSSSSSS"
        });
        
        this.plugin = plugin;
        
        this.setDynamicChar('S');
        
        GuiManager manager = plugin.getServer().getServicesManager().getRegistration(GuiManager.class).getProvider();
        setElement('B', new Button(
                p -> ItemBuilders.of(SMaterial.ARROW).displayName("&6&l<< Back").addLoreLine("&6&lClick &fto go back to the game selector!").build(),
                e -> Bukkit.getScheduler().runTaskLater(plugin, () -> manager.openGUI(new GameBrowserMenu(plugin), e.getWhoClicked()), 1L),
                Sound.CLICK,
                0.5f
        ));
        
        setElement('M', new Element(p -> {
            int playersInSG = 0;
            for (NexusServer nexusServer : NexusReborn.getServerRegistry().values()) {
                if (nexusServer.getMode().equalsIgnoreCase("survivalgames")) {
                    playersInSG += nexusServer.getPlayers().size();
                }
            }
            return ItemBuilders.of(SMaterial.DIAMOND_SWORD).displayName("&a&lSurvival Games").setLore(List.of("&7Drop into the arena, gear up,", "&7and outplay your oponents", "&7in this high-stakes PvP showdown.", "&d&lPlayers &f" + playersInSG)).build();
        }));
        
        for (NexusServer nexusServer : NexusReborn.getServerRegistry().values()) {
            if (nexusServer.getMode().equalsIgnoreCase("survivalgames")) {
                this.addElement(new ServerElement(plugin, nexusServer));
            }
        }
        
        NexusReborn.getServerRegistry().addRegisterListener(e -> handleRegistryEvents(e.value(), e.oldValue()));
    }
    
    private void handleRegistryEvents(NexusServer added, NexusServer removed) {
        if (added != null) {
            if (added.getMode().equalsIgnoreCase("survivalgames")) {
                addElement(new ServerElement(plugin, added));
            }
        } else if (removed != null) {
            if (removed.getMode().equalsIgnoreCase("survivalgames")) {
                //This should work because I have an equals and hashcode method on the name of the server in the ServerElement class
                dynamicElements.remove(new ServerElement(plugin, removed));
            }
        }
    }
}