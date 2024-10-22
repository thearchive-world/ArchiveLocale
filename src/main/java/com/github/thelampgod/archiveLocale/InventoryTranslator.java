package com.github.thelampgod.archiveLocale;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryTranslator {

    private final ArchiveLocale plugin;

    public InventoryTranslator(ArchiveLocale plugin) {
        this.plugin = plugin;
    }

    public void translateInventoryItems(List<ItemStack> slots, Locale locale) {
        for (ItemStack item : slots) {
            translateStack(item, locale);
        }
    }

    public void translateStack(ItemStack item, Locale locale) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // Translate Display Name
        if (meta.hasDisplayName()) {
            Component displayName = meta.displayName();

            String translationKey = PlainTextComponentSerializer.plainText().serialize(displayName);
            String translatedName = plugin.getTranslationManager().translate(translationKey, locale);

            TextReplacementConfig nameConfig = TextReplacementConfig.builder()
                    .matchLiteral(translationKey)
                    .replacement(translatedName)
                    .build();

            displayName = displayName.replaceText(nameConfig);
            meta.displayName(displayName);
        }

        // Translate Lore
        if (meta.hasLore()) {
            List<Component> lore = meta.lore();
            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    Component loreLine = lore.get(i);

                    String loreTranslationKey = PlainTextComponentSerializer.plainText().serialize(loreLine);
                    String translatedLore = plugin.getTranslationManager().translate(loreTranslationKey, locale);


                    TextReplacementConfig loreConfig = TextReplacementConfig.builder()
                            .matchLiteral(loreTranslationKey)
                            .replacement(translatedLore)
                            .build();

                    loreLine = loreLine.replaceText(loreConfig);
                    lore.set(i, loreLine);
                }
            }
            meta.lore(lore);
        }

        item.setItemMeta(meta);
    }

    public void translateTitle(WrappedChatComponent title, Locale locale) {
        String titleKey = title.getJson();

        String translatedText = translateKeysInString(titleKey, locale);

        ArchiveLocale.INSTANCE.getLogger().info(translatedText);
        title.setJson(translatedText);
    }

    private String translateKeysInString(String text, Locale locale) {
        Pattern pattern = Pattern.compile("archive\\.[a-zA-Z0-9._]+"); // match anything string that starts with "archive."
        Matcher matcher = pattern.matcher(text);

        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            // Get the found translation key
            String translationKey = matcher.group(0);
            // Translate the key
            String translatedKey = plugin.getTranslationManager().translate(translationKey, locale);

            // Append the text before the key and the translated key
            result.append(text, lastEnd, matcher.start());
            result.append(translatedKey);
            lastEnd = matcher.end();
        }

        // Append any remaining text after the last match
        result.append(text.substring(lastEnd));

        return result.toString();
    }
}
