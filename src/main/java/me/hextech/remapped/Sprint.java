package me.hextech.remapped;

import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;

public class Sprint
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Sprint INSTANCE;
    public static boolean shouldSprint;
    public final EnumSetting<_kIBjeDSbfTeuMDPgEQgD> mode = this.add(new EnumSetting<_kIBjeDSbfTeuMDPgEQgD>("Mode", _kIBjeDSbfTeuMDPgEQgD.Normal));

    public Sprint() {
        super("Sprint", Module_JlagirAibYQgkHtbRnhw.Movement);
        this.setDescription("Permanently keeps player in sprinting mode.");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (Sprint.nullCheck()) {
            return;
        }
        switch (this.mode.getValue().ordinal()) {
            case 0: {
                Sprint.mc.field_1690.field_1867.method_23481(true);
                shouldSprint = false;
                break;
            }
            case 1: {
                Sprint.mc.field_1690.field_1867.method_23481(true);
                shouldSprint = false;
                if (Sprint.mc.field_1724.method_7344().method_7586() <= 6 && !Sprint.mc.field_1724.method_7337()) {
                    return;
                }
                Sprint.mc.field_1724.method_5728(MovementUtil.isMoving() && !Sprint.mc.field_1724.method_5715());
                break;
            }
            case 2: {
                shouldSprint = (Sprint.mc.field_1724.method_7344().method_7586() > 6 || Sprint.mc.field_1724.method_7337()) && !Sprint.mc.field_1724.method_5715();
                Sprint.mc.field_1724.method_5728(shouldSprint);
            }
        }
    }

    public static final class _kIBjeDSbfTeuMDPgEQgD
    extends Enum<_kIBjeDSbfTeuMDPgEQgD> {
        public static final /* enum */ _kIBjeDSbfTeuMDPgEQgD Legit;
        public static final /* enum */ _kIBjeDSbfTeuMDPgEQgD Normal;
        public static final /* enum */ _kIBjeDSbfTeuMDPgEQgD Rage;

        public static _kIBjeDSbfTeuMDPgEQgD[] values() {
            return null;
        }

        public static _kIBjeDSbfTeuMDPgEQgD valueOf(String string) {
            return null;
        }
    }
}
