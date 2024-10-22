package com.github.thelampgod.archiveLocale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;

public class InventoryTranslator {

    private final ArchiveLocale plugin;

    public InventoryTranslator(ArchiveLocale plugin) {
        this.plugin = plugin;
    }

    public void translateInventoryItems(List<ItemStack> slots, Locale locale) {
        for (ItemStack item : slots) {
            if (item == null) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

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
    }
}
