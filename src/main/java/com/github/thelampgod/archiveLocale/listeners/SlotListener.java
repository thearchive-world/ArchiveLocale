package com.github.thelampgod.archiveLocale.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.thelampgod.archiveLocale.ArchiveLocale;
import com.github.thelampgod.archiveLocale.InventoryTranslator;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class SlotListener extends PacketAdapter {

    private final InventoryTranslator translator;

    public SlotListener(ArchiveLocale plugin, PacketType... types) {
        super(plugin, types);
        this.translator = new InventoryTranslator(plugin);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType packetType = event.getPacketType();
        if (packetType == PacketType.Play.Server.WINDOW_ITEMS) {
            handleSetContent(event);
        } else if (packetType == PacketType.Play.Server.SET_SLOT) {
            handleSetSlot(event);
        } else if (packetType == PacketType.Play.Server.OPEN_WINDOW) {
            handleOpenWindow(event);
        }
    }

    private void handleSetContent(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Locale locale = ArchiveLocale.INSTANCE.getLocaleManager().get(event.getPlayer());
        // Get the slot array (Slot[])
        List<ItemStack> slots = packet.getItemListModifier().read(0);

        translator.translateInventoryItems(slots, locale);

        packet.getItemListModifier().write(0, slots);
    }

    private void handleSetSlot(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Locale locale = ArchiveLocale.INSTANCE.getLocaleManager().get(event.getPlayer());

        ItemStack stack = packet.getItemModifier().read(0);
        translator.translateStack(stack, locale);

        packet.getItemModifier().write(0, stack);
    }

    private void handleOpenWindow(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Locale locale = ArchiveLocale.INSTANCE.getLocaleManager().get(event.getPlayer());
        WrappedChatComponent title = packet.getChatComponents().read(0);
        translator.translateTitle(title, locale);

        packet.getChatComponents().write(0, title);
    }
}
