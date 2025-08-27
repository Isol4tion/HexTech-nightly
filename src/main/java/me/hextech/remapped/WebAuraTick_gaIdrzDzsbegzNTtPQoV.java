package me.hextech.remapped;

import java.util.ArrayList;
import me.hextech.HexTech;
import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.AutoTrap;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FinalHoleKick;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import me.hextech.remapped.WebAuraTick;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class WebAuraTick_gaIdrzDzsbegzNTtPQoV
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static WebAuraTick_gaIdrzDzsbegzNTtPQoV INSTANCE;
    public static float lastYaw;
    public static float lastPitch;
    public static boolean force;
    public static boolean ignore;
    public final EnumSetting<WebAuraTick> page = this.add(new EnumSetting<WebAuraTick>("Page", WebAuraTick.General));
    public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 2, 1, 10, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 2.0, 0.0, 50.0, 1.0, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting maxWebs = this.add(new SliderSetting("MaxWebs", 2.0, 1.0, 8.0, 1.0, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting burrowMaxWebs = this.add(new SliderSetting("BurrowMaxWebs", 2.0, 1.0, 8.0, 1.0, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 8.0, 0.0, 8.0, 0.1, v -> this.page.getValue() == WebAuraTick.General));
    public final SliderSetting offset = this.add(new SliderSetting("Offset", 0.25, 0.0, 0.3, 0.01, v -> this.page.getValue() == WebAuraTick.General));
    public final ArrayList<BlockPos> pos = new ArrayList();
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting noMine = this.add(new BooleanSetting("NoMine", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting interact = this.add(new BooleanSetting("InteractPacket", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting seqpack = this.add(new BooleanSetting("SequencedPacket", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting onlyTick = this.add(new BooleanSetting("OnlyTick", false, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting face = this.add(new BooleanSetting("Face", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting noPushFaceHT = this.add(new BooleanSetting("NoFacePush[HT]", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting noPushFaceFK = this.add(new BooleanSetting("NoFacePush[FK]", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting preferAnchor = this.add(new BooleanSetting("WaitAnchor", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting waitTrap = this.add(new BooleanSetting("WaitTrap", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, v -> this.page.getValue() == WebAuraTick.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, v -> this.page.getValue() == WebAuraTick.Rotate).setParent());
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, v -> this.rotate.isOpen() && this.page.getValue() == WebAuraTick.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.3, 0.1, 1.0, 0.01, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == WebAuraTick.Rotate));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == WebAuraTick.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 30, 0, 50, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.checkFov.getValue() && this.page.getValue() == WebAuraTick.Rotate));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, v -> this.rotate.isOpen() && this.yawStep.getValue() && this.page.getValue() == WebAuraTick.Rotate));
    private final Timer timer = new Timer();
    public Vec3d directionVec = null;
    int progress = 0;
    int tempMaxWebs = 1;

    public WebAuraTick_gaIdrzDzsbegzNTtPQoV() {
        super("WebAuraTick", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        if (this.pos.isEmpty()) {
            return null;
        }
        return "Working";
    }

    @EventHandler
    public void onRotate(OffTrackEvent event) {
        if (this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        if (!this.onlyTick.getValue()) {
            this.onUpdate();
        }
    }

    @Override
    public void onDisable() {
        force = false;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (!this.onlyTick.getValue()) {
            this.onUpdate();
        }
    }

    @Override
    public void onUpdate() {
        if (force) {
            ignore = true;
        }
        if (this.noPushFaceHT.getValue() && HoleKickTest.INSTANCE.isOn()) {
            this.face.setValue(false);
            this.noPushFaceFK.setValue(false);
        }
        if (this.noPushFaceHT.getValue() && HoleKickTest.INSTANCE.isOff()) {
            this.face.setValue(true);
        }
        if (this.noPushFaceFK.getValue() && FinalHoleKick.INSTANCE.isOn()) {
            this.face.setValue(false);
            this.noPushFaceHT.setValue(false);
        }
        if (this.noPushFaceFK.getValue() && FinalHoleKick.INSTANCE.isOff()) {
            this.face.setValue(true);
        }
        this.update();
    }

    private void update() {
        if (!this.timer.passedMs(this.placeDelay.getValueInt())) {
            return;
        }
        if (this.cancelBurrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            return;
        }
        if (this.waitTrap.getValue() && AutoTrap.INSTANCE.isOn()) {
            return;
        }
        this.pos.clear();
        this.progress = 0;
        this.directionVec = null;
        if (this.preferAnchor.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null) {
            return;
        }
        if (this.getWebSlot() == -1) {
            return;
        }
        if (this.usingPause.getValue() && WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.player.isUsingItem()) {
            return;
        }
        block0: for (PlayerEntity player : CombatUtil.getEnemies(this.targetRange.getValue())) {
            this.tempMaxWebs = (int)this.maxWebs.getValue();
            if (this.isInBurrow(player)) {
                this.tempMaxWebs = (int)this.burrowMaxWebs.getValue();
            }
            Vec3d playerPos = this.predictTicks.getValue() > 0.0 ? CombatUtil.getEntityPosVec(player, this.predictTicks.getValueInt()) : player.method_19538();
            int webs = 0;
            if (this.down.getValue()) {
                this.placeWeb(new BlockPosX(playerPos.method_10216(), playerPos.method_10214() - 0.8, playerPos.method_10215()));
            }
            ArrayList<BlockPosX> list = new ArrayList<BlockPosX>();
            for (float x : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                for (float z : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                    for (float y : new float[]{0.0f, 1.0f, -1.0f}) {
                        BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214() + (double)y, playerPos.method_10215() + (double)z);
                        if (list.contains((Object)pos)) continue;
                        list.add(pos);
                        if (!this.isTargetHere(pos, player) || WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.COBWEB || HexTech.BREAK.isMining(pos)) continue;
                        ++webs;
                    }
                }
            }
            if (webs >= this.tempMaxWebs && !ignore) continue;
            boolean skip = false;
            if (this.feet.getValue()) {
                block4: for (float x : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                    for (float z : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                        BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214(), playerPos.method_10215() + (double)z);
                        if (!this.isTargetHere(pos, player) || !this.placeWeb(pos) || ++webs < this.tempMaxWebs) continue;
                        skip = true;
                        break block4;
                    }
                }
            }
            if (skip || !this.face.getValue()) continue;
            for (float x : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                for (float z : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                    BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214() + 1.1, playerPos.method_10215() + (double)z);
                    if (this.isTargetHere(pos, player) && this.placeWeb(pos) && ++webs >= this.tempMaxWebs) continue block0;
                }
            }
        }
    }

    private boolean isTargetHere(BlockPos pos, PlayerEntity target) {
        return new Box(pos).intersects(target.getBoundingBox());
    }

    private boolean placeWeb(BlockPos pos) {
        if (this.pos.contains(pos)) {
            return false;
        }
        this.pos.add(pos);
        if (this.progress >= this.blocksPer.getValueInt()) {
            return false;
        }
        if (this.getWebSlot() == -1) {
            return false;
        }
        if (this.detectMining.getValue() && (HexTech.BREAK.isMining(pos) || !this.noMine.getValue() && pos.equals((Object)SpeedMine.breakPos))) {
            return false;
        }
        if (BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) != null && (WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world.isAir(pos) || ignore && BlockUtil.getBlock(pos) == Blocks.COBWEB) && pos.method_10264() < 320) {
            int oldSlot = WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.player.getInventory().selectedSlot;
            int webSlot = this.getWebSlot();
            if (!this.placeBlock(pos, this.rotate.getValue(), webSlot)) {
                return false;
            }
            if (this.noMine.getValue() && pos.equals((Object)SpeedMine.breakPos)) {
                SpeedMine.breakPos = null;
            }
            BlockUtil.placedPos.add(pos);
            ++this.progress;
            if (this.inventorySwap.getValue()) {
                this.doSwap(webSlot);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(oldSlot);
            }
            force = false;
            ignore = false;
            this.timer.reset();
            return true;
        }
        return false;
    }

    public boolean isInBurrow(PlayerEntity player) {
        for (float x : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
            for (float z : new float[]{0.0f, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                BlockPosX pos = new BlockPosX(player.getX() + (double)x, player.getY() + (double)0.15f, player.getZ() + (double)z);
                if (WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.OBSIDIAN && WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.BEDROCK && WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.ENDER_CHEST && WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world.getBlockState((BlockPos)pos).getBlock() != Blocks.RESPAWN_ANCHOR) continue;
                return true;
            }
        }
        return false;
    }

    public boolean placeBlock(BlockPos pos, boolean rotate, int slot) {
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            if (BlockUtil.airPlace()) {
                return this.clickBlock(pos, Direction.DOWN, rotate, slot);
            }
            return false;
        }
        return this.clickBlock(pos.offset(side), side.getOpposite(), rotate, slot);
    }

    public boolean clickBlock(BlockPos pos, Direction side, boolean rotate, int slot) {
        Vec3d directionVec = new Vec3d((double)pos.method_10263() + 0.5 + (double)side.method_10163().getX() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().getY() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().getZ() * 0.5);
        if (rotate && !this.faceVector(directionVec)) {
            return false;
        }
        this.doSwap(slot);
        EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        if (this.interact.getValue()) {
            WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, BlockUtil.getWorldActionId(WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.world)));
        }
        if (this.seqpack.getValue()) {
            Module_eSdgMXWuzcxgQVaJFmKZ.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, id));
        }
        return true;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.yawStep.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
        }
        return !this.checkFov.getValue();
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, WebAuraTick_gaIdrzDzsbegzNTtPQoV.mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getWebSlot() {
        if (this.inventorySwap.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.COBWEB);
        }
        return InventoryUtil.findBlock(Blocks.COBWEB);
    }
}
