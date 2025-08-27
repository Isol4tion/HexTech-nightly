package me.hextech.remapped;

import net.minecraft.client.sound.SoundInstance;

public class PlaySoundEvent extends Event_auduwKaxKOWXRtyJkCPb {
   public final SoundInstance sound;

   public PlaySoundEvent(SoundInstance soundInstance) {
      super(Event.Pre);
      this.sound = soundInstance;
   }
}
