package com.github.thelampgod.archiveLocale.commands.impl;

import com.github.thelampgod.archiveLocale.ArchiveLocale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SetLocaleTabCompleter implements TabCompleter {

    private final Map<String, Locale> availableLocales;

    public SetLocaleTabCompleter() {
        this.availableLocales = ArchiveLocale.INSTANCE.getTranslationManager().getAvailLocales();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList(); // Only players can change locale
        }

        // Combine args to handle locales with spaces
        String currentInput = String.join(" ", args).toLowerCase();

        // If no input, return all locales
        if (currentInput.isEmpty()) {
            return new ArrayList<>(availableLocales.keySet());
        }

        // Filter locales based on current input
        return availableLocales.keySet().stream()
                .filter(name -> name.toLowerCase().startsWith(currentInput))
                .collect(Collectors.toList());
    }
}
