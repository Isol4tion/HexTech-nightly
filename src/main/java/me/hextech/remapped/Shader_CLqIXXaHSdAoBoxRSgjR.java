package me.hextech.remapped;

import java.awt.Color;
import me.hextech.HexTech;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class Shader_CLqIXXaHSdAoBoxRSgjR
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Shader_CLqIXXaHSdAoBoxRSgjR INSTANCE;
    private final EnumSetting<Shader> page = this.add(new EnumSetting<Shader>("Page", Shader.Shader));
    public final EnumSetting<ShaderManager.Mode> mode = this.add(new EnumSetting<ShaderManager.Mode>("Mode", ShaderManager.Mode.Solid, v -> this.page.getValue() == Shader.Shader));
    public final EnumSetting<ShaderManager.Mode> skyMode = this.add(new EnumSetting<ShaderManager.Mode>("SkyMode", ShaderManager.Mode.Solid, v -> this.page.getValue() == Shader.Shader));
    public final SliderSetting speed = this.add(new SliderSetting("Speed", 4.0, 0.0, 20.0, 0.1, v -> this.page.getValue() == Shader.Shader));
    public final ColorSetting fill = this.add(new ColorSetting("Color", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Shader));
    public final SliderSetting maxSample = this.add(new SliderSetting("MaxSample", 10.0, 0.0, 20.0, v -> this.page.getValue() == Shader.Shader));
    public final SliderSetting divider = this.add(new SliderSetting("Divider", 150.0, 0.0, 300.0, v -> this.page.getValue() == Shader.Shader));
    public final SliderSetting radius = this.add(new SliderSetting("Radius", 2.0, 0.0, 6.0, v -> this.page.getValue() == Shader.Shader));
    public final SliderSetting smoothness = this.add(new SliderSetting("Smoothness", 1.0, 0.0, 1.0, 0.01, v -> this.page.getValue() == Shader.Shader));
    public final SliderSetting alpha = this.add(new SliderSetting("GlowAlpha", 255, 0, 255, v -> this.page.getValue() == Shader.Shader));
    public final BooleanSetting sky = this.add(new BooleanSetting("Sky", false, v -> this.page.getValue() == Shader.Target));
    public final SliderSetting maxRange = this.add(new SliderSetting("MaxRange", 64, 16, 512, v -> this.page.getValue() == Shader.Target));
    public final SliderSetting factor = this.add(new SliderSetting("GradientFactor", 2.0, 0.0, 20.0, v -> this.page.getValue() == Shader.Legacy));
    public final SliderSetting gradient = this.add(new SliderSetting("Gradient", 2.0, 0.0, 20.0, v -> this.page.getValue() == Shader.Legacy));
    public final SliderSetting octaves = this.add(new SliderSetting("Octaves", 10, 5, 30, v -> this.page.getValue() == Shader.Legacy));
    public final ColorSetting smoke1 = this.add(new ColorSetting("Smoke1", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Legacy));
    public final ColorSetting smoke2 = this.add(new ColorSetting("Smoke2", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Legacy));
    public final ColorSetting smoke3 = this.add(new ColorSetting("Smoke3", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Legacy));
    private final BooleanSetting hands = this.add(new BooleanSetting("Hands", true, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting self = this.add(new BooleanSetting("Self", true, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting friends = this.add(new BooleanSetting("Friends", true, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting crystals = this.add(new BooleanSetting("Crystals", true, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting creatures = this.add(new BooleanSetting("Creatures", false, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting monsters = this.add(new BooleanSetting("Monsters", false, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting ambients = this.add(new BooleanSetting("Ambients", false, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting items = this.add(new BooleanSetting("Items", true, v -> this.page.getValue() == Shader.Target));
    private final BooleanSetting others = this.add(new BooleanSetting("Others", false, v -> this.page.getValue() == Shader.Target));

    public Shader_CLqIXXaHSdAoBoxRSgjR() {
        super("Shader", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    public boolean shouldRender(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (Shader_CLqIXXaHSdAoBoxRSgjR.mc.player == null) {
            return false;
        }
        if ((double)MathHelper.sqrt((float)Shader_CLqIXXaHSdAoBoxRSgjR.mc.player.squaredDistanceTo(entity.getPos())) > this.maxRange.getValue()) {
            return false;
        }
        if (entity instanceof PlayerEntity) {
            if (entity == Shader_CLqIXXaHSdAoBoxRSgjR.mc.player) {
                return this.self.getValue();
            }
            if (HexTech.FRIEND.isFriend((PlayerEntity)entity)) {
                return this.friends.getValue();
            }
            return this.players.getValue();
        }
        if (entity instanceof EndCrystalEntity) {
            return this.crystals.getValue();
        }
        if (entity instanceof ItemEntity) {
            return this.items.getValue();
        }

        return switch (entity.getType().getSpawnGroup()) {
            case CREATURE, WATER_CREATURE -> this.creatures.getValue();
            case MONSTER -> this.monsters.getValue();
            case AMBIENT, WATER_AMBIENT -> this.ambients.getValue();
            default -> this.others.getValue();
        };
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        if (this.hands.getValue()) {
            HexTech.SHADER.renderShader(() -> Shader_CLqIXXaHSdAoBoxRSgjR.mc.gameRenderer.renderHand(matrixStack, Shader_CLqIXXaHSdAoBoxRSgjR.mc.gameRenderer.getCamera(), mc.getTickDelta()), this.mode.getValue());
        }
    }

    @Override
    public void onToggle() {
        HexTech.SHADER.reloadShaders();
    }

    @Override
    public void onLogin() {
        HexTech.SHADER.reloadShaders();
    }
}
