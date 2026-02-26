package org.vikestudio.vikeautomessage.manager;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.vikestudio.vikeautomessage.model.BroadcastMessage;
import org.vikestudio.vikeautomessage.model.SoundConfig;

import java.util.ArrayList;
import java.util.List;

public class BroadcastManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private BukkitTask broadcastTask;

    public BroadcastManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void start() {
        if (broadcastTask != null) {
            broadcastTask.cancel();
        }

        long intervalTicks = configManager.getInterval() * 20;

        broadcastTask = Bukkit.getScheduler().runTaskTimer(plugin, this::broadcast, intervalTicks, intervalTicks);
    }

    public void stop() {
        if (broadcastTask != null) {
            broadcastTask.cancel();
            broadcastTask = null;
        }
    }

    private void broadcast() {
        BroadcastMessage broadcastMessage = getBroadcastMessage();

        if (broadcastMessage == null) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hasPermission(player, broadcastMessage)) {
                continue;
            }

            if (isWorldBlacklisted(player, broadcastMessage)) {
                continue;
            }

            sendMessage(player, broadcastMessage);
            playSound(player);
        }
    }

    private BroadcastMessage getBroadcastMessage() {
        String format = configManager.getFormat();

        if ("RANDOM".equals(format)) {
            return configManager.getRandomMessage();
        }

        return configManager.getMessages().values().stream().findFirst().orElse(null);
    }

    private boolean hasPermission(Player player, BroadcastMessage message) {
        String permission = message.getPermission();

        if (permission == null || permission.isEmpty()) {
            return true;
        }

        return player.hasPermission(permission);
    }

    private boolean isWorldBlacklisted(Player player, BroadcastMessage message) {
        return message.getBlacklistedWorlds().contains(player.getWorld().getName());
    }

    private void sendMessage(Player player, BroadcastMessage message) {
        String playerName = player.getName();

        for (String line : message.getMessages()) {
            String formattedLine = line.replace("%player_name%", playerName);
            TextComponent component = new TextComponent(formattedLine);

            if (!message.getClickUrl().isEmpty()) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.getClickUrl()));
            }

            player.spigot().sendMessage(component);
        }
    }

    private void playSound(Player player) {
        SoundConfig soundConfig = configManager.getSoundConfig();

        if (!soundConfig.isEnabled()) {
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundConfig.getType());
            player.playSound(player.getLocation(), sound, soundConfig.getVolume(), soundConfig.getPitch());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Неизвестный тип звука: " + soundConfig.getType());
        }
    }
}