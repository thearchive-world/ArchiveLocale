package com.github.thelampgod.archiveLocale;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.thelampgod.archiveLocale.commands.impl.SetLocaleCommand;
import com.github.thelampgod.archiveLocale.listeners.PlayerJoinListener;
import com.github.thelampgod.archiveLocale.listeners.SlotListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArchiveLocale extends JavaPlugin {

    public static ArchiveLocale INSTANCE;
    private ProtocolManager protocolManager;

    private LocaleManager localeManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        // Plugin startup logic
        protocolManager = ProtocolLibrary.getProtocolManager();

        localeManager = new LocaleManager();
        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        getCommand("SetLocale").setExecutor(new SetLocaleCommand());
    }

    private void registerListeners() {
        protocolManager.addPacketListener(new SlotListener(this, PacketType.Play.Server.WINDOW_ITEMS));
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }


    public LocaleManager getLocaleManager() {
        return localeManager;
    }
}
