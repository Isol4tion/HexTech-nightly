package me.hextech.mod.modules.impl.movement;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.UpdateWalkingEvent;
import me.hextech.api.utils.entity.MovementUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.HoleKickTest;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class Step_EShajbhvQeYkCdreEeNY
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Step_EShajbhvQeYkCdreEeNY INSTANCE;
    public final SliderSetting height = this.add(new SliderSetting("Height", 1.0, 0.0, 5.0, 0.5));
    public final BooleanSetting onlyMoving = this.add(new BooleanSetting("OnlyMoving", true));
    public final BooleanSetting inWebPause = this.add(new BooleanSetting("InWebPause", true));
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Vanilla));
    boolean timer;
    int packets = 0;

    public Step_EShajbhvQeYkCdreEeNY() {
        super("Step", "Steps up blocks.", Category.Movement);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        if (Step_EShajbhvQeYkCdreEeNY.nullCheck()) {
            return;
        }
        Step_EShajbhvQeYkCdreEeNY.mc.player.setStepHeight(0.6f);
        HexTech.TIMER.reset();
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (this.inWebPause.getValue() && HoleKickTest.isInWeb(Step_EShajbhvQeYkCdreEeNY.mc.player) || !Step_EShajbhvQeYkCdreEeNY.mc.player.isOnGround() || this.onlyMoving.getValue() && !MovementUtil.isMoving()) {
            Step_EShajbhvQeYkCdreEeNY.mc.player.setStepHeight(0.6f);
            return;
        }
        Step_EShajbhvQeYkCdreEeNY.mc.player.setStepHeight(this.height.getValueFloat());
    }

    @EventHandler
    public void onStep(UpdateWalkingEvent event) {
        if (event.isPost()) {
            --this.packets;
            return;
        }
        if (this.timer && this.packets <= 0) {
            HexTech.TIMER.reset();
            this.timer = false;
        }
        boolean strict = this.mode.getValue() == Mode.NCP;
    }

    public double getTimer(double height) {
        if (height > 0.6 && height <= 1.0) {
            return 0.5;
        }
        double[] offsets = this.getOffset(height);
        if (offsets == null) {
            return 1.0;
        }
        return 1.0 / (double) offsets.length;
    }

    public double[] getOffset(double height) {
        boolean strict;
        boolean bl = strict = this.mode.getValue() == Mode.NCP;
        if (height == 0.75) {
            if (strict) {
                return new double[]{0.42, 0.753, 0.75};
            }
            return new double[]{0.42, 0.753};
        }
        if (height == 0.8125) {
            if (strict) {
                return new double[]{0.39, 0.7, 0.8125};
            }
            return new double[]{0.39, 0.7};
        }
        if (height == 0.875) {
            if (strict) {
                return new double[]{0.39, 0.7, 0.875};
            }
            return new double[]{0.39, 0.7};
        }
        if (height == 1.0) {
            if (strict) {
                return new double[]{0.42, 0.753, 1.0};
            }
            return new double[]{0.42, 0.753};
        }
        if (height == 1.5) {
            return new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
        }
        if (height == 2.0) {
            return new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
        }
        if (height == 2.5) {
            return new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
        }
        return null;
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Mode {
        Vanilla,
        NCP

    }
}
