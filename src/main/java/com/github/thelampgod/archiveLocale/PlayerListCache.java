package com.github.thelampgod.archiveLocale;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerListCache {

    private final Map<Player, PacketContainer> playerToTabPacketCache = new HashMap<>();
    private static PacketContainer DEFAULT;

    public void put(Player player, PacketContainer packet) {
        if (DEFAULT == null) {
            DEFAULT = packet;
            return;
        }
        if (!packet.equals(DEFAULT)) {
            playerToTabPacketCache.put(player, packet);
        }
    }

    public PacketContainer get(Player player) {
        return playerToTabPacketCache.getOrDefault(player, DEFAULT);
    }
}
