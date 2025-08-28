package me.hextech.remapped.mod.modules.impl.setting;

import java.awt.Color;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.api.managers.CommandManager;
import me.hextech.remapped.api.utils.combat.CombatUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Debug
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Debug INSTANCE;
    private final BooleanSetting prerender = this.add(new BooleanSetting("PredictRender", true));
    private final BooleanSetting chat = this.add(new BooleanSetting("Chat", true));
    public ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 200)));
    public SliderSetting ticks = this.add(new SliderSetting("Ticks", 5, 0, 40));

    public Debug() {
        super("Debug", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (Debug.nullCheck()) {
            return;
        }
        if (Debug.mc.world != null) {
            for (AbstractClientPlayerEntity player : Debug.mc.world.getPlayers()) {
                Vec3d vec3d = CombatUtil.getEntityPosVec(player, this.ticks.getValueInt());
                if (this.prerender.getValue()) {
                    Render3DUtil.draw3DBox(matrixStack, new Box(BlockPos.ofFloored(vec3d)), this.color.getValue());
                }
                if (!this.chat.getValue()) continue;
                CommandManager.sendChatMessage(vec3d.x + " " + vec3d.y + " " + vec3d.z);
            }
        }
    }
}
