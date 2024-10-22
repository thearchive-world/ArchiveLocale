package com.github.thelampgod.archiveLocale.listeners;

import com.github.thelampgod.archiveLocale.ArchiveLocale;
import com.github.thelampgod.archiveLocale.LocaleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Locale;
import java.util.NoSuchElementException;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final LocaleManager manager = ArchiveLocale.INSTANCE.getLocaleManager();
        var metaLocale = player.getMetadata("locale");

        try {
            manager.put(player, (Locale) metaLocale.getFirst().value());
        } catch (NoSuchElementException e) {
            //no saved locale, use player default
            manager.put(player, player.locale());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final LocaleManager manager = ArchiveLocale.INSTANCE.getLocaleManager();

        player.setMetadata("locale", new FixedMetadataValue(ArchiveLocale.INSTANCE, manager.get(player)));
        manager.remove(player);
    }
}
