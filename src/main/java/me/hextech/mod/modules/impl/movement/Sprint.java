package me.hextech.mod.modules.impl.movement;

import me.hextech.api.utils.entity.MovementUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.EnumSetting;

public class Sprint
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Sprint INSTANCE;
    public static boolean shouldSprint;
    public final EnumSetting<_kIBjeDSbfTeuMDPgEQgD> mode = this.add(new EnumSetting<_kIBjeDSbfTeuMDPgEQgD>("Mode", _kIBjeDSbfTeuMDPgEQgD.Normal));

    public Sprint() {
        super("Sprint", Category.Movement);
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
                Sprint.mc.options.sprintKey.setPressed(true);
                shouldSprint = false;
                break;
            }
            case 1: {
                Sprint.mc.options.sprintKey.setPressed(true);
                shouldSprint = false;
                if (Sprint.mc.player.getHungerManager().getFoodLevel() <= 6 && !Sprint.mc.player.isCreative()) {
                    return;
                }
                Sprint.mc.player.setSprinting(MovementUtil.isMoving() && !Sprint.mc.player.isSneaking());
                break;
            }
            case 2: {
                shouldSprint = (Sprint.mc.player.getHungerManager().getFoodLevel() > 6 || Sprint.mc.player.isCreative()) && !Sprint.mc.player.isSneaking();
                Sprint.mc.player.setSprinting(shouldSprint);
            }
        }
    }

    public enum _kIBjeDSbfTeuMDPgEQgD {
        Legit,
        Normal,
        Rage

    }
}
