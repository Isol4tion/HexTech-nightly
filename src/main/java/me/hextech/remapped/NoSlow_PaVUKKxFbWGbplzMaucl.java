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
        if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.isUsingItem() && !NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.isRiding() && !NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_6128()) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getInventory().selectedSlot));
                    break;
                }
                case 2: {
                    if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_6058() == Hand.OFF_HAND) {
                        mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getInventory().selectedSlot % 8 + 1));
                        mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getInventory().selectedSlot % 7 + 2));
                        mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getInventory().selectedSlot));
                        break;
                    }
                    NoSlow_PaVUKKxFbWGbplzMaucl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, id));
                }
            }
        }
        if (this.active.getValue() && !(NoSlow_PaVUKKxFbWGbplzMaucl.mc.currentScreen instanceof ChatScreen)) {
            for (KeyBinding k : new KeyBinding[]{NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.backKey, NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.leftKey, NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.rightKey}) {
                k.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)k.getBoundKeyTranslationKey()).getCode()));
            }
            InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.forwardKey.getBoundKeyTranslationKey()).getCode());
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sprintKey.setPressed(Sprint.INSTANCE.isOn() || InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sprintKey.getBoundKeyTranslationKey()).getCode()));
            if (this.sneak.getValue()) {
                NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sneakKey.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
            }
        }
    }

    @EventHandler
    public void keyboard(KeyboardInputEvent event) {
        if (this.active.getValue() && !(NoSlow_PaVUKKxFbWGbplzMaucl.mc.currentScreen instanceof ChatScreen)) {
            for (KeyBinding k : new KeyBinding[]{NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.backKey, NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.leftKey, NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.rightKey}) {
                k.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)k.getBoundKeyTranslationKey()).getCode()));
            }
            InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.forwardKey.getBoundKeyTranslationKey()).getCode());
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sprintKey.setPressed(Sprint.INSTANCE.isOn() || InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sprintKey.getBoundKeyTranslationKey()).getCode()));
            if (this.sneak.getValue()) {
                NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sneakKey.setPressed(InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)InputUtil.fromTranslationKey((String)NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
            }
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3910 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.forwardKey.isPressed();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3909 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.backKey.isPressed();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3908 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.leftKey.isPressed();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3906 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.rightKey.isPressed();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.movementForward = NoSlow_PaVUKKxFbWGbplzMaucl.getMovementMultiplier(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3910, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3909);
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.movementSideways = NoSlow_PaVUKKxFbWGbplzMaucl.getMovementMultiplier(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3908, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3906);
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3904 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.jumpKey.isPressed();
            NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3903 = NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.sneakKey.isPressed();
        }
    }

    @EventHandler
    public void onPacketSend(PacketEvent e) {
        if (NoSlow_PaVUKKxFbWGbplzMaucl.nullCheck() || !MovementUtil.isMoving() || !NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.jumpKey.isPressed() || this.pause.get()) {
            return;
        }
        Object t = e.getPacket();
        if (t instanceof ClickSlotC2SPacket) {
            ClickSlotC2SPacket click = (ClickSlotC2SPacket)t;
            switch (this.clickBypass.getValue().ordinal()) {
                case 2: {
                    if (click.getActionType() == SlotActionType.PICKUP || click.getActionType() == SlotActionType.PICKUP_ALL) break;
                    int syncId = NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.field_7512.syncId;
                    int closePacketCount = 2 + (int)(Math.random() * 2.0);
                    for (int i = 0; i < closePacketCount; ++i) {
                        mc.getNetworkHandler().sendPacket((Packet)new CloseHandledScreenC2SPacket(syncId));
                    }
                    int randomSlot = (int)(Math.random() * 9.0);
                    mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(randomSlot));
                    mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getInventory().selectedSlot));
                    NoSlow_PaVUKKxFbWGbplzMaucl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                    NoSlow_PaVUKKxFbWGbplzMaucl.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, id));
                    if (!NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.isOnGround()) break;
                    double offset = 2.71875E-7 + Math.random() * 1.0E-7;
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getX(), NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getY() + offset, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getZ(), false));
                    break;
                }
                case 1: {
                    if (!NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.isOnGround() || NoSlow_PaVUKKxFbWGbplzMaucl.mc.world.method_20812((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getBoundingBox().offset(0.0, 0.0656, 0.0)).iterator().hasNext()) break;
                    if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5624()) {
                        mc.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    }
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getX(), NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getY() + 0.0656, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getZ(), false));
                    break;
                }
                case 5: {
                    if (!NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.isOnGround() || NoSlow_PaVUKKxFbWGbplzMaucl.mc.world.method_20812((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getBoundingBox().offset(0.0, 2.71875E-7, 0.0)).iterator().hasNext()) break;
                    if (NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5624()) {
                        mc.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    }
                    mc.getNetworkHandler().sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getX(), NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getY() + 2.71875E-7, NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.getZ(), false));
                    break;
                }
                case 3: {
                    mc.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    NoSlow_PaVUKKxFbWGbplzMaucl.mc.options.forwardKey.setPressed(false);
                    NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.movementForward = 0.0f;
                    NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.input.field_3910 = false;
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
                mc.getNetworkHandler().sendPacket((Packet)this.storedClicks.poll());
            }
            this.pause.set(false);
        }
    }

    @EventHandler
    public void onPacketSendPost(PacketEvent_nYAfaBsVQmKvWkMiCKYv e) {
        if (e.getPacket() instanceof ClickSlotC2SPacket && NoSlow_PaVUKKxFbWGbplzMaucl.mc.player.method_5624() && this.clickBypass.is(NoSlow.StrictNCP)) {
            mc.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)NoSlow_PaVUKKxFbWGbplzMaucl.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }

    public boolean noSlow() {
        return this.isOn() && this.mode.getValue() != NoSlow_dcwUNFGCrJDcinmdQVVo.None;
    }

    public boolean soulSand() {
        return this.isOn() && this.soulSand.getValue();
    }
}
