package com.github.thelampgod.archiveLocale;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleManager {

    private final Map<Player, Locale> playerToLocaleMap = new HashMap<>();

    public Locale get(Player player) {
        return playerToLocaleMap.get(player);
    }

    public void put(Player player, Locale locale) {
        playerToLocaleMap.put(player, locale);
    }
}
