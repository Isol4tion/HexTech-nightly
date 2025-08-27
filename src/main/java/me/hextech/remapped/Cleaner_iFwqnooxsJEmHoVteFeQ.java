package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.Cleaner_KppopygwsrtGITfPgSgG;
import me.hextech.remapped.Cleaner_zDJWekpHpTWvapSemLWN;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Enum_rNhWITNdkrqkhKfDZgGo;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.OyveyExplosionUtil;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SwingSide;
import me.hextech.remapped.Timer;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class Cleaner_iFwqnooxsJEmHoVteFeQ
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Cleaner_iFwqnooxsJEmHoVteFeQ INSTANCE;
    public final Timer lastBreakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer noPosTimer = new Timer();
    private final Timer switchTimer = new Timer();
    private final Timer delayTimer = new Timer();
    private final List<BlockPos> checkPos = new ArrayList<BlockPos>();
    public Vec3d directionVec = null;
    public BlockPos syncPos;
    public float state = 2.0f;
    EnumSetting<Cleaner_zDJWekpHpTWvapSemLWN> page = this.add(new EnumSetting<Cleaner_zDJWekpHpTWvapSemLWN>("Page", Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation).setParent());
    private final BooleanSetting onBreak = this.add(new BooleanSetting("OnBreak", false, v -> this.rotate.isOpen() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.onBreak.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, v -> this.rotate.isOpen() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 30, 0, 50, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final SliderSetting noPosTimerSet = this.add(new SliderSetting("NoPosTimer", 50, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Rotation));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final BooleanSetting attack = this.add(new BooleanSetting("Attack", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final BooleanSetting forceWeb = this.add(new BooleanSetting("ForceWeb", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final BooleanSetting afterBreak = this.add(new BooleanSetting("PlaceAfterBreak", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 1.0, 10.0, 0.1, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("m"));
    private final SliderSetting wallRange = this.add(new SliderSetting("WallRange", 5.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final SliderSetting maxSelf = this.add(new SliderSetting("MaxSelf", 3.0, 0.0, 36.0, 0.1, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("ms"));
    private final SliderSetting breakDelay = this.add(new SliderSetting("BreakDelay", 0, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("ms"));
    private final SliderSetting calcDelay = this.add(new SliderSetting("CalcDelay", 250, 0, 1000, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General).setSuffix("ms"));
    private final BooleanSetting checkBurrow = this.add(new BooleanSetting("CheckBurrow", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Check));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Check));
    private final BooleanSetting fireFix = this.add(new BooleanSetting("FireFix", true, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.Check));
    EnumSetting<Enum_rNhWITNdkrqkhKfDZgGo> autoSwap = this.add(new EnumSetting<Enum_rNhWITNdkrqkhKfDZgGo>("AutoSwap", Enum_rNhWITNdkrqkhKfDZgGo.Off, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.Server, v -> this.page.getValue() == Cleaner_zDJWekpHpTWvapSemLWN.General));
    private final float lastYaw = 0.0f;
    private final float lastPitch = 0.0f;

    public Cleaner_iFwqnooxsJEmHoVteFeQ() {
        super("WebCleaner", Module_JlagirAibYQgkHtbRnhw.Combat);
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

    @EventHandler(priority=-199)
    public void onPacketSend(PacketEvent event) {
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
                    BlockPos pos = new BlockPosX(playerPos.getX() + (double)x, playerPos.getY(), playerPos.getZ() + (double)z).up(y);
                    if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world != null && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState(pos).getBlock() != Blocks.COBWEB) continue;
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
        if (!this.delayTimer.passedMs((long)this.calcDelay.getValue())) {
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
        List<BlockPos> webPos = this.getWebPos((PlayerEntity)Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player);
        if (webPos.size() >= 1) {
            for (BlockPos pos : BlockUtil.getSphere(this.range.getValueFloat())) {
                if (this.behindWall(pos) || !this.canExplodeReach(pos, webPos.get(0), 6.1f) || !((double)this.calculateDamage(pos.toCenterPos(), (PlayerEntity)Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player, (PlayerEntity)Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player) <= this.maxSelf.getValue())) continue;
                if (this.attack.getValue()) {
                    this.doBreak(pos);
                }
                if (this.fireFix.getValue()) {
                    CombatUtil.terrainIgnore = false;
                }
                if (!((double)pos.getY() - Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getY() <= 1.0) || !this.canTouch(pos.down()) || !this.canPlaceCrystal(pos, false, false) || !Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.isAir(pos) || Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState(pos).getBlock() == Blocks.FIRE || !this.place.getValue()) continue;
                this.doPlace(pos);
            }
        }
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        float damage = OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), (Entity)player, (Entity)predict, 6.0f);
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
        Vec3d vec = obsPos.toCenterPos().add((double)facing.getVector().getX() * 0.5, (double)facing.getVector().getY() * 0.5, (double)facing.getVector().getZ() * 0.5);
        if (facing != Direction.UP && facing != Direction.DOWN) {
            vec = vec.add(0.0, 0.45, 0.0);
        }
        if (this.rotate.getValue() && !this.faceVector(vec)) {
            return;
        }
        if (!this.placeTimer.passedMs((long)this.placeDelay.getValue())) {
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
            if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent) {
                this.doSwap(old);
            } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
                this.doSwap(crystal);
                EntityUtil.syncInventory();
            }
        }
    }

    public boolean findCrystal() {
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Off) {
            return false;
        }
        return this.getCrystal() != -1;
    }

    private int getCrystal() {
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
            return InventoryUtil.findItem(Items.END_CRYSTAL);
        }
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            return InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL);
        }
        return -1;
    }

    private void doSwap(int slot) {
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
            InventoryUtil.switchToSlot(slot);
        } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
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
        Iterator<EndCrystalEntity> iterator = BlockUtil.getEndCrystals(new Box((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1))).iterator();
        if (iterator.hasNext()) {
            EndCrystalEntity entity = iterator.next();
            if (this.rotate.getValue() && this.onBreak.getValue() && !this.faceVector(entity.getPos().add(0.0, this.yOffset.getValue(), 0.0))) {
                return;
            }
            if (!CombatUtil.breakTimer.passedMs((long)this.breakDelay.getValue())) {
                return;
            }
            CombatUtil.breakTimer.reset();
            this.syncPos = pos;
            mc.getNetworkHandler().sendPacket((Packet)PlayerInteractEntityC2SPacket.attack((Entity)entity, (boolean)Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.isSneaking()));
            Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.resetLastAttackedTicks();
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
            if (pos != null && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player != null && this.afterBreak.getValue() && (!this.yawStep.getValue() || !this.checkFov.getValue() || HexTech.ROTATE.inFov(entity.getPos(), this.fov.getValueFloat()))) {
                this.doPlace(pos);
            }
            if (this.forceWeb.getValue() && WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.isOn()) {
                WebAuraTick_gaIdrzDzsbegzNTtPQoV.force = true;
            }
            return;
        }
    }

    public boolean behindWall(BlockPos pos) {
        Vec3d testVec = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.7, (double)pos.getZ() + 0.5);
        BlockHitResult result = Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.method_17742(new RaycastContext(EntityUtil.getEyesPos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player));
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
        Vec3d vec3d = new Vec3d((double)side.getVector().getX() * 0.5, (double)side.getVector().getY() * 0.5, (double)side.getVector().getZ() * 0.5);
        if (!(pos.toCenterPos().add(vec3d).distanceTo(Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getEyePos()) <= this.range.getValue())) return false;
        return true;
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
                if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.canSee(entity) || Cleaner_iFwqnooxsJEmHoVteFeQ.mc.player.getEyePos().distanceTo(entity.getPos()) <= this.wallRange.getValue()) continue;
            }
            return false;
        }
        return true;
    }

    public boolean canExplodeReach(BlockPos explosionCenter, BlockPos targetBlock, float power) {
        if (explosionCenter.getY() > targetBlock.getY()) {
            return false;
        }
        Vec3d start = new Vec3d((double)explosionCenter.getX() + 0.5, (double)explosionCenter.getY() + 0.5, (double)explosionCenter.getZ() + 0.5);
        Vec3d end = new Vec3d((double)targetBlock.getX() + 0.5, (double)targetBlock.getY() + 0.5, (double)targetBlock.getZ() + 0.5);
        Vec3d direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        double step = 0.5;
        for (double d = 0.0; d < distance; d += step) {
            float resistance;
            Vec3d currentPos = start.add(direction.multiply(d));
            BlockPosX currentBlockPos = new BlockPosX(currentPos);
            BlockState blockState = null;
            if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world != null) {
                blockState = Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.getBlockState((BlockPos)currentBlockPos);
            }
            if (!(blockState == null || !blockState.method_26215() && (resistance = blockState.getBlock().getBlastResistance()) > power)) continue;
            return false;
        }
        return true;
    }

    public boolean willBlockBeDestroyedByExplosion(World world, BlockPos explosionPos, BlockPos pos2, float power) {
        float blastResistance;
        float exposure;
        Explosion explosion = new Explosion(world, null, (double)explosionPos.getX(), (double)explosionPos.getY(), (double)explosionPos.getZ(), power, false, Explosion.DestructionType.DESTROY);
        BlockState blockState = world.getBlockState(pos2);
        float doubleExplosionSize = 2.0f * explosion.getPower();
        double distancedsize = (double)MathHelper.sqrt((float)((float)explosionPos.method_10262((Vec3i)pos2))) / (double)doubleExplosionSize;
        float damage = (float)((1.0 - distancedsize) * (double)(exposure = this.getExposure(explosionPos.toCenterPos(), pos2)));
        return (double)damage > (double)(blastResistance = blockState.getBlock().getBlastResistance()) * 3.5;
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
                        double n = MathHelper.lerp((double)k, (double)box.minX, (double)box.maxX);
                        double o = MathHelper.lerp((double)l, (double)box.minY, (double)box.maxY);
                        double p = MathHelper.lerp((double)m, (double)box.minZ, (double)box.maxZ);
                        Vec3d vec3d = new Vec3d(n + g, o, p + h);
                        if (Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world != null && Cleaner_iFwqnooxsJEmHoVteFeQ.mc.world.method_17742(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)new Cleaner_KppopygwsrtGITfPgSgG((Cleaner_iFwqnooxsJEmHoVteFeQ)this, (Cleaner_iFwqnooxsJEmHoVteFeQ)this, (BlockPos)pos2).predict)).method_17783() == HitResult.Type.MISS) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return (float)i / (float)j;
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
}
