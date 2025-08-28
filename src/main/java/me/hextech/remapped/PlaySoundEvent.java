package me.hextech.remapped;

import me.hextech.remapped.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.sound.SoundInstance;

public class PlaySoundEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public final SoundInstance sound;

    public PlaySoundEvent(SoundInstance soundInstance) {
        super(Stage.Pre);
        this.sound = soundInstance;
    }
}
