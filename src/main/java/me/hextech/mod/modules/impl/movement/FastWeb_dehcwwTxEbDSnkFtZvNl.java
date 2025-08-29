package me.hextech.mod.modules.impl.movement;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.TimerEvent;
import me.hextech.api.utils.entity.MovementUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.HoleKickTest;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class FastWeb_dehcwwTxEbDSnkFtZvNl
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static FastWeb_dehcwwTxEbDSnkFtZvNl INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Vanilla));
    public final SliderSetting xZSlow = this.add(new SliderSetting("XZSpeed", 25.0, 0.0, 100.0, 0.1, v -> this.mode.getValue() == Mode.Custom).setSuffix("%"));
    public final SliderSetting ySlow = this.add(new SliderSetting("YSpeed", 100.0, 0.0, 100.0, 0.1, v -> this.mode.getValue() == Mode.Custom).setSuffix("%"));
    public final BooleanSetting onlySneak = this.add(new BooleanSetting("OnlySneak", true));
    public final BooleanSetting groundcheck = this.add(new BooleanSetting("GroundCheck", true));
    private final SliderSetting fastSpeed = this.add(new SliderSetting("Speed", 3.0, 0.0, 8.0, v -> this.mode.getValue() == Mode.Vanilla || this.mode.getValue() == Mode.Strict));
    private boolean work = false;

    public FastWeb_dehcwwTxEbDSnkFtZvNl() {
        super("FastWeb", "So you don't need to keep timer on keybind", Category.Movement);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    public boolean isWorking() {
        return this.work;
    }

    @Override
    public void onUpdate() {
        if (this.groundcheck.getValue() && FastWeb_dehcwwTxEbDSnkFtZvNl.mc.player.isOnGround()) {
            return;
        }
        boolean bl = this.work = !FastWeb_dehcwwTxEbDSnkFtZvNl.mc.player.isOnGround() && (FastWeb_dehcwwTxEbDSnkFtZvNl.mc.options.sneakKey.isPressed() || !this.onlySneak.getValue()) && HoleKickTest.isInWeb(FastWeb_dehcwwTxEbDSnkFtZvNl.mc.player);
        if (this.work && this.mode.getValue() == Mode.Vanilla) {
            MovementUtil.setMotionY(MovementUtil.getMotionY() - this.fastSpeed.getValue());
        }
    }

    @EventHandler(priority = -100)
    public void onTimer(TimerEvent event) {
        if (this.work && this.mode.getValue() == Mode.Strict) {
            event.set(this.fastSpeed.getValueFloat());
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Mode {
        Vanilla,
        Strict,
        Custom,
        Ignore

    }
}
