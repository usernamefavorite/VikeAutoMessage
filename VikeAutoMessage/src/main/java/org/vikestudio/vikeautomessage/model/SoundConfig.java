package org.vikestudio.vikeautomessage.model;

public class SoundConfig {

    private final boolean enabled;
    private final String type;
    private final float volume;
    private final float pitch;

    public SoundConfig(boolean enabled, String type, float volume, float pitch) {
        this.enabled = enabled;
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getType() {
        return type;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}