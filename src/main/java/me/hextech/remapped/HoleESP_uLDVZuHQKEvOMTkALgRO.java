package me.hextech.remapped;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.HoleESP;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class HoleESP_uLDVZuHQKEvOMTkALgRO
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final SliderSetting startFade = this.add(new SliderSetting("AlphaFade", 5.0, 1.0, 20.0));
    public final SliderSetting distance = this.add(new SliderSetting("Distance", 6.0, 1.0, 20.0));
    public final SliderSetting airHeight = this.add(new SliderSetting("AirHeight", 1.0, -3.0, 3.0, 0.1));
    public final BooleanSetting airYCheck = this.add(new BooleanSetting("AirYCheck", true));
    public final SliderSetting height = this.add(new SliderSetting("Height", 1.0, -3.0, 3.0, 0.1));
    final List<BlockPos> tempNormalList = new CopyOnWriteArrayList<BlockPos>();
    final List<BlockPos> tempBedrockList = new CopyOnWriteArrayList<BlockPos>();
    final List<BlockPos> tempAirList = new CopyOnWriteArrayList<BlockPos>();
    final Timer timer = new Timer();
    private final ColorSetting airFill = this.add(new ColorSetting("AirFill", new Color(148, 0, 0, 100)).injectBoolean(true));
    private final ColorSetting airBox = this.add(new ColorSetting("AirBox", new Color(148, 0, 0, 100)).injectBoolean(true));
    private final ColorSetting airFade = this.add(new ColorSetting("AirFade", new Color(148, 0, 0, 0)).injectBoolean(true));
    private final ColorSetting normalFill = this.add(new ColorSetting("NormalFill", new Color(255, 0, 0, 50)).injectBoolean(true));
    private final ColorSetting normalBox = this.add(new ColorSetting("NormalBox", new Color(255, 0, 0, 100)).injectBoolean(true));
    private final ColorSetting normalFade = this.add(new ColorSetting("NormalFade", new Color(255, 0, 0, 0)).injectBoolean(true));
    private final ColorSetting bedrockFill = this.add(new ColorSetting("BedrockFill", new Color(8, 255, 79, 50)).injectBoolean(true));
    private final ColorSetting bedrockBox = this.add(new ColorSetting("BedrockBox", new Color(8, 255, 79, 100)).injectBoolean(true));
    private final ColorSetting bedrockFade = this.add(new ColorSetting("BedrockFade", new Color(8, 255, 79, 100)).injectBoolean(true));
    private final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 50, 0, 1000));
    List<BlockPos> normalList = new CopyOnWriteArrayList<BlockPos>();
    List<BlockPos> bedrockList = new CopyOnWriteArrayList<BlockPos>();
    List<BlockPos> airList = new CopyOnWriteArrayList<BlockPos>();
    boolean drawing = false;

    public HoleESP_uLDVZuHQKEvOMTkALgRO() {
        super("HoleESP", Module_JlagirAibYQgkHtbRnhw.Render);
    }

    @Override
    public void onThread() {
        if (HoleESP_uLDVZuHQKEvOMTkALgRO.nullCheck()) {
            return;
        }
        if (!this.drawing && this.timer.passed(this.updateDelay.getValue())) {
            this.normalList = new CopyOnWriteArrayList<BlockPos>(this.tempNormalList);
            this.bedrockList = new CopyOnWriteArrayList<BlockPos>(this.tempBedrockList);
            this.airList = new CopyOnWriteArrayList<BlockPos>(this.tempAirList);
            this.timer.reset();
            this.tempBedrockList.clear();
            this.tempNormalList.clear();
            this.tempAirList.clear();
            for (BlockPos pos : BlockUtil.getSphere(this.distance.getValueFloat(), HoleESP_uLDVZuHQKEvOMTkALgRO.mc.player.method_19538())) {
                HoleESP type = this.isHole(pos);
                if (type == HoleESP.Bedrock) {
                    this.tempBedrockList.add(pos);
                    continue;
                }
                if (type == HoleESP.Normal) {
                    this.tempNormalList.add(pos);
                    continue;
                }
                if (type != HoleESP.Air) continue;
                this.tempAirList.add(pos);
            }
        }
    }

    HoleESP isHole(BlockPos pos) {
        if (HoleESP_uLDVZuHQKEvOMTkALgRO.mc.world.isAir(pos) && (!this.airYCheck.getValue() || pos.method_10264() == HoleESP_uLDVZuHQKEvOMTkALgRO.mc.player.method_31478() - 1) && CombatUtil.isHard(pos.up())) {
            return HoleESP.Air;
        }
        int blockProgress = 0;
        boolean bedRock = true;
        for (Direction i : Direction.values()) {
            if (i == Direction.UP || i == Direction.DOWN || !CombatUtil.isHard(pos.offset(i))) continue;
            if (HoleESP_uLDVZuHQKEvOMTkALgRO.mc.world.getBlockState(pos.offset(i)).getBlock() != Blocks.BEDROCK) {
                bedRock = false;
            }
            ++blockProgress;
        }
        if (HoleESP_uLDVZuHQKEvOMTkALgRO.mc.world.isAir(pos) && HoleESP_uLDVZuHQKEvOMTkALgRO.mc.world.isAir(pos.up()) && HoleESP_uLDVZuHQKEvOMTkALgRO.mc.world.isAir(pos.up(2)) && blockProgress > 3 && HoleESP_uLDVZuHQKEvOMTkALgRO.mc.world.method_39454((Entity)HoleESP_uLDVZuHQKEvOMTkALgRO.mc.player, new Box(pos.down()))) {
            if (bedRock) {
                return HoleESP.Bedrock;
            }
            return HoleESP.Normal;
        }
        if (CombatUtil.isDoubleHole(pos)) {
            return HoleESP.Normal;
        }
        return HoleESP.None;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        this.drawing = true;
        this.draw(matrixStack, this.bedrockList, this.bedrockFill, this.bedrockFade, this.bedrockBox, this.height.getValue());
        this.draw(matrixStack, this.airList, this.airFill, this.airFade, this.airBox, this.airHeight.getValue());
        this.draw(matrixStack, this.normalList, this.normalFill, this.normalFade, this.normalBox, this.height.getValue());
        this.drawing = false;
    }

    private void draw(MatrixStack matrixStack, List<BlockPos> list, ColorSetting fill, ColorSetting fade, ColorSetting box, double height) {
        for (BlockPos pos : list) {
            double distance = HoleESP_uLDVZuHQKEvOMTkALgRO.mc.player.method_19538().distanceTo(pos.toCenterPos());
            double alpha = distance > this.startFade.getValue() ? Math.max(Math.min(1.0, 1.0 - (distance - this.startFade.getValue()) / (this.distance.getValue() - this.startFade.getValue())), 0.0) : 1.0;
            Box espBox = new Box((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264() + height, (double)(pos.method_10260() + 1));
            if (fill.booleanValue) {
                if (fade.booleanValue) {
                    Render3DUtil.drawFadeFill(matrixStack, espBox, ColorUtil.injectAlpha(fill.getValue(), (int)((double)fill.getValue().getAlpha() * alpha)), ColorUtil.injectAlpha(fade.getValue(), (int)((double)fade.getValue().getAlpha() * alpha)));
                } else {
                    Render3DUtil.drawFill(matrixStack, espBox, ColorUtil.injectAlpha(fill.getValue(), (int)((double)fill.getValue().getAlpha() * alpha)));
                }
            }
            if (!box.booleanValue) continue;
            Render3DUtil.drawBox(matrixStack, espBox, ColorUtil.injectAlpha(box.getValue(), (int)((double)box.getValue().getAlpha() * alpha)));
        }
    }
}
