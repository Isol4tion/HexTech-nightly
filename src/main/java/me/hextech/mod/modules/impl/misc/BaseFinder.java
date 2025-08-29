package me.hextech.mod.modules.impl.misc;

import me.hextech.api.managers.CommandManager;
import me.hextech.api.managers.Manager;
import me.hextech.api.utils.math.Timer;
import me.hextech.api.utils.render.Render3DUtil;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BaseFinder
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final File basedata = Manager.getFile("Base.txt");
    private static final File chestdata = Manager.getFile("Chest,txt");
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 15, 0, 30));
    private final SliderSetting count = this.add(new SliderSetting("Count", 50, 1, 2000));
    private final BooleanSetting log = this.add(new BooleanSetting("SaveChestLog", true));
    private final Timer timer = new Timer();

    public BaseFinder() {
        super("BaseFinder", Category.Misc);
    }

    private static void writePacketData(File file, String data) {
        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(data);
                writer.newLine();
                writer.close();
            } catch (IOException iOException) {
                // empty catch block
            }
        }).start();
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        ArrayList<BlockEntity> blockEntities2 = BlockUtil.getTileEntities();
        for (BlockEntity blockEntity : blockEntities2) {
            if (!(blockEntity instanceof ChestBlockEntity)) continue;
            Box box = new Box(blockEntity.getPos());
            Render3DUtil.draw3DBox(matrixStack, box, this.color.getValue());
        }
        if (!this.timer.passed((long) this.delay.getValueInt() * 20L)) {
            return;
        }
        int chest = 0;
        ArrayList<BlockEntity> blockEntities = BlockUtil.getTileEntities();
        for (BlockEntity blockEntity : blockEntities) {
            if (!(blockEntity instanceof ChestBlockEntity)) continue;
            ++chest;
            if (!this.log.getValue()) continue;
            BaseFinder.writePacketData(chestdata, "FindChest:" + blockEntity.getPos());
        }
        if ((double) chest >= this.count.getValue()) {
            this.timer.reset();
            BaseFinder.writePacketData(basedata, "Find:" + BaseFinder.mc.player.getPos() + " Count:" + chest);
            CommandManager.sendChatMessage("Find:" + BaseFinder.mc.player.getPos() + " Count:" + chest);
            chest = 0;
        }
    }
}
