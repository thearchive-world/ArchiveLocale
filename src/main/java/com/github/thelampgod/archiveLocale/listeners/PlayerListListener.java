package com.github.thelampgod.archiveLocale.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.thelampgod.archiveLocale.ArchiveLocale;
import com.github.thelampgod.archiveLocale.Translator;

import java.util.Locale;

public class PlayerListListener extends PacketAdapter {

    private final Translator translator;

    public PlayerListListener(ArchiveLocale plugin, PacketType... types) {
        super(plugin, types);
        this.translator = new Translator(plugin);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ((ArchiveLocale) plugin).getPlayerListCache().put(event.getPlayer(), packet.deepClone());

        Locale locale = ArchiveLocale.INSTANCE.getLocaleManager().get(event.getPlayer());

        var header = packet.getChatComponents().read(0);
        var footer = packet.getChatComponents().read(1);

        translator.translateTextComponent(header, locale);
        translator.translateTextComponent(footer, locale);

        packet.getChatComponents().write(0, header);
        packet.getChatComponents().write(1, footer);
    }
}
