package me.hextech.mod.modules.impl.movement;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.MoveEvent;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.mod.modules.impl.combat.HoleKickTest;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class BurrowMove
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static BurrowMove INSTANCE;
    public final SliderSetting wspeed = this.add(new SliderSetting("WebSpeed", 10.0, 0.0, 50.0, 0.01)).setSuffix("\u00f72");
    public final SliderSetting aSpeed = this.add(new SliderSetting("BlockSpeed", 10.02, 0.0, 30.0, 0.01));
    public final SliderSetting aForward = this.add(new SliderSetting("AnchorForward", 1.0, 0.0, 30.0, 0.25));
    public final SliderSetting bSpeed = this.add(new SliderSetting("currentPos", 10.0, 0.0, 30.0, 0.01));

    public BurrowMove() {
        super("BurrowMove", Category.Movement);
        INSTANCE = this;
    }

    @EventHandler
    public void onMove(MoveEvent event) {
        if (!EntityUtil.isInsideBlock()) {
            return;
        }
        double speed = AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null ? (HoleKickTest.isInWeb(BurrowMove.mc.player) ? this.wspeed.getValue() : this.aSpeed.getValue()) : this.bSpeed.getValue();
        double moveSpeed = 0.002873 * speed;
        double n = 0.0;
        if (BurrowMove.mc.player != null) {
            n = BurrowMove.mc.player.input.movementForward;
        }
        double n2 = 0.0;
        if (BurrowMove.mc.player != null) {
            n2 = BurrowMove.mc.player.input.movementSideways;
        }
        double n3 = 0.0;
        if (BurrowMove.mc.player != null) {
            n3 = BurrowMove.mc.player.getYaw();
        }
        if (n == 0.0 && n2 == 0.0) {
            if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos == null) {
                event.setX(0.0);
                event.setZ(0.0);
            } else {
                moveSpeed = 0.002873 * this.aForward.getValue();
                event.setX(1.0 * moveSpeed * -Math.sin(Math.toRadians(n3)));
                event.setZ(1.0 * moveSpeed * Math.cos(Math.toRadians(n3)));
            }
            return;
        }
        if (n != 0.0 && n2 != 0.0) {
            n *= Math.sin(0.7853981633974483);
            n2 *= Math.cos(0.7853981633974483);
        }
        event.setX(n * moveSpeed * -Math.sin(Math.toRadians(n3)) + n2 * moveSpeed * Math.cos(Math.toRadians(n3)));
        event.setZ(n * moveSpeed * Math.cos(Math.toRadians(n3)) - n2 * moveSpeed * -Math.sin(Math.toRadians(n3)));
    }
}
