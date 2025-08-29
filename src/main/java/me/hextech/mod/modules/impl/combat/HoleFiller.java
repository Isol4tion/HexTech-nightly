package me.hextech.mod.modules.impl.combat;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.RotateEvent;
import me.hextech.api.managers.RotateManager;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.mod.modules.impl.setting.PredictionSetting;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class HoleFiller
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HoleFiller INSTANCE;
    public final EnumSetting<_sZEAolNLTvldDiHjSnlT> page = this.add(new EnumSetting<_sZEAolNLTvldDiHjSnlT>("Page", _sZEAolNLTvldDiHjSnlT.General));
    public final SliderSetting delay = this.add(new SliderSetting("Delay", 50, 0, 500, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    public final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 2, 1, 10, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    public final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    public final BooleanSetting any = this.add(new BooleanSetting("AnyHole", true, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    public final BooleanSetting doubleHole = this.add(new BooleanSetting("DoubleHole", true, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final BooleanSetting webs = this.add(new BooleanSetting("Webs", false, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final SliderSetting range = this.add(new SliderSetting("Radius", 1.9, 0.0, 6.0, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final SliderSetting saferange = this.add(new SliderSetting("SafeRange", 1.4, 0.0, 6.0, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 12.0, 0.0, 20.0, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 4, 0, 10, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true, v -> this.page.getValue() == _sZEAolNLTvldDiHjSnlT.Rotate).setParent());
    private final BooleanSetting newRotate = this.add(new BooleanSetting("NewRotate", false, v -> this.rotate.isOpen() && this.page.getValue() == _sZEAolNLTvldDiHjSnlT.Rotate));
    private final SliderSetting yawStep = this.add(new SliderSetting("YawStep", 0.3f, 0.1f, 1.0, 0.01f, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _sZEAolNLTvldDiHjSnlT.Rotate));
    private final BooleanSetting checkLook = this.add(new BooleanSetting("CheckLook", true, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.page.getValue() == _sZEAolNLTvldDiHjSnlT.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 5.0, 0.0, 30.0, v -> this.rotate.isOpen() && this.newRotate.getValue() && this.checkLook.getValue() && this.page.getValue() == _sZEAolNLTvldDiHjSnlT.Rotate));
    private final Timer timer = new Timer();
    public Vec3d directionVec = null;
    int progress = 0;
    private PlayerEntity closestTarget;
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;

    public HoleFiller() {
        super("HoleFiller", "Fills all safe spots in radius", Category.Combat);
        INSTANCE = this;
    }

    public static boolean needSneak(Block in) {
        return BlockUtil.shiftBlocks.contains(in);
    }

    @Override
    public void onDisable() {
        this.closestTarget = null;
    }

    @Override
    public String getInfo() {
        return "";
    }

    @EventHandler(priority = 98)
    public void onRotate(RotateEvent event) {
        if (this.newRotate.getValue() && this.directionVec != null) {
            float[] newAngle = this.injectStep(EntityUtil.getLegitRotations(this.directionVec), this.yawStep.getValueFloat());
            this.lastYaw = newAngle[0];
            this.lastPitch = newAngle[1];
            event.setYaw(this.lastYaw);
            event.setPitch(this.lastPitch);
        } else {
            this.lastYaw = HexTech.ROTATE.lastYaw;
            this.lastPitch = RotateManager.lastPitch;
        }
    }

    @Override
    public void onUpdate() {
        int block = -1;
        if (HoleFiller.nullCheck()) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValueInt())) {
            return;
        }
        if (this.usingPause.getValue() && HoleFiller.mc.player.isUsingItem()) {
            return;
        }
        this.progress = 0;
        this.directionVec = null;
        this.timer.reset();
        int obbySlot = this.findBlock(Blocks.OBSIDIAN);
        int eChestSlot = this.findBlock(Blocks.ENDER_CHEST);
        int webSlot = this.findBlock(Blocks.COBWEB);
        int n = this.webs.getValue() ? (webSlot == -1 ? (obbySlot == -1 ? eChestSlot : obbySlot) : webSlot) : (block = obbySlot == -1 ? eChestSlot : obbySlot);
        if (!this.webs.getValue() && obbySlot == -1 && eChestSlot == -1) {
            return;
        }
        if (this.webs.getValue() && webSlot == -1 && obbySlot == -1 && eChestSlot == -1) {
            return;
        }
        ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS> list = new ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS>();
        for (PlayerEntity target : CombatUtil.getEnemies(this.targetRange.getRange())) {
            list.add(new PredictionSetting._XBpBEveLWEKUGQPHCCIS(target));
        }
        PredictionSetting._XBpBEveLWEKUGQPHCCIS self = new PredictionSetting._XBpBEveLWEKUGQPHCCIS(HoleFiller.mc.player);
        if (!list.isEmpty()) {
            for (PredictionSetting._XBpBEveLWEKUGQPHCCIS pap : list) {
                for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat(), pap.player.getPos())) {
                    if (!BlockUtil.isHole(pos, true, true, this.any.getValue()) && (!this.doubleHole.getValue() || !CombatUtil.isDoubleHole(pos)) || HoleFiller.mc.player.squaredDistanceTo(pos.toCenterPos()) < this.saferange.getValue() || this.detectMining.getValue() && (HexTech.BREAK.isMining(pos) || pos.equals(SpeedMine.breakPos)) || this.progress >= this.blocksPer.getValueInt() || !BlockUtil.canPlace(pos, this.placeRange.getValue()) || BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) == null || !HoleFiller.mc.world.isAir(pos))
                        continue;
                    int oldSlot = HoleFiller.mc.player.getInventory().selectedSlot;
                    this.doSwap(block);
                    this.placeBlock(pos, this.rotate.getValue());
                    ++this.progress;
                    if (this.inventory.getValue()) {
                        this.doSwap(block);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(oldSlot);
                    }
                    this.timer.reset();
                }
            }
        }
    }

    public boolean placeBlock(BlockPos pos, boolean rotate) {
        Direction side;
        if (BlockUtil.airPlace()) {
            for (Direction i : Direction.values()) {
                if (!HoleFiller.mc.world.isAir(pos.offset(i))) continue;
                return this.clickBlock(pos, i, rotate);
            }
        }
        if ((side = BlockUtil.getPlaceSide(pos)) == null) {
            return false;
        }
        Vec3d directionVec = new Vec3d((double) pos.getX() + 0.5 + (double) side.getVector().getX() * 0.5, (double) pos.getY() + 0.5 + (double) side.getVector().getY() * 0.5, (double) pos.getZ() + 0.5 + (double) side.getVector().getZ() * 0.5);
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        BlockUtil.placedPos.add(pos);
        boolean sprint = false;
        if (HoleFiller.mc.player != null) {
            sprint = HoleFiller.mc.player.isSprinting();
        }
        boolean sneak = false;
        if (HoleFiller.mc.world != null) {
            boolean bl = sneak = HoleFiller.needSneak(HoleFiller.mc.world.getBlockState(result.getBlockPos()).getBlock()) && !HoleFiller.mc.player.isSneaking();
        }
        if (sprint) {
            HoleFiller.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(HoleFiller.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
        if (sneak) {
            HoleFiller.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(HoleFiller.mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        }
        this.clickBlock(pos.offset(side), side.getOpposite(), rotate);
        if (sneak) {
            HoleFiller.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(HoleFiller.mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        }
        if (sprint) {
            HoleFiller.mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(HoleFiller.mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
        EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        return true;
    }

    public boolean clickBlock(BlockPos pos, Direction side, boolean rotate) {
        Vec3d directionVec = new Vec3d((double) pos.getX() + 0.5 + (double) side.getVector().getX() * 0.5, (double) pos.getY() + 0.5 + (double) side.getVector().getY() * 0.5, (double) pos.getZ() + 0.5 + (double) side.getVector().getZ() * 0.5);
        if (rotate && !this.faceVector(directionVec)) {
            return false;
        }
        EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        HoleFiller.mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, BlockUtil.getWorldActionId(HoleFiller.mc.world)));
        return true;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.newRotate.getValue()) {
            EntityUtil.faceVector(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        float[] angle = EntityUtil.getLegitRotations(directionVec);
        if (Math.abs(MathHelper.wrapDegrees(angle[0] - this.lastYaw)) < this.fov.getValueFloat() && Math.abs(MathHelper.wrapDegrees(angle[1] - this.lastPitch)) < this.fov.getValueFloat()) {
            EntityUtil.sendYawAndPitch(angle[0], angle[1]);
            return true;
        }
        return !this.checkLook.getValue();
    }

    private float[] injectStep(float[] angle, float steps) {
        if (steps < 0.01f) {
            steps = 0.01f;
        }
        if (steps > 1.0f) {
            steps = 1.0f;
        }
        if (steps < 1.0f && angle != null) {
            float packetPitch;
            float packetYaw = this.lastYaw;
            float diff = MathHelper.wrapDegrees(angle[0] - packetYaw);
            if (Math.abs(diff) > 90.0f * steps) {
                angle[0] = packetYaw + diff * (90.0f * steps / Math.abs(diff));
            }
            if (Math.abs(diff = angle[1] - (packetPitch = this.lastPitch)) > 90.0f * steps) {
                angle[1] = packetPitch + diff * (90.0f * steps / Math.abs(diff));
            }
        }
        return new float[]{angle[0], angle[1]};
    }

    public int findBlock(Block blockIn) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(blockIn);
        }
        return InventoryUtil.findBlock(blockIn);
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, HoleFiller.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public enum _sZEAolNLTvldDiHjSnlT {
        General,
        Rotate

    }
}
