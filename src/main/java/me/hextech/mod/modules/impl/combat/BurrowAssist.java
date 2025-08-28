package me.hextech.mod.modules.impl.combat;

import me.hextech.HexTech;
import me.hextech.api.managers.MineManager_aMxFbgVZCMGgbqNPBFpw;
import me.hextech.api.utils.combat.CombatUtil;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.api.utils.world.MeteorExplosionUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.setting.PredictionSetting;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;

public class BurrowAssist
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static BurrowAssist INSTANCE;
    public static Timer delay;
    public final HashMap<PlayerEntity, Double> playerSpeeds = new HashMap();
    private final SliderSetting Delay = this.add(new SliderSetting("Delay", 100, 0, 1000));
    private final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 4, 0, 10));
    private final BooleanSetting terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true));
    private final SliderSetting cRange = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0));
    private final SliderSetting breakMinSelf = this.add(new SliderSetting("BreakSelf", 12.0, 0.0, 36.0));
    public BooleanSetting pause = this.add(new BooleanSetting("PauseEat", true));
    public SliderSetting speed = this.add(new SliderSetting("MaxSpeed", 8, 0, 20));
    public BooleanSetting ccheck = this.add(new BooleanSetting("CheckCrystal", true).setParent());
    public BooleanSetting mcheck = this.add(new BooleanSetting("CheckMine", true).setParent());
    public BooleanSetting checkPos = this.add(new BooleanSetting("CheckPos", true, v -> this.mcheck.isOpen()));
    public BooleanSetting mself = this.add(new BooleanSetting("Self", true));

    public BurrowAssist() {
        super("BurrowAssist", Category.Combat);
        INSTANCE = this;
    }

    private static boolean canbur() {
        BlockPosX pos1 = new BlockPosX(BurrowAssist.mc.player.getX() + 0.3, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() + 0.3);
        BlockPosX pos2 = new BlockPosX(BurrowAssist.mc.player.getX() - 0.3, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() + 0.3);
        BlockPosX pos3 = new BlockPosX(BurrowAssist.mc.player.getX() + 0.3, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() - 0.3);
        BlockPosX pos4 = new BlockPosX(BurrowAssist.mc.player.getX() - 0.3, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() - 0.3);
        BlockPos playerPos = EntityUtil.getPlayerPos(true);
        return Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos1) || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos2) || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos3) || Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.canPlace(pos4);
    }

    @Override
    public void onUpdate() {
        if (BurrowAssist.nullCheck()) {
            return;
        }
        if (!delay.passed((long)this.Delay.getValue())) {
            return;
        }
        if (this.pause.getValue() && BurrowAssist.mc.player.isUsingItem()) {
            return;
        }
        if (BurrowAssist.mc.options.jumpKey.isPressed()) {
            return;
        }
        if (!BurrowAssist.canbur()) {
            return;
        }
        if (BurrowAssist.mc.player.isOnGround() && this.getPlayerSpeed(BurrowAssist.mc.player) < (double)this.speed.getValueInt() && (this.ccheck.getValue() && this.mcheck.getValue() ? this.findcrystal() || this.checkmine(this.mself.getValue()) : !(this.ccheck.getValue() && !this.findcrystal() || this.mcheck.getValue() && !this.checkmine(this.mself.getValue())))) {
            if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                return;
            }
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
            delay.reset();
        }
    }

    public boolean findcrystal() {
        PredictionSetting._XBpBEveLWEKUGQPHCCIS self = new PredictionSetting._XBpBEveLWEKUGQPHCCIS(BurrowAssist.mc.player);
        for (Entity crystal : BurrowAssist.mc.world.getEntities()) {
            float f = 0.0f;
            if (!(crystal instanceof EndCrystalEntity) || EntityUtil.getEyesPos().distanceTo(crystal.getPos()) > this.cRange.getValue()) continue;
            float selfDamage = this.calculateDamage(crystal.getPos(), self.player, self.predict);
            if ((double)f < this.breakMinSelf.getValue()) continue;
            return true;
        }
        return false;
    }

    public double getPlayerSpeed(PlayerEntity player) {
        if (this.playerSpeeds.get(player) == null) {
            return 0.0;
        }
        return this.turnIntoKpH(this.playerSpeeds.get(player));
    }

    public double turnIntoKpH(double input) {
        return (double)MathHelper.sqrt((float)input) * 71.2729367892;
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        if (this.terrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        float damage = 0.0f;
        damage = (float) MeteorExplosionUtil.crystalDamage(player, pos, predict);
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    public boolean checkmine(boolean self) {
        ArrayList<BlockPos> pos = new ArrayList<BlockPos>();
        pos.add(EntityUtil.getPlayerPos(true));
        pos.add(new BlockPosX(BurrowAssist.mc.player.getX() + 0.4, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() + 0.4));
        pos.add(new BlockPosX(BurrowAssist.mc.player.getX() - 0.4, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() + 0.4));
        pos.add(new BlockPosX(BurrowAssist.mc.player.getX() + 0.4, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() - 0.4));
        pos.add(new BlockPosX(BurrowAssist.mc.player.getX() - 0.4, BurrowAssist.mc.player.getY() + 0.5, BurrowAssist.mc.player.getZ() - 0.4));
        for (MineManager_aMxFbgVZCMGgbqNPBFpw.MineInfo breakData : new HashMap<Integer, MineManager_aMxFbgVZCMGgbqNPBFpw.MineInfo>(HexTech.BREAK.breakMap).values()) {
            if (breakData == null || breakData.getEntity() == null) continue;
            for (BlockPos pos1 : pos) {
                if (!pos1.equals(breakData.pos) || breakData.getEntity() == BurrowAssist.mc.player) continue;
                return true;
            }
        }
        if (!self) {
            return false;
        }
        for (BlockPos pos1 : pos) {
            if (!pos1.equals(SpeedMine.breakPos)) continue;
            return true;
        }
        return false;
    }

    static {
        delay = new Timer();
    }
}
