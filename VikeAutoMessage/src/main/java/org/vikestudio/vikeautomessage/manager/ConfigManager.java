package org.vikestudio.vikeautomessage.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.vikestudio.vikeautomessage.model.BroadcastMessage;
import org.vikestudio.vikeautomessage.model.SoundConfig;

import java.io.File;
import java.util.*;

public class ConfigManager {

    private final JavaPlugin plugin;
    private YamlConfiguration config;
    private File configFile;

    private SoundConfig soundConfig;
    private String format;
    private long interval;
    private Map<Integer, BroadcastMessage> messages;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        loadSoundConfig();
        loadSettings();
        loadMessages();
    }

    private void loadSoundConfig() {
        if (!config.contains("sound")) {
            soundConfig = new SoundConfig(false, "entity.experience_orb.pickup", 0.1f, 0.8f);
            return;
        }

        boolean enabled = config.getBoolean("sound.enable", false);
        String type = config.getString("sound.type", "entity.experience_orb.pickup");
        float volume = (float) config.getDouble("sound.volume", 0.1);
        float pitch = (float) config.getDouble("sound.pitch", 0.8);

        soundConfig = new SoundConfig(enabled, type, volume, pitch);
    }

    private void loadSettings() {
        format = config.getString("settings.format", "RANDOM").toUpperCase();
        interval = config.getLong("settings.interval", 180);
    }

    private void loadMessages() {
        messages.clear();

        if (!config.contains("list")) {
            return;
        }

        for (String key : config.getConfigurationSection("list").getKeys(false)) {
            try {
                int id = Integer.parseInt(key);
                String path = "list." + key;

                String permission = config.getString(path + ".permission", "");
                List<String> messageLines = config.getStringList(path + ".messages");
                String clickUrl = config.getString(path + ".click-url", "");
                List<String> blacklistedWorlds = config.getStringList(path + ".blacklisted_worlds");

                BroadcastMessage message = new BroadcastMessage(
                        id,
                        permission,
                        messageLines,
                        clickUrl,
                        blacklistedWorlds
                );

                messages.put(id, message);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public SoundConfig getSoundConfig() {
        return soundConfig;
    }

    public String getFormat() {
        return format;
    }

    public long getInterval() {
        return interval;
    }

    public Map<Integer, BroadcastMessage> getMessages() {
        return messages;
    }

    public BroadcastMessage getRandomMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        List<Integer> keys = new ArrayList<>(messages.keySet());
        return messages.get(keys.get(new Random().nextInt(keys.size())));
    }
}