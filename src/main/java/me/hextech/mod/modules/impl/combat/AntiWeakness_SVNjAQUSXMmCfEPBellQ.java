package me.hextech.mod.modules.impl.combat;


import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public class AntiWeakness_SVNjAQUSXMmCfEPBellQ
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final EnumSetting<AntiWeakness> swapMode = this.add(new EnumSetting<AntiWeakness>("SwapMode", AntiWeakness.Inventory));
    private final BooleanSetting onlyCrystal = this.add(new BooleanSetting("OnlyCrystal", true));
    private final Timer delayTimer = new Timer();
    boolean ignore = false;
    private PlayerInteractEntityC2SPacket lastPacket = null;
    public AntiWeakness_SVNjAQUSXMmCfEPBellQ() {
        super("AntiWeakness", "anti weak", Category.Combat);
    }

    @Override
    public String getInfo() {
        return this.swapMode.getValue().name();
    }

    @EventHandler(priority = -200)
    public void onPacketSend(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        PlayerInteractEntityC2SPacket packet;
        if (AntiWeakness_SVNjAQUSXMmCfEPBellQ.nullCheck()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (this.ignore) {
            return;
        }
        if (AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.getStatusEffect(StatusEffects.WEAKNESS) == null) {
            return;
        }
        if (AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.getMainHandStack().getItem() instanceof SwordItem) {
            return;
        }
        if (!this.delayTimer.passedMs(this.delay.getValue())) {
            return;
        }
        Object t = event.getPacket();
        if (t instanceof PlayerInteractEntityC2SPacket && Criticals.getInteractType(packet = (PlayerInteractEntityC2SPacket) t) == Criticals._QenzavIULhSqCVPmsILH.ATTACK) {
            if (this.onlyCrystal.getValue() && !(Criticals.getEntity(packet) instanceof EndCrystalEntity)) {
                return;
            }
            this.lastPacket = event.getPacket();
            this.delayTimer.reset();
            this.ignore = true;
            this.doAnti();
            this.ignore = false;
            event.cancel();
        }
    }

    private void doAnti() {
        if (this.lastPacket == null) {
            return;
        }
        int strong = this.swapMode.getValue() != AntiWeakness.Inventory ? InventoryUtil.findClass(SwordItem.class) : InventoryUtil.findClassInventorySlot(SwordItem.class);
        if (strong == -1) {
            return;
        }
        int old = AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.getInventory().selectedSlot;
        if (this.swapMode.getValue() != AntiWeakness.Inventory) {
            InventoryUtil.switchToSlot(strong);
        } else {
            AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.interactionManager.clickSlot(AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.currentScreenHandler.syncId, strong, AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.getInventory().selectedSlot, SlotActionType.SWAP, AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player);
        }
        AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.networkHandler.sendPacket(this.lastPacket);
        if (this.swapMode.getValue() != AntiWeakness.Inventory) {
            if (this.swapMode.getValue() != AntiWeakness.Normal) {
                InventoryUtil.switchToSlot(old);
            }
        } else {
            AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.interactionManager.clickSlot(AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.currentScreenHandler.syncId, strong, AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player.getInventory().selectedSlot, SlotActionType.SWAP, AntiWeakness_SVNjAQUSXMmCfEPBellQ.mc.player);
            EntityUtil.syncInventory();
        }
    }

    public enum AntiWeakness {
        Normal,
        Silent,
        Inventory
    }
}
