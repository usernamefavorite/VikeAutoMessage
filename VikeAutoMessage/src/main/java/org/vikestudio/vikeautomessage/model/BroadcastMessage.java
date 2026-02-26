package org.vikestudio.vikeautomessage.model;

import java.util.List;

public class BroadcastMessage {

    private final int id;
    private final String permission;
    private final List<String> messages;
    private final String clickUrl;
    private final List<String> blacklistedWorlds;

    public BroadcastMessage(int id, String permission, List<String> messages, String clickUrl, List<String> blacklistedWorlds) {
        this.id = id;
        this.permission = permission;
        this.messages = messages;
        this.clickUrl = clickUrl;
        this.blacklistedWorlds = blacklistedWorlds;
    }

    public int getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public List<String> getBlacklistedWorlds() {
        return blacklistedWorlds;
    }
}
