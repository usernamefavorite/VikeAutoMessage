package org.vikestudio.vikeautomessage;

import org.bukkit.plugin.java.JavaPlugin;
import org.vikestudio.vikeautomessage.manager.ConfigManager;
import org.vikestudio.vikeautomessage.manager.BroadcastManager;

public class VikeAutoMessage extends JavaPlugin {

    private ConfigManager configManager;
    private BroadcastManager broadcastManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        broadcastManager = new BroadcastManager(this, configManager);
        broadcastManager.start();

        getLogger().info("§aVikeAutoMessage Включен!");
    }

    @Override
    public void onDisable() {
        if (broadcastManager != null) {
            broadcastManager.stop();
        }
        getLogger().info("§cVikeAutoMessage Выключен!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BroadcastManager getBroadcastManager() {
        return broadcastManager;
    }
}