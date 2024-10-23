package com.github.thelampgod.archiveLocale.commands.impl;

import com.comphenix.protocol.events.PacketContainer;
import com.github.thelampgod.archiveLocale.ArchiveLocale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class SetLocaleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return false;
        }

        Map<String, Locale> availableLocales = ArchiveLocale.INSTANCE.getTranslationManager().getAvailLocales();

        if (args.length == 0) {
            StringBuilder message = new StringBuilder("Choose between ");
            int i = 0;
            for (String localeName : availableLocales.keySet()) {
                message.append(localeName);
                if (i != availableLocales.size() - 1) {
                    message.append(", ");
                }
                i++;
            }
            sender.sendMessage(message.toString());
            return false;
        }

        // Combine args to handle locales with spaces
        String localeNameInput = String.join(" ", args).trim();

        // Find the Locale object by human-readable name
        Locale locale = findLocaleByName(localeNameInput, availableLocales);
        if (locale == null) {
            sender.sendMessage("Invalid locale. Use one of the following:");
            availableLocales.keySet().forEach(name -> sender.sendMessage("- " + name));
            return false;
        }

        if (!ArchiveLocale.INSTANCE.getTranslationManager().loadTranslations(locale)) {
            Component message = Component.text("Locale for " + locale + " not found. Feel free to contribute at ")
                    .append(Component.text("GitHub!")
                            .color(TextColor.fromHexString("#00A1E4"))
                            .clickEvent(ClickEvent.openUrl("https://github.com/thelampgod/ArchiveLocale"))
                            .hoverEvent(Component.text("Click to open")));

            player.sendMessage(message);
            return true;
        }

        ArchiveLocale.INSTANCE.getLocaleManager().put(player, locale);
        player.sendMessage("Locale set to " + locale.getDisplayName());

        player.updateInventory();
        // Update tab
        PacketContainer packet = ArchiveLocale.INSTANCE.getPlayerListCache().get(player);
        ArchiveLocale.INSTANCE.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    /**
     * Find a Locale object by its display name, case-insensitive
     * @param name Display name of the locale
     * @param locales Map of display names to Locale objects
     * @return Locale object if found, otherwise null
     */
    private Locale findLocaleByName(String name, Map<String, Locale> locales) {
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
