package com.github.thelampgod.archiveLocale.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.thelampgod.archiveLocale.ArchiveLocale;
import com.github.thelampgod.archiveLocale.InventoryTranslator;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class SlotListener extends PacketAdapter {

    public SlotListener(ArchiveLocale plugin, PacketType... types) {
        super(plugin, types);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Locale locale = ArchiveLocale.INSTANCE.getLocaleManager().get(event.getPlayer());
        // Get the slot array (Slot[])
        List<ItemStack> slots = packet.getItemListModifier().read(0);

        InventoryTranslator translator = new InventoryTranslator((ArchiveLocale) this.plugin);
        translator.translateInventoryItems(slots, locale);

        packet.getItemListModifier().write(0, slots);
    }
}
