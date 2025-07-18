package com.thenexusreborn.hub.chat;

import com.stardevllc.starchat.rooms.ChatRoom;
import com.stardevllc.starmclib.actors.Actor;
import com.thenexusreborn.hub.NexusHub;

public class HubChatRoom extends ChatRoom {
    public HubChatRoom(NexusHub plugin) {
        super(plugin, Actor.of(plugin), "hub");
        senderFormat.set(plugin.getNexusCore().getGlobalChannel().getSenderFormat());
    }
}
