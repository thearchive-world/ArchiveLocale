package com.github.thelampgod.archiveLocale;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ArchiveLocale extends JavaPlugin {

    public static ArchiveLocale INSTANCE;
    private ProtocolManager protocolManager;

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        // Plugin startup logic
        protocolManager = ProtocolLibrary.getProtocolManager();

        registerSetSlotListener();
    }

    private void registerSetSlotListener() {
        protocolManager.addPacketListener(new PacketAdapter(this,
                PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                // Get the slot array (Slot[])
                List<ItemStack> slots = packet.getItemListModifier().read(0);

                // Loop through the slots and log item info
                for (int i = 0; i < slots.size(); i++) {
                    ItemStack item = slots.get(i);
                    if (item != null) {
                        var meta = item.getItemMeta();

                        if (meta == null) continue;
                        TextReplacementConfig config = TextReplacementConfig.builder()
                                .match("archive")
                                .replacement("translated")
                                .build();
                        if (meta.hasDisplayName()) {
                            var modifiedMeta = meta.displayName().replaceText(config);
                            meta.displayName(modifiedMeta);
                            this.getPlugin().getLogger().info("replaced title");
                        }

                        if (meta.hasLore()) {
                            List<Component> newLore = new ArrayList<>();
                            for (Component comp : meta.lore()) {
                                newLore.add(comp.replaceText(config));
                            }
                            meta.lore(newLore);
                            this.getPlugin().getLogger().info("replaced meta");
                        }

                        item.setItemMeta(meta);
                    }
                }
                packet.getItemListModifier().write(0, slots);
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
