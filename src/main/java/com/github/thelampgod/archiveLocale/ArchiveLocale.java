package com.github.thelampgod.archiveLocale;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.thelampgod.archiveLocale.commands.impl.SetLocaleCommand;
import com.github.thelampgod.archiveLocale.listeners.PlayerJoinListener;
import com.github.thelampgod.archiveLocale.listeners.PlayerListListener;
import com.github.thelampgod.archiveLocale.listeners.SlotListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArchiveLocale extends JavaPlugin {

    public static ArchiveLocale INSTANCE;
    private ProtocolManager protocolManager;

    private LocaleManager localeManager;
    private TranslationManager translationManager;
    private PlayerListCache cache;

    @Override
    public void onEnable() {
        INSTANCE = this;
        // Plugin startup logic
        protocolManager = ProtocolLibrary.getProtocolManager();

        localeManager = new LocaleManager();
        translationManager = new TranslationManager(this);
        cache = new PlayerListCache();
        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        getCommand("SetLocale").setExecutor(new SetLocaleCommand());
    }

    //todo: translate chat? signs?
    private void registerListeners() {
        protocolManager.addPacketListener(new SlotListener(this,
                PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Server.OPEN_WINDOW
        ));
        protocolManager.addPacketListener(new PlayerListListener(this, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER));
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }


    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public TranslationManager getTranslationManager() {
        return translationManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public PlayerListCache getPlayerListCache() {
        return cache;
    }
}
