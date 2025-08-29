package me.hextech.mod.modules.impl.combat;

import com.mojang.authlib.GameProfile;
import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.OffTrackEvent;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.managers.RotateManager;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.InventoryUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.api.utils.world.OyveyExplosionUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.player.Blink;
import me.hextech.mod.modules.settings.SwingSide;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Cleaner_iFwqnooxsJEmHoVteFeQ
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Cleaner_iFwqnooxsJEmHoVteFeQ INSTANCE;
    public final Timer lastBreakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer noPosTimer = new Timer();
    private final Timer switchTimer = new Timer();
    private final Timer delayTimer = new Timer();
    private final List<BlockPos> checkPos = new ArrayList<BlockPos>();
    private final float lastYaw = 0.0f;
    private final float lastPitch = 0.0f;
    public Vec3d directionVec = null;
    public BlockPos syncPos;
    public float state = 2.0f;
    EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == Page.Rotation).setParent());
    private final BooleanSetting onBreak = this.add(new BooleanSetting("OnBreak", false, v -> this.rotate.isOpen() && this.page.getValue() == Page.Rotation));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.onBreak.getValue() && this.page.getValue() == Page.Rotation));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, v -> this.rotate.isOpen() && this.page.getValue() == Page.Rotation));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Page.Rotation));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Page.Rotation));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 30, 0, 50, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == Page.Rotation));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Page.Rotation));
    private final SliderSetting noPosTimerSet = this.add(new SliderSetting("NoPosTimer", 50, 0, 1000, v -> this.page.getValue() == Page.Rotation));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting attack = this.add(new BooleanSetting("Attack", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting forceWeb = this.add(new BooleanSetting("ForceWeb", true, v -> this.page.getValue() == Page.General));
    private final BooleanSetting afterBreak = this.add(new BooleanSetting("PlaceAfterBreak", true, v -> this.page.getValue() == Page.General));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 1.0, 10.0, 0.1, v -> this.page.getValue() == Page.General).setSuffix("m"));
    private final SliderSetting wallRange = this.add(new SliderSetting("WallRange", 5.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Page.General));
    private final SliderSetting maxSelf = this.add(new SliderSetting("MaxSelf", 3.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Page.General));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting breakDelay = this.add(new SliderSetting("BreakDelay", 0, 0, 1000, v -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting calcDelay = this.add(new SliderSetting("CalcDelay", 250, 0, 1000, v -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final BooleanSetting checkBurrow = this.add(new BooleanSetting("CheckBurrow", true, v -> this.page.getValue() == Page.Check));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == Page.Check));
    private final BooleanSetting fireFix = this.add(new BooleanSetting("FireFix", true, v -> this.page.getValue() == Page.Check));
    EnumSetting<AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo> autoSwap = this.add(new EnumSetting<AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo>("AutoSwap", AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Off, v -> this.page.getValue() == Page.General));
    EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.Server, v -> this.page.getValue() == Page.General));

    public Cleaner_iFwqnooxsJEmHoVteFeQ() {
        super("WebCleaner", Category.Combat);
        INSTANCE = this;
    }

    public static Block getBlock(BlockPos pos) {
        return Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState(pos).getBlock();
    }

    @Override
    public void onDisable() {
        this.directionVec = null;
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null && !this.noPosTimer.passed(this.noPosTimerSet.getValue())) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @EventHandler(priority = -199)
    public void onPacketSend(PacketEvent_gBzdMCvQxlHfSrulemGS.Send event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
            this.switchTimer.reset();
        }
    }

    public List<BlockPos> getWebPos(PlayerEntity player) {
        Vec3d playerPos = player.getPos();
        ArrayList<BlockPos> qzw = new ArrayList<BlockPos>();
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                for (int y : new int[]{0, 1, 2}) {
                    BlockPos pos = new BlockPosX(playerPos.getX() + (double) x, playerPos.getY(), playerPos.getZ() + (double) z).up(y);
                    if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world != null && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState(pos).getBlock() != Blocks.COBWEB)
                        continue;
                    qzw.add(pos);
                }
            }
        }
        return qzw;
    }

    @Override
    public void onUpdate() {
        if (Cleaner_iFwqnooxsJEmHoVteFeQ.nullCheck()) {
            return;
        }
        if (!this.delayTimer.passedMs((long) this.calcDelay.getValue())) {
            return;
        }
        if (Blink.INSTANCE.isOn()) {
            return;
        }
        this.state = 2.0f;
        if (this.checkBurrow.getValue() && !Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            return;
        }
        if (this.selfGround.getValue() && !Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.isOnGround() && !HexTech.PLAYER.insideBlock) {
            return;
        }
        if (this.state != 2.0f) {
            return;
        }
        if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player != null && this.eatingPause.getValue() && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.isUsingItem()) {
            return;
        }
        this.delayTimer.reset();
        List<BlockPos> webPos = this.getWebPos(Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player);
        if (webPos.size() >= 1) {
            for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
                if (this.behindWall(pos) || !this.canExplodeReach(pos, webPos.get(0), 6.1f) || !((double) this.calculateDamage(pos.toCenterPos(), Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player, Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player) <= this.maxSelf.getValue()))
                    continue;
                if (this.attack.getValue()) {
                    this.doBreak(pos);
                }
                if (this.fireFix.getValue()) {
                    CombatUtil.terrainIgnore = false;
                }
                if (!((double) pos.getY() - Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getY() <= 1.0) || !this.canTouch(pos.down()) || !this.canPlaceCrystal(pos, false, false) || !Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.isAir(pos) || Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState(pos).getBlock() == Blocks.FIRE || !this.place.getValue())
                    continue;
                this.doPlace(pos);
            }
        }
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        float damage = OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), player, predict, 6.0f);
        CombatUtil.modifyPos = null;
        return damage;
    }

    private void doPlace(BlockPos pos) {
        this.noPosTimer.reset();
        if (!(Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getMainHandStack().getItem().equals(Items.END_CRYSTAL) || Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getOffHandStack().getItem().equals(Items.END_CRYSTAL) || this.findCrystal())) {
            return;
        }
        if (!this.canTouch(pos.down())) {
            return;
        }
        BlockPos obsPos = pos.down();
        Direction facing = BlockUtil.getClickSide(obsPos);
        Vec3d vec = obsPos.toCenterPos().add((double) facing.getVector().getX() * 0.5, (double) facing.getVector().getY() * 0.5, (double) facing.getVector().getZ() * 0.5);
        if (facing != Direction.UP && facing != Direction.DOWN) {
            vec = vec.add(0.0, 0.45, 0.0);
        }
        if (this.rotate.getValue() && !this.faceVector(vec)) {
            return;
        }
        if (!this.placeTimer.passedMs((long) this.placeDelay.getValue())) {
            return;
        }
        if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getMainHandStack().getItem().equals(Items.END_CRYSTAL) || Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getOffHandStack().getItem().equals(Items.END_CRYSTAL)) {
            this.placeTimer.reset();
            this.syncPos = pos;
            this.placeCrystal(pos);
        } else {
            this.placeTimer.reset();
            this.syncPos = pos;
            int old = Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getInventory().selectedSlot;
            int crystal = this.getCrystal();
            if (crystal == -1) {
                return;
            }
            this.doSwap(crystal);
            this.placeCrystal(pos);
            if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Silent) {
                this.doSwap(old);
            } else if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
                this.doSwap(crystal);
                EntityUtil.syncInventory();
            }
        }
    }

    public boolean findCrystal() {
        if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Off) {
            return false;
        }
        return this.getCrystal() != -1;
    }

    private int getCrystal() {
        if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
            return InventoryUtil.findItem(Items.END_CRYSTAL);
        }
        if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            return InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL);
        }
        return -1;
    }

    private void doSwap(int slot) {
        if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
            InventoryUtil.switchToSlot(slot);
        } else if (this.autoSwap.getValue() == AutoCrystal_QcRVYRsOqpKivetoXSJa.Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            InventoryUtil.inventorySwap(slot, Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getInventory().selectedSlot);
        }
    }

    private void placeCrystal(BlockPos pos) {
        boolean offhand = Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
        BlockPos obsPos = pos.down();
        Direction facing = BlockUtil.getClickSide(obsPos);
        BlockUtil.clickBlock(obsPos, facing, false, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, this.swingMode.getValue());
    }

    private void doBreak(BlockPos pos) {
        this.noPosTimer.reset();
        this.lastBreakTimer.reset();
        Iterator<EndCrystalEntity> iterator = BlockUtil.getEndCrystals(new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1)).iterator();
        if (iterator.hasNext()) {
            EndCrystalEntity entity = iterator.next();
            if (this.rotate.getValue() && this.onBreak.getValue() && !this.faceVector(entity.getPos().add(0.0, this.yOffset.getValue(), 0.0))) {
                return;
            }
            if (!CombatUtil.breakTimer.passedMs((long) this.breakDelay.getValue())) {
                return;
            }
            CombatUtil.breakTimer.reset();
            this.syncPos = pos;
            mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.isSneaking()));
            Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.resetLastAttackedTicks();
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
            if (pos != null && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player != null && this.afterBreak.getValue() && (!this.yawStep.getValue() || !this.checkFov.getValue() || HexTech.ROTATE.inFov(entity.getPos(), this.fov.getValueFloat()))) {
                this.doPlace(pos);
            }
            if (this.forceWeb.getValue() && WebAuraTick.INSTANCE.isOn()) {
                WebAuraTick.force = true;
            }
        }
    }

    public boolean behindWall(BlockPos pos) {
        Vec3d testVec = new Vec3d((double) pos.getX() + 0.5, (double) pos.getY() + 1.7, (double) pos.getZ() + 0.5);
        BlockHitResult result = Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.raycast(new RaycastContext(EntityUtil.getEyesPos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player));
        if (result == null || result.getType() == HitResult.Type.MISS) {
            return false;
        }
        return Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getEyePos().distanceTo(pos.toCenterPos().add(0.0, -0.5, 0.0)) > this.wallRange.getValue();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean canTouch(BlockPos pos) {
        Direction side = BlockUtil.getClickSideStrict(pos);
        if (side == null) return false;
        Vec3d vec3d = new Vec3d((double) side.getVector().getX() * 0.5, (double) side.getVector().getY() * 0.5, (double) side.getVector().getZ() * 0.5);
        return pos.toCenterPos().add(vec3d).distanceTo(Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getEyePos()) <= this.range.getValue();
    }

    public boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        return (Cleaner_iFwqnooxsJEmHoVteFeQ.getBlock(obsPos) == Blocks.BEDROCK || Cleaner_iFwqnooxsJEmHoVteFeQ.getBlock(obsPos) == Blocks.OBSIDIAN) && BlockUtil.getClickSideStrict(obsPos) != null && this.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) && this.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) && (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.isAir(boost) || BlockUtil.hasCrystal(boost) && Cleaner_iFwqnooxsJEmHoVteFeQ.getBlock(boost) == Blocks.FIRE);
    }

    private boolean noEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        for (Entity entity : BlockUtil.getEntities(new Box(pos))) {
            if (!entity.isAlive() || ignoreItem && entity instanceof ItemEntity) continue;
            if (entity instanceof EndCrystalEntity) {
                if (!ignoreCrystal) {
                    return false;
                }
                if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.canSee(entity) || Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getEyePos().distanceTo(entity.getPos()) <= this.wallRange.getValue())
                    continue;
            }
            return false;
        }
        return true;
    }

    public boolean canExplodeReach(BlockPos explosionCenter, BlockPos targetBlock, float power) {
        if (explosionCenter.getY() > targetBlock.getY()) {
            return false;
        }
        Vec3d start = new Vec3d((double) explosionCenter.getX() + 0.5, (double) explosionCenter.getY() + 0.5, (double) explosionCenter.getZ() + 0.5);
        Vec3d end = new Vec3d((double) targetBlock.getX() + 0.5, (double) targetBlock.getY() + 0.5, (double) targetBlock.getZ() + 0.5);
        Vec3d direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        double step = 0.5;
        for (double d = 0.0; d < distance; d += step) {
            float resistance;
            Vec3d currentPos = start.add(direction.multiply(d));
            BlockPosX currentBlockPos = new BlockPosX(currentPos);
            BlockState blockState = null;
            if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world != null) {
                blockState = Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState(currentBlockPos);
            }
            if (!(blockState == null || !blockState.isAir() && (resistance = blockState.getBlock().getBlastResistance()) > power))
                continue;
            return false;
        }
        return true;
    }

    public boolean willBlockBeDestroyedByExplosion(World world, BlockPos explosionPos, BlockPos pos2, float power) {
        float blastResistance;
        float exposure;
        Explosion explosion = new Explosion(world, null, explosionPos.getX(), explosionPos.getY(), explosionPos.getZ(), power, false, Explosion.DestructionType.DESTROY);
        BlockState blockState = world.getBlockState(pos2);
        float doubleExplosionSize = 2.0f * explosion.getPower();
        double distancedsize = (double) MathHelper.sqrt((float) explosionPos.getSquaredDistance(pos2)) / (double) doubleExplosionSize;
        float damage = (float) ((1.0 - distancedsize) * (double) (exposure = this.getExposure(explosionPos.toCenterPos(), pos2)));
        return (double) damage > (double) (blastResistance = blockState.getBlock().getBlastResistance()) * 3.5;
    }

    public float getExposure(Vec3d source, BlockPos pos2) {
        Box box = new Box(pos2);
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (!(d < 0.0 || e < 0.0 || f < 0.0)) {
            int i = 0;
            int j = 0;
            for (double k = 0.0; k <= 1.0; k += d) {
                for (double l = 0.0; l <= 1.0; l += e) {
                    for (double m = 0.0; m <= 1.0; m += f) {
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        double o = MathHelper.lerp(l, box.minY, box.maxY);
                        double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                        Vec3d vec3d = new Vec3d(n + g, o, p + h);
                        if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world != null && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, new Cleaner_KppopygwsrtGITfPgSgG(this, pos2).predict)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return (float) i / (float) j;
        }
        return 0.0f;
    }

    @Override
    public void onEnable() {
        this.lastBreakTimer.reset();
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.yawStep.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        return HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat());
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum Page {
        General,
        Rotation,
        Check

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class Cleaner_KppopygwsrtGITfPgSgG {
        final PlayerEntity predict;

        public Cleaner_KppopygwsrtGITfPgSgG(Cleaner_iFwqnooxsJEmHoVteFeQ cleaner, BlockPos pos) {
            this.predict = new PlayerEntity(mc.world, pos.down(), 0.0f, new GameProfile(UUID.fromString("66123666-1234-5432-6666-667563866600"), "PredictEntity339")) {
                @Override
                public boolean isSpectator() {
                    return false;
                }

                @Override
                public boolean isCreative() {
                    return false;
                }
            };
            this.predict.setPosition(pos.toCenterPos().add(0.0, -1.0, 0.0));
            this.predict.setHealth(20.0f);
            this.predict.setOnGround(true);
        }
    }
}
