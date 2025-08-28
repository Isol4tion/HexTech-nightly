package me.hextech.mod.modules.impl.misc;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PlaySoundEvent;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class NoSoundLag
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    static final ArrayList<SoundEvent> armor = new ArrayList();
    public static NoSoundLag INSTANCE;
    private final BooleanSetting equip = this.add(new BooleanSetting("ArmorEquip", true));
    private final BooleanSetting explode = this.add(new BooleanSetting("Explode", true));
    private final BooleanSetting attack = this.add(new BooleanSetting("Attack", true));

    public NoSoundLag() {
        super("NoSoundLag", Category.Misc);
        INSTANCE = this;
    }

    @EventHandler
    public void onPlaySound(PlaySoundEvent event) {
        if (this.equip.getValue()) {
            for (SoundEvent se : armor) {
                if (event.sound.getId() != se.getId()) continue;
                event.cancel();
                return;
            }
        }
        if (this.explode.getValue() && event.sound.getId() == SoundEvents.ENTITY_GENERIC_EXPLODE.getId()) {
            event.cancel();
            return;
        }
        if (this.attack.getValue() && (event.sound.getId() == SoundEvents.ENTITY_PLAYER_ATTACK_WEAK.getId() || event.sound.getId() == SoundEvents.ENTITY_PLAYER_ATTACK_STRONG.getId())) {
            event.cancel();
        }
    }
}
