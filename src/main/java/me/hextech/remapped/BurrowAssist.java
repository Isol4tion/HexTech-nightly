package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import me.hextech.HexTech;
import me.hextech.remapped.BlockPosX;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.MeteorExplosionUtil;
import me.hextech.remapped.MineManager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PredictionSetting;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        super("BurrowAssist", Module_JlagirAibYQgkHtbRnhw.Combat);
        INSTANCE = this;
    }

    private static boolean canbur() {
        BlockPosX pos1 = new BlockPosX(BurrowAssist.mc.field_1724.method_23317() + 0.3, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() + 0.3);
        BlockPosX pos2 = new BlockPosX(BurrowAssist.mc.field_1724.method_23317() - 0.3, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() + 0.3);
        BlockPosX pos3 = new BlockPosX(BurrowAssist.mc.field_1724.method_23317() + 0.3, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() - 0.3);
        BlockPosX pos4 = new BlockPosX(BurrowAssist.mc.field_1724.method_23317() - 0.3, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() - 0.3);
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
        if (this.pause.getValue() && BurrowAssist.mc.field_1724.method_6115()) {
            return;
        }
        if (BurrowAssist.mc.field_1690.field_1903.method_1434()) {
            return;
        }
        if (!BurrowAssist.canbur()) {
            return;
        }
        if (BurrowAssist.mc.field_1724.method_24828() && this.getPlayerSpeed((PlayerEntity)BurrowAssist.mc.field_1724) < (double)this.speed.getValueInt() && (this.ccheck.getValue() && this.mcheck.getValue() ? this.findcrystal() || this.checkmine(this.mself.getValue()) : !(this.ccheck.getValue() && !this.findcrystal() || this.mcheck.getValue() && !this.checkmine(this.mself.getValue())))) {
            if (Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                return;
            }
            Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.enable();
            delay.reset();
        }
    }

    public boolean findcrystal() {
        PredictionSetting._XBpBEveLWEKUGQPHCCIS self = new PredictionSetting._XBpBEveLWEKUGQPHCCIS((PlayerEntity)BurrowAssist.mc.field_1724);
        for (Entity crystal : BurrowAssist.mc.field_1687.method_18112()) {
            float f;
            if (!(crystal instanceof EndCrystalEntity) || EntityUtil.getEyesPos().method_1022(crystal.method_19538()) > this.cRange.getValue()) continue;
            float selfDamage = this.calculateDamage(crystal.method_19538(), self.player, self.predict);
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
        return (double)MathHelper.method_15355((float)((float)input)) * 71.2729367892;
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player, PlayerEntity predict) {
        if (this.terrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        float damage = 0.0f;
        damage = (float)MeteorExplosionUtil.crystalDamage(player, pos, predict);
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    public boolean checkmine(boolean self) {
        ArrayList<BlockPos> pos = new ArrayList<BlockPos>();
        pos.add(EntityUtil.getPlayerPos(true));
        pos.add(new BlockPosX(BurrowAssist.mc.field_1724.method_23317() + 0.4, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() + 0.4));
        pos.add(new BlockPosX(BurrowAssist.mc.field_1724.method_23317() - 0.4, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() + 0.4));
        pos.add(new BlockPosX(BurrowAssist.mc.field_1724.method_23317() + 0.4, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() - 0.4));
        pos.add(new BlockPosX(BurrowAssist.mc.field_1724.method_23317() - 0.4, BurrowAssist.mc.field_1724.method_23318() + 0.5, BurrowAssist.mc.field_1724.method_23321() - 0.4));
        for (MineManager breakData : new HashMap<Integer, MineManager>(HexTech.BREAK.breakMap).values()) {
            if (breakData == null || breakData.getEntity() == null) continue;
            for (BlockPos pos1 : pos) {
                if (!pos1.equals((Object)breakData.pos) || breakData.getEntity() == BurrowAssist.mc.field_1724) continue;
                return true;
            }
        }
        if (!self) {
            return false;
        }
        for (BlockPos pos1 : pos) {
            if (!pos1.equals((Object)SpeedMine.breakPos)) continue;
            return true;
        }
        return false;
    }

    static {
        delay = new Timer();
    }
}
