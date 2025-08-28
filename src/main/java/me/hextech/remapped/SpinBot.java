package me.hextech.remapped;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.item.BowItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;

public class SpinBot
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final SliderSetting yawDelta = this.add(new SliderSetting("YawDelta", 60, -360, 360));
    public final SliderSetting pitchDelta = this.add(new SliderSetting("PitchDelta", 10, -90, 90));
    public final BooleanSetting allowInteract = this.add(new BooleanSetting("AllowInteract", true));
    private final EnumSetting<_YiToqkCkUTOMQxneHmRR> pitchMode = this.add(new EnumSetting<_YiToqkCkUTOMQxneHmRR>("PitchMode", _YiToqkCkUTOMQxneHmRR.None));
    private final EnumSetting<_YiToqkCkUTOMQxneHmRR> yawMode = this.add(new EnumSetting<_YiToqkCkUTOMQxneHmRR>("YawMode", _YiToqkCkUTOMQxneHmRR.None));
    private float rotationYaw;
    private float rotationPitch;

    public SpinBot() {
        super("SpinBot", "fun", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        PlayerActionC2SPacket packet;
        Object t = event.getPacket();
        if (t instanceof PlayerActionC2SPacket && (packet = (PlayerActionC2SPacket)t).getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM && SpinBot.mc.player.getActiveItem().getItem() instanceof BowItem) {
            EntityUtil.sendYawAndPitch(SpinBot.mc.player.getYaw(), SpinBot.mc.player.getPitch());
        }
    }

    @EventHandler(priority=200)
    public void onUpdateWalkingPlayerPre(RotateEvent event) {
        if (this.pitchMode.getValue() == _YiToqkCkUTOMQxneHmRR.RandomAngle) {
            this.rotationPitch = MathUtil.random(90.0f, -90.0f);
        }
        if (this.yawMode.getValue() == _YiToqkCkUTOMQxneHmRR.RandomAngle) {
            this.rotationYaw = MathUtil.random(0.0f, 360.0f);
        }
        if (this.yawMode.getValue() == _YiToqkCkUTOMQxneHmRR.Spin) {
            this.rotationYaw = (float)((double)this.rotationYaw + this.yawDelta.getValue());
        }
        if (this.rotationYaw > 360.0f) {
            this.rotationYaw = 0.0f;
        }
        if (this.rotationYaw < 0.0f) {
            this.rotationYaw = 360.0f;
        }
        if (this.pitchMode.getValue() == _YiToqkCkUTOMQxneHmRR.Spin) {
            this.rotationPitch = (float)((double)this.rotationPitch + this.pitchDelta.getValue());
        }
        if (this.rotationPitch > 90.0f) {
            this.rotationPitch = -90.0f;
        }
        if (this.rotationPitch < -90.0f) {
            this.rotationPitch = 90.0f;
        }
        if (this.pitchMode.getValue() == _YiToqkCkUTOMQxneHmRR.Static) {
            this.rotationPitch = SpinBot.mc.player.getPitch() + this.pitchDelta.getValueFloat();
            this.rotationPitch = MathUtil.clamp(this.rotationPitch, -90.0f, 90.0f);
        }
        if (this.yawMode.getValue() == _YiToqkCkUTOMQxneHmRR.Static) {
            this.rotationYaw = SpinBot.mc.player.getYaw() % 360.0f + this.yawDelta.getValueFloat();
        }
        if (this.allowInteract.getValue() && (SpinBot.mc.options.useKey.isPressed() && !EntityUtil.isUsing() || SpinBot.mc.options.attackKey.isPressed())) {
            return;
        }
        if (this.yawMode.getValue() != _YiToqkCkUTOMQxneHmRR.None) {
            event.setYaw(this.rotationYaw);
        }
        if (this.pitchMode.getValue() != _YiToqkCkUTOMQxneHmRR.None) {
            event.setPitch(this.rotationPitch);
        }
    }

    public enum _YiToqkCkUTOMQxneHmRR {
        None,
        RandomAngle,
        Spin,
        Static

    }
}
