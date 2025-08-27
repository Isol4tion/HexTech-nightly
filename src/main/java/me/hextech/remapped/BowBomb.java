package me.hextech.remapped;

import java.util.Random;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class BowBomb
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final Timer delayTimer = new Timer();
    private final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", false));
    private final SliderSetting spoofs = this.add(new SliderSetting("Spoofs", 50.0, 0.0, 200.0, 1.0));
    private final EnumSetting<_rjdFZKIVfyUHfSoAeHLl> exploit = this.add(new EnumSetting<_rjdFZKIVfyUHfSoAeHLl>("Exploit", _rjdFZKIVfyUHfSoAeHLl.Strong));
    private final BooleanSetting minimize = this.add(new BooleanSetting("Minimize", false));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 10.0).setSuffix("s"));
    private final SliderSetting activeTime = this.add(new SliderSetting("ActiveTime", 0.4f, 0.0, 3.0).setSuffix("s"));
    private final Random random = new Random();
    private final Timer activeTimer = new Timer();

    public BowBomb() {
        super("BowBomb", "exploit", Module_JlagirAibYQgkHtbRnhw.Combat);
    }

    @Override
    public void onUpdate() {
        if (!BowBomb.mc.player.method_6115() || BowBomb.mc.player.method_6030().getItem() != Items.BOW) {
            this.activeTimer.reset();
        }
    }

    @EventHandler
    protected void onPacketSend(PacketEvent event) {
        PlayerActionC2SPacket packet;
        if (BowBomb.nullCheck() || !this.delayTimer.passedMs((long)(this.delay.getValue() * 1000.0)) || !this.activeTimer.passedMs((long)(this.activeTime.getValue() * 1000.0))) {
            return;
        }
        Object t = event.getPacket();
        if (t instanceof PlayerActionC2SPacket && (packet = (PlayerActionC2SPacket)t).getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM) {
            BowBomb.mc.player.networkHandler.method_52787((Packet)new ClientCommandC2SPacket((Entity)BowBomb.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            if (this.exploit.getValue() == _rjdFZKIVfyUHfSoAeHLl.Fast) {
                for (int i = 0; i < this.getRuns(); ++i) {
                    this.spoof(BowBomb.mc.player.method_23317(), this.minimize.getValue() ? BowBomb.mc.player.method_23318() : BowBomb.mc.player.method_23318() - 1.0E-10, BowBomb.mc.player.method_23321(), true);
                    this.spoof(BowBomb.mc.player.method_23317(), BowBomb.mc.player.method_23318() + 1.0E-10, BowBomb.mc.player.method_23321(), false);
                }
            }
            if (this.exploit.getValue() == _rjdFZKIVfyUHfSoAeHLl.Strong) {
                for (int i = 0; i < this.getRuns(); ++i) {
                    this.spoof(BowBomb.mc.player.method_23317(), BowBomb.mc.player.method_23318() + 1.0E-10, BowBomb.mc.player.method_23321(), false);
                    this.spoof(BowBomb.mc.player.method_23317(), this.minimize.getValue() ? BowBomb.mc.player.method_23318() : BowBomb.mc.player.method_23318() - 1.0E-10, BowBomb.mc.player.method_23321(), true);
                }
            }
            if (this.exploit.getValue() == _rjdFZKIVfyUHfSoAeHLl.Phobos) {
                for (int i = 0; i < this.getRuns(); ++i) {
                    this.spoof(BowBomb.mc.player.method_23317(), BowBomb.mc.player.method_23318() + 1.3E-13, BowBomb.mc.player.method_23321(), true);
                    this.spoof(BowBomb.mc.player.method_23317(), BowBomb.mc.player.method_23318() + 2.7E-13, BowBomb.mc.player.method_23321(), false);
                }
            }
            if (this.exploit.getValue() == _rjdFZKIVfyUHfSoAeHLl.Strict) {
                double[] strict_direction = new double[]{100.0 * -Math.sin(Math.toRadians(BowBomb.mc.player.method_36454())), 100.0 * Math.cos(Math.toRadians(BowBomb.mc.player.method_36454()))};
                for (int i = 0; i < this.getRuns(); ++i) {
                    if (this.random.nextBoolean()) {
                        this.spoof(BowBomb.mc.player.method_23317() - strict_direction[0], BowBomb.mc.player.method_23318(), BowBomb.mc.player.method_23321() - strict_direction[1], false);
                        continue;
                    }
                    this.spoof(BowBomb.mc.player.method_23317() + strict_direction[0], BowBomb.mc.player.method_23318(), BowBomb.mc.player.method_23321() + strict_direction[1], true);
                }
            }
            this.delayTimer.reset();
        }
    }

    private void spoof(double x, double y, double z, boolean ground) {
        if (this.rotation.getValue()) {
            BowBomb.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.Full(x, y, z, BowBomb.mc.player.method_36454(), BowBomb.mc.player.method_36455(), ground));
        } else {
            BowBomb.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, ground));
        }
    }

    private int getRuns() {
        return this.spoofs.getValueInt();
    }

    private static enum _rjdFZKIVfyUHfSoAeHLl {
        Strong,
        Fast,
        Strict,
        Phobos;

    }
}
