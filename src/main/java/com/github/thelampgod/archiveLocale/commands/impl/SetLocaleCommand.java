package com.github.thelampgod.archiveLocale.commands.impl;

import com.github.thelampgod.archiveLocale.ArchiveLocale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SetLocaleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return false;
        }

        Locale locale;
        try {
            locale = Locale.forLanguageTag(args[0].replace("_", "-"));
        } catch (Exception e) {
            sender.sendMessage("Invalid locale format. Use language-country format like en_US or en-US.");
            return false;
        }

        ArchiveLocale.INSTANCE.getLocaleManager().put(player, locale);
        player.sendMessage("Locale set to " + locale.getDisplayName());

        return true;
    }
}
