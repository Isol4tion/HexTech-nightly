package me.hextech.remapped;

import java.util.ArrayList;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PlaySoundEvent;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class NoSoundLag
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    static final ArrayList<SoundEvent> armor = new ArrayList();
    public static NoSoundLag INSTANCE;
    private final BooleanSetting equip = this.add(new BooleanSetting("ArmorEquip", true));
    private final BooleanSetting explode = this.add(new BooleanSetting("Explode", true));
    private final BooleanSetting attack = this.add(new BooleanSetting("Attack", true));

    public NoSoundLag() {
        super("NoSoundLag", Module_JlagirAibYQgkHtbRnhw.Misc);
        INSTANCE = this;
    }

    @EventHandler
    public void onPlaySound(PlaySoundEvent event) {
        if (this.equip.getValue()) {
            for (SoundEvent se : armor) {
                if (event.sound.method_4775() != se.method_14833()) continue;
                event.cancel();
                return;
            }
        }
        if (this.explode.getValue() && event.sound.method_4775() == SoundEvents.field_15152.method_14833()) {
            event.cancel();
            return;
        }
        if (this.attack.getValue() && (event.sound.method_4775() == SoundEvents.field_14625.method_14833() || event.sound.method_4775() == SoundEvents.field_14840.method_14833())) {
            event.cancel();
        }
    }
}
