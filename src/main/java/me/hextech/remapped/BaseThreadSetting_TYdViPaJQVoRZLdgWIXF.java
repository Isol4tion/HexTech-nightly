package me.hextech.remapped;

import java.util.ArrayList;
import me.hextech.HexTech;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.BaseThreadSetting;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.Cleaner_iFwqnooxsJEmHoVteFeQ;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.ComboBreaks;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn;
import me.hextech.remapped.ListenerDamage;
import me.hextech.remapped.ListenerHelper;
import me.hextech.remapped.ListenerHelperUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.NoRotateSet;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.PredictionSetting;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BaseThreadSetting_TYdViPaJQVoRZLdgWIXF
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static final Timer placeTimer = new Timer();
    public static final Timer placeBaseTimer = new Timer();
    public static BaseThreadSetting_TYdViPaJQVoRZLdgWIXF INSTANCE;
    public static BlockPos crystalPos;
    public final Timer delayTimer = new Timer();
    public final EnumSetting<BaseThreadSetting> page = this.add(new EnumSetting<BaseThreadSetting>("Page", BaseThreadSetting.Thread));
    public final BooleanSetting multiThread = this.add(new BooleanSetting("MultiThread", true, v -> this.page.is(BaseThreadSetting.Thread)));
    public final BooleanSetting crystalThread = this.add(new BooleanSetting("CrystalThread", true, v -> this.page.is(BaseThreadSetting.Thread)));
    public final BooleanSetting rotatepacket = this.add(new BooleanSetting("PacketRotate", false, v -> this.page.is(BaseThreadSetting.Thread)));
    public final BooleanSetting jumpCooldown = this.add(new BooleanSetting("JumpCooldown", true, v -> this.page.is(BaseThreadSetting.Change)));
    public final BooleanSetting antiopen = this.add(new BooleanSetting("AntiOpen", true, v -> this.page.is(BaseThreadSetting.Change)));
    public final BooleanSetting packethook = this.add(new BooleanSetting("MixinPacketHook", true, v -> this.page.is(BaseThreadSetting.Mixin)));
    public final BooleanSetting movehook = this.add(new BooleanSetting("MixinMoveHook", true, v -> this.page.is(BaseThreadSetting.Mixin)));
    public final SliderSetting minrad = this.add(new SliderSetting("OffTrackRadian", 0, 0, 30, v -> this.page.is(BaseThreadSetting.Dev)).setSuffix("/-Age"));
    public final BooleanSetting attack = this.add(new BooleanSetting("Attack\u00a74(DeSync)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Aweb = this.add(new BooleanSetting("Attack\u00a74(AutoWeb)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Async = this.add(new BooleanSetting("Attack\u00a74(AutoCrystal.INSTANCE)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Arenderslinder = this.add(new BooleanSetting("Attack\u00a74(AutoCrystal)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Arotate = this.add(new BooleanSetting("Attack\u00a74(Rotate)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting defend = this.add(new BooleanSetting("Defend\u00a7b(AutoCrystal.INSTANCE)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Dweb = this.add(new BooleanSetting("Defend\u00a7b(AutoWeb)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Dsync = this.add(new BooleanSetting("Defend\u00a7b(AutoCrystal.INSTANCE)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Drenderslinder = this.add(new BooleanSetting("Defend\u00a7b(AutoCrystal)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting Drotate = this.add(new BooleanSetting("Defend\u00a7b(Rotate)", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting smart = this.add(new BooleanSetting("Smart-Out1", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting smart2 = this.add(new BooleanSetting("Smart-Out2", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting smart3 = this.add(new BooleanSetting("Smart-Out3", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting inmoveSync = this.add(new BooleanSetting("MovingSync", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting staticSync = this.add(new BooleanSetting("StaticSync", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final BooleanSetting burrowcleaner = this.add(new BooleanSetting("StartBurrowCleaner", false, v -> this.page.is(BaseThreadSetting.ComboBreaks)));
    public final SliderSetting blockposx = this.add(new SliderSetting("BlockPosX", 0.3, 0.0, 1.0, v -> this.page.is(BaseThreadSetting.Fix)));
    public final BooleanSetting nullfix = this.add(new BooleanSetting("AntiWorldNull", true, v -> this.page.is(BaseThreadSetting.Fix)));
    public final SliderSetting fadeInQuad = this.add(new SliderSetting("FadeInQuad", 1, -1, 1, v -> this.page.is(BaseThreadSetting.FadeUtils)));
    public final SliderSetting fadeInEnd = this.add(new SliderSetting("FadeEnd", 1, -1, 1, v -> this.page.is(BaseThreadSetting.FadeUtils)));
    public final SliderSetting fadeInlength = this.add(new SliderSetting("FadeInLength", 3, -3, 3, v -> this.page.is(BaseThreadSetting.FadeUtils)));
    public PlayerEntity displayTarget;
    public float breakDamage;
    public float tempDamage;
    public float lastDamage;
    public BlockPos tempPos;
    public Vec3d directionVec = null;

    public BaseThreadSetting_TYdViPaJQVoRZLdgWIXF() {
        super("BaseThreadSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    public static boolean faceVector(Vec3d directionVec) {
        if (!AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.ObbyVector.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        if (HexTech.ROTATE.inFov(directionVec, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.fov.getValueFloat())) {
            return true;
        }
        return !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.checkLook.getValue();
    }

    @Override
    public void onDisable() {
        crystalPos = null;
        this.tempPos = null;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
    }

    @Override
    public void onThread() {
        if (!this.multiThread.getValue()) {
            this.updateCrystalPos();
        }
    }

    @Override
    public void onUpdate() {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.cblink.getValue() && Blink.INSTANCE.isOn()) {
            this.tempPos = null;
            crystalPos = null;
            return;
        }
        if (this.burrowcleaner.getValue()) {
            if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.enable();
            } else {
                Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.disable();
            }
        }
        if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.SmartActive.getValue() && EntityUtil.isInsideBlock()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
        }
        if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.onlyStatic.getValue() && MovementUtil.isJumping()) {
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.disable();
        }
        if (this.inmoveSync.getValue() && !MovementUtil.isMoving()) {
            CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateSync.setValue(true);
        }
        if (this.staticSync.getValue() && MovementUtil.isMoving()) {
            CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateSync.setValue(false);
        }
        if (this.smart.getValue() && !MovementUtil.isMoving()) {
            ComboBreaks.INSTANCE.disable();
        }
        if (this.smart3.getValue() && MovementUtil.isMoving()) {
            ComboBreaks.INSTANCE.enable();
            this.smart2.setValue(false);
        }
        if (this.smart2.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            ComboBreaks.INSTANCE.enable();
            this.smart3.setValue(false);
        }
        if (ComboBreaks.INSTANCE.isOn()) {
            if (this.Arenderslinder.getValue()) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.sliderSpeed.setValue(ComboBreaks.INSTANCE.Arender.getValueFloat());
            }
            if (this.Aweb.getValue()) {
                WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.placeDelay.setValue(ComboBreaks.INSTANCE.Aweb.getValueFloat());
            }
            if (this.Async.getValue()) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceWeb.setValue(this.isOff());
            }
            if (this.attack.getValue()) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.HurtTime.setValue(ComboBreaks.INSTANCE.attackSync.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTime.setValue(ComboBreaks.INSTANCE.AlastSync.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.OnlySyncTime.setValue(ComboBreaks.INSTANCE.AonlySync.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SpamSyncTime.setValue(ComboBreaks.INSTANCE.AspamTime.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.placeDelay.setValue(ComboBreaks.INSTANCE.Acyrstal.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.breakDelay.setValue(ComboBreaks.INSTANCE.Abreak.getValueFloat());
            }
            if (this.Arotate.getValue()) {
                NoRotateSet.INSTANCE.lagTime.setValue(ComboBreaks.INSTANCE.Arotate.getValueFloat());
            }
        } else {
            if (this.Drenderslinder.getValue()) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.sliderSpeed.setValue(ComboBreaks.INSTANCE.Drender.getValueFloat());
            }
            if (this.Dweb.getValue()) {
                WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.placeDelay.setValue(ComboBreaks.INSTANCE.Dweb.getValueFloat());
            }
            if (this.Dsync.getValue()) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceWeb.setValue(this.isOn());
            }
            if (this.defend.getValue()) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.HurtTime.setValue(ComboBreaks.INSTANCE.DattackSync.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SyncTime.setValue(ComboBreaks.INSTANCE.DlastSync.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.OnlySyncTime.setValue(ComboBreaks.INSTANCE.DonlySync.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.SpamSyncTime.setValue(ComboBreaks.INSTANCE.DspamTime.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.placeDelay.setValue(ComboBreaks.INSTANCE.Dcrystal.getValueFloat());
                AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.breakDelay.setValue(ComboBreaks.INSTANCE.Dbreak.getValueFloat());
            }
            if (this.Drotate.getValue()) {
                NoRotateSet.INSTANCE.lagTime.setValue(ComboBreaks.INSTANCE.Drotate.getValueFloat());
            }
        }
        if (this.jumpCooldown.getValue()) {
            BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.field_6228 = 0;
        }
        if (this.multiThread.getValue()) {
            this.updateCrystalPos();
        }
        this.doUpdate();
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        PlayerMoveC2SPacket packet;
        Object t;
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.raytracebypass.getValue() && (t = event.getPacket()) instanceof PlayerMoveC2SPacket) {
            packet = (PlayerMoveC2SPacket)t;
            if (RotateManager.lastPitch != -91.0f) {
                ((IPlayerMoveC2SPacket)packet).setPitch(-91.0f);
            }
        }
        if (this.antiopen.getValue()) {
            if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.nullCheck() || !((t = event.getPacket()) instanceof PlayerInteractBlockC2SPacket)) {
                return;
            }
            packet = (PlayerInteractBlockC2SPacket)t;
            Block block = BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.world.getBlockState(packet.getBlockHitResult().getBlockPos()).getBlock();
            if (!BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.isSneaking() && (block instanceof ChestBlock || block instanceof EnderChestBlock)) {
                event.cancel();
            }
        }
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        if (!this.multiThread.getValue()) {
            this.updateCrystalPos();
        }
        this.doUpdate();
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (!this.multiThread.getValue()) {
            this.updateCrystalPos();
        }
        this.doUpdate();
    }

    private void doUpdate() {
        if (crystalPos != null) {
            ListenerHelper.doBase(crystalPos);
        }
    }

    private void updateCrystalPos() {
        this.update();
        this.lastDamage = this.tempDamage;
        crystalPos = this.tempPos;
    }

    private void update() {
        if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.nullCheck()) {
            return;
        }
        if (PredictionSetting.INSTANCE.prediction.getValue()) {
            ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.updateHistory();
        }
        if (!this.delayTimer.passedMs((long)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.calcdelay.getValue())) {
            return;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.eatingPause.getValue() && BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.isUsingItem()) {
            this.tempPos = null;
            return;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.breakOnlyHasCrystal.getValue() && !BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.getMainHandStack().getItem().equals(Items.OBSIDIAN) && !BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.getOffHandStack().getItem().equals(Items.END_CRYSTAL) && !AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.findCrystal()) {
            this.tempPos = null;
            return;
        }
        this.delayTimer.reset();
        this.breakDamage = 0.0f;
        this.tempPos = null;
        this.tempDamage = 0.0f;
        ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS> list = new ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS>();
        for (PlayerEntity target : CombatUtil.getEnemies(AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.targetRange.getValueFloat())) {
            if (target.hurtTime > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.HurtTime.getValueInt()) continue;
            list.add(new PredictionSetting._XBpBEveLWEKUGQPHCCIS(target));
        }
        PredictionSetting._XBpBEveLWEKUGQPHCCIS self = new PredictionSetting._XBpBEveLWEKUGQPHCCIS((PlayerEntity)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player);
        if (!list.isEmpty()) {
            for (BlockPos pos : BlockUtil.getSphere((float)AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue() + 1.0f)) {
                CombatUtil.modifyPos = null;
                if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.getEyePos().distanceTo(pos.toCenterPos().add(0.0, -0.5, 0.0)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.range.getValue() || !ListenerHelperUtil.canPlaceCrystal(pos, true, false)) continue;
                CombatUtil.modifyPos = pos.down();
                CombatUtil.modifyBlockState = Blocks.OBSIDIAN.getDefaultState();
                if (ListenerHelperUtil.behindWall(pos) || !ListenerHelperUtil.canTouch(pos.down())) continue;
                for (PredictionSetting._XBpBEveLWEKUGQPHCCIS pap : list) {
                    float selfDamage;
                    if (pos.down().getY() > pap.player.method_31478() || AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.lite.getValue() && ListenerHelperUtil.liteCheck(pos.toCenterPos().add(0.0, -0.5, 0.0), pap.predict.getPos())) continue;
                    float damage = ListenerHelperUtil.calculateBase(pos, pap.player, pap.predict);
                    if (this.tempPos != null && !(damage > this.tempDamage) || (double)(selfDamage = ListenerHelperUtil.calculateBase(pos, self.player, self.predict)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.maxSelf.getValue() || AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.noSuicide.getValue() > 0.0 && (double)selfDamage > (double)(BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.getHealth() + BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.mc.player.getAbsorptionAmount()) - AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.noSuicide.getValue() || damage < EntityUtil.getHealth((Entity)pap.player) && ((double)damage < ListenerDamage.getDamage(pap.player) || AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.smart.getValue() && (ListenerDamage.getDamage(pap.player) == AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.forceMin.getValue() ? (double)damage < (double)selfDamage - 2.5 : damage < selfDamage))) continue;
                    this.displayTarget = pap.player;
                    this.tempPos = pos.down();
                    this.tempDamage = damage;
                }
            }
            CombatUtil.modifyPos = null;
            if (this.tempPos != null && !BlockUtil.canPlace(this.tempPos, AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue())) {
                this.tempPos = null;
                this.tempDamage = 0.0f;
            }
        }
    }

    @Override
    public void enable() {
        this.state = true;
    }

    @Override
    public void disable() {
        this.state = true;
    }

    @Override
    public boolean isOn() {
        return true;
    }
}
