package com.github.thelampgod.archiveLocale;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.thelampgod.archiveLocale.commands.impl.SetLocaleCommand;
import com.github.thelampgod.archiveLocale.commands.impl.SetLocaleTabCompleter;
import com.github.thelampgod.archiveLocale.listeners.PlayerJoinListener;
import com.github.thelampgod.archiveLocale.listeners.PlayerListListener;
import com.github.thelampgod.archiveLocale.listeners.SlotListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ArchiveLocale extends JavaPlugin {

    public static ArchiveLocale INSTANCE;
    private TranslationManager translationManager;
    private LocaleManager localeManager;
    private ProtocolManager protocolManager;
    private PlayerListCache playerListCache;

    @Override
    public void onEnable() {
        INSTANCE = this;
        translationManager = new TranslationManager(this);
        localeManager = new LocaleManager();
        protocolManager = ProtocolLibrary.getProtocolManager();
        playerListCache = new PlayerListCache();

        // Register command executor and tab completer
        this.getCommand("setlocale").setExecutor(new SetLocaleCommand());
        this.getCommand("setlocale").setTabCompleter(new SetLocaleTabCompleter());

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Register protocol listeners
        protocolManager.addPacketListener(new SlotListener(this));
        protocolManager.addPacketListener(new PlayerListListener(this));

        // Any other initialization code...
    }

    @Override
    public void onDisable() {
        // Any cleanup code...
    }

    // Getters for various managers and caches
    public TranslationManager getTranslationManager() {
        return translationManager;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public PlayerListCache getPlayerListCache() {
        return playerListCache;
    }
}
