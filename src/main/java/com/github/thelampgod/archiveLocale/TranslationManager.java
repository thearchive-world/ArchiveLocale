package com.github.thelampgod.archiveLocale;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TranslationManager {

    private static final Locale DEFAULT_LOCALE = Locale.US;
    private final JavaPlugin plugin;
    private final Map<Locale, Map<String, String>> translations = new HashMap<>();

    public TranslationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadTranslations(DEFAULT_LOCALE);
    }

    public List<String> getAvailLocales() {
        List<String> locales = new ArrayList<>();
        try {
            Enumeration<URL> urls = plugin.getClass().getClassLoader().getResources("assets/archivelocale/lang/");

            // Check if it's a jar connection
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                URLConnection connection = url.openConnection();
                if (connection instanceof java.net.JarURLConnection) {
                    JarFile jarFile = ((java.net.JarURLConnection) connection).getJarFile();
                    jarFile.stream()
                            .filter(e -> e.getName().startsWith("assets/archivelocale/lang/") && !e.isDirectory())
                            .map(e -> e.getName().split("assets/archivelocale/lang/")[1])
                            .map(e -> e.split("\\.")[0])
                            .forEach(locales::add);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locales;
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

        plugin.getLogger().info("Loaded " + locale.toString() + " lang");
        return true;
    }

    public String translate(String key, Locale locale) {
        if (!key.startsWith("archive.")) return key;
        if (!(translations.containsKey(locale))) {
            locale = DEFAULT_LOCALE;
        }

        Map<String, String> localeTranslations = translations.get(locale);
        return localeTranslations.getOrDefault(key, fallback(key));
    }

    private String fallback(String key) {
        Map<String, String> localeTranslations = translations.get(DEFAULT_LOCALE);
        return localeTranslations.getOrDefault(key, key);
    }
}

