package me.hextech.remapped;

import me.hextech.remapped.Event;
import me.hextech.remapped.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.sound.SoundInstance;

public class PlaySoundEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    public final SoundInstance sound;

    public PlaySoundEvent(SoundInstance soundInstance) {
        super(Event.Pre);
        this.sound = soundInstance;
    }
}
