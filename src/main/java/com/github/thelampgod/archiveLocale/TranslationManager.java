package com.github.thelampgod.archiveLocale;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TranslationManager {

    private static final Locale DEFAULT_LOCALE = Locale.US;
    private final JavaPlugin plugin;
    private final Map<Locale, Map<String, String>> translations = new HashMap<>();

    public TranslationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Load localization file into memory
     * @param locale the locale
     * @return true on success
     */
    public boolean loadTranslations(Locale locale) {
        if (translations.containsKey(locale)) return true; // locale already loaded

        String localeFile = "assets/archivelocale/lang/" + locale.toString() + ".json";
        InputStream inputStream = plugin.getResource(localeFile);

        if (inputStream == null) {
            plugin.getLogger().severe("Translation file for " + locale + " not found.");
            return false;
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            Map<String, String> localeTranslations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
            translations.put(locale, localeTranslations);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load translations for " + locale + ": " + e.getMessage());
            return false;
        }

        return true;
    }

    public String translate(String key, Locale locale) {
        if (!(translations.containsKey(locale))) {
            locale = DEFAULT_LOCALE;
        }

        Map<String, String> localeTranslations = translations.get(locale);
        return localeTranslations.getOrDefault(key, key);
    }
}

