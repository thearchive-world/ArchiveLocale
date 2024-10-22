package com.github.thelampgod.archiveLocale.listeners;

import com.github.thelampgod.archiveLocale.ArchiveLocale;
import com.github.thelampgod.archiveLocale.LocaleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Locale;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final LocaleManager manager = ArchiveLocale.INSTANCE.getLocaleManager();
        Locale savedLocale = manager.get(player);

        if (savedLocale == null) {
            manager.put(player, player.locale());
        }
    }
}
