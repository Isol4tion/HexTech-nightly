package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FastWeb;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.TimerEvent;
import net.minecraft.entity.player.PlayerEntity;

public class FastWeb_dehcwwTxEbDSnkFtZvNl
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static FastWeb_dehcwwTxEbDSnkFtZvNl INSTANCE;
    public final EnumSetting<FastWeb> mode = this.add(new EnumSetting<FastWeb>("Mode", FastWeb.Vanilla));
    public final SliderSetting xZSlow = this.add(new SliderSetting("XZSpeed", 25.0, 0.0, 100.0, 0.1, v -> this.mode.getValue() == FastWeb.Custom).setSuffix("%"));
    public final SliderSetting ySlow = this.add(new SliderSetting("YSpeed", 100.0, 0.0, 100.0, 0.1, v -> this.mode.getValue() == FastWeb.Custom).setSuffix("%"));
    public final BooleanSetting onlySneak = this.add(new BooleanSetting("OnlySneak", true));
    public final BooleanSetting groundcheck = this.add(new BooleanSetting("GroundCheck", true));
    private final SliderSetting fastSpeed = this.add(new SliderSetting("Speed", 3.0, 0.0, 8.0, v -> this.mode.getValue() == FastWeb.Vanilla || this.mode.getValue() == FastWeb.Strict));
    private boolean work = false;

    public FastWeb_dehcwwTxEbDSnkFtZvNl() {
        super("FastWeb", "So you don't need to keep timer on keybind", Module_JlagirAibYQgkHtbRnhw.Movement);
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
        if (this.groundcheck.getValue() && FastWeb_dehcwwTxEbDSnkFtZvNl.mc.player.method_24828()) {
            return;
        }
        boolean bl = this.work = !FastWeb_dehcwwTxEbDSnkFtZvNl.mc.player.method_24828() && (FastWeb_dehcwwTxEbDSnkFtZvNl.mc.field_1690.field_1832.method_1434() || !this.onlySneak.getValue()) && HoleKickTest.isInWeb((PlayerEntity)FastWeb_dehcwwTxEbDSnkFtZvNl.mc.player);
        if (this.work && this.mode.getValue() == FastWeb.Vanilla) {
            MovementUtil.setMotionY(MovementUtil.getMotionY() - this.fastSpeed.getValue());
        }
    }

    @EventHandler(priority=-100)
    public void onTimer(TimerEvent event) {
        if (this.work && this.mode.getValue() == FastWeb.Strict) {
            event.set(this.fastSpeed.getValueFloat());
        }
    }
}
