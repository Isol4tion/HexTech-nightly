package me.hextech.remapped;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.KeyboardInputEvent;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.NoSlow;
import me.hextech.remapped.NoSlow_dcwUNFGCrJDcinmdQVVo;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.PacketEvent_nYAfaBsVQmKvWkMiCKYv;
import me.hextech.remapped.Sprint;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class NoSlow_PaVUKKxFbWGbplzMaucl
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static NoSlow_PaVUKKxFbWGbplzMaucl INSTANCE;
    private final EnumSetting<NoSlow_dcwUNFGCrJDcinmdQVVo> mode = this.add(new EnumSetting<NoSlow_dcwUNFGCrJDcinmdQVVo>("Mode", NoSlow_dcwUNFGCrJDcinmdQVVo.Vanilla));
    private final BooleanSetting soulSand = this.add(new BooleanSetting("SoulSand", true));
    private final BooleanSetting active = this.add(new BooleanSetting("Gui", true));
    private final EnumSetting<NoSlow> clickBypass = this.add(new EnumSetting<NoSlow>("Bypass", NoSlow.None));
    private final BooleanSetting sneak = this.add(new BooleanSetting("Sneak", false));
    private final Queue<ClickSlotC2SPacket> storedClicks = new LinkedList<ClickSlotC2SPacket>();
    private final AtomicBoolean pause = new AtomicBoolean();

    public NoSlow_PaVUKKxFbWGbplzMaucl() {
        super("NoSlow", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    private static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventHandler
    public void onUpdate(UpdateWalkingEvent event) {
        if (event.isPost()) {
            return;
        }
        if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_6115() && !NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_3144() && !NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_6128()) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_31548().field_7545));
                    break;
                }
                case 2: {
                    if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_6058() == Hand.field_5810) {
                        mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_31548().field_7545 % 8 + 1));
                        mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_31548().field_7545 % 7 + 2));
                        mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_31548().field_7545));
                        break;
                    }
                    NoSlow_PaVUKKxFbWGbplzMaucl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5810, id));
                }
            }
        }
        if (this.active.getValue() && !(NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1755 instanceof ChatScreen)) {
            for (KeyBinding k : new KeyBinding[]{NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1881, NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1913, NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1849}) {
                k.method_23481(InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)k.method_1428()).method_1444()));
            }
            InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1894.method_1428()).method_1444());
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() || InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1867.method_1428()).method_1444()));
            if (this.sneak.getValue()) {
                NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1832.method_23481(InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1832.method_1428()).method_1444()));
            }
        }
    }

    @EventHandler
    public void keyboard(KeyboardInputEvent event) {
        if (this.active.getValue() && !(NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1755 instanceof ChatScreen)) {
            for (KeyBinding k : new KeyBinding[]{NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1881, NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1913, NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1849}) {
                k.method_23481(InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)k.method_1428()).method_1444()));
            }
            InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1894.method_1428()).method_1444());
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() || InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1867.method_1428()).method_1444()));
            if (this.sneak.getValue()) {
                NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1832.method_23481(InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1832.method_1428()).method_1444()));
            }
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3910 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1894.method_1434();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3909 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1881.method_1434();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3908 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1913.method_1434();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3906 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1849.method_1434();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3905 = NoSlow_PaVUKKxFbWGbplzMaucl.getMovementMultiplier(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3910, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3909);
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3907 = NoSlow_PaVUKKxFbWGbplzMaucl.getMovementMultiplier(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3908, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3906);
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3904 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1903.method_1434();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3903 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1832.method_1434();
        }
    }

    @EventHandler
    public void onPacketSend(PacketEvent e) {
        if (NoSlow_PaVUKKxFbWGbplzMaucl.nullCheck() || !MovementUtil.isMoving() || !NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1903.method_1434() || this.pause.get()) {
            return;
        }
        Object t = e.getPacket();
        if (t instanceof ClickSlotC2SPacket) {
            ClickSlotC2SPacket click = (ClickSlotC2SPacket)t;
            switch (this.clickBypass.getValue().ordinal()) {
                case 2: {
                    if (click.method_12195() == SlotActionType.field_7790 || click.method_12195() == SlotActionType.field_7793) break;
                    int syncId = NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_7512.field_7763;
                    int closePacketCount = 2 + (int)(Math.random() * 2.0);
                    for (int i = 0; i < closePacketCount; ++i) {
                        mc.method_1562().method_52787((Packet)new CloseHandledScreenC2SPacket(syncId));
                    }
                    int randomSlot = (int)(Math.random() * 9.0);
                    mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(randomSlot));
                    mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_31548().field_7545));
                    NoSlow_PaVUKKxFbWGbplzMaucl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5808, id));
                    NoSlow_PaVUKKxFbWGbplzMaucl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.field_5810, id));
                    if (!NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_24828()) break;
                    double offset = 2.71875E-7 + Math.random() * 1.0E-7;
                    mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getX(), NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getY() + offset, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getZ(), false));
                    break;
                }
                case 1: {
                    if (!NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_24828() || NoSlow_PaVUKKxFbWGbplzMaucl.mc.world.method_20812((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5829().method_989(0.0, 0.0656, 0.0)).iterator().hasNext()) break;
                    if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5624()) {
                        mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.field_12985));
                    }
                    mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getX(), NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getY() + 0.0656, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getZ(), false));
                    break;
                }
                case 5: {
                    if (!NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_24828() || NoSlow_PaVUKKxFbWGbplzMaucl.mc.world.method_20812((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5829().method_989(0.0, 2.71875E-7, 0.0)).iterator().hasNext()) break;
                    if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5624()) {
                        mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.field_12985));
                    }
                    mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getX(), NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getY() + 2.71875E-7, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getZ(), false));
                    break;
                }
                case 3: {
                    mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.field_12985));
                    NoSlow_PaVUKKxFbWGbplzMaucl.mc.field_1690.field_1894.method_23481(false);
                    NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3905 = 0.0f;
                    NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_3913.field_3910 = false;
                    break;
                }
                case 4: {
                    this.storedClicks.add(click);
                    e.cancel();
                }
            }
        }
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket && this.clickBypass.is(NoSlow.Delay)) {
            this.pause.set(true);
            while (!this.storedClicks.isEmpty()) {
                mc.method_1562().method_52787((Packet)this.storedClicks.poll());
            }
            this.pause.set(false);
        }
    }

    @EventHandler
    public void onPacketSendPost(PacketEvent_nYAfaBsVQmKvWkMiCKYv e) {
        if (e.getPacket() instanceof ClickSlotC2SPacket && NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5624() && this.clickBypass.is(NoSlow.StrictNCP)) {
            mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.field_12981));
        }
    }

    public boolean noSlow() {
        return this.isOn() && this.mode.getValue() != NoSlow_dcwUNFGCrJDcinmdQVVo.None;
    }

    public boolean soulSand() {
        return this.isOn() && this.soulSand.getValue();
    }
}
