package me.hextech.remapped;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render3DUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

public class BaseFinder
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final File basedata;
    private static final File chestdata;
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 15, 0, 30));
    private final SliderSetting count = this.add(new SliderSetting("Count", 50, 1, 2000));
    private final BooleanSetting log = this.add(new BooleanSetting("SaveChestLog", true));
    private final Timer timer = new Timer();

    public BaseFinder() {
        super("BaseFinder", Module_JlagirAibYQgkHtbRnhw.Misc);
    }

    private static void writePacketData(File file, String data) {
        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(data);
                writer.newLine();
                writer.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }).start();
    }

    @Override
    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
        ArrayList<BlockEntity> blockEntities2 = BlockUtil.getTileEntities();
        for (BlockEntity blockEntity : blockEntities2) {
            if (!(blockEntity instanceof ChestBlockEntity) && !(blockEntity instanceof TrappedChestBlockEntity)) continue;
            Box box = new Box(blockEntity.method_11016());
            Render3DUtil.draw3DBox(matrixStack, box, this.color.getValue());
        }
        if (!this.timer.passed((long)this.delay.getValueInt() * 20L)) {
            return;
        }
        int chest = 0;
        ArrayList<BlockEntity> blockEntities = BlockUtil.getTileEntities();
        for (BlockEntity blockEntity : blockEntities) {
            if (!(blockEntity instanceof ChestBlockEntity) && !(blockEntity instanceof TrappedChestBlockEntity)) continue;
            ++chest;
            if (!this.log.getValue()) continue;
            BaseFinder.writePacketData(chestdata, "FindChest:" + String.valueOf(blockEntity.method_11016()));
        }
        if ((double)chest >= this.count.getValue()) {
            this.timer.reset();
            BaseFinder.writePacketData(basedata, "Find:" + String.valueOf(BaseFinder.mc.player.method_19538()) + " Count:" + chest);
            CommandManager.sendChatMessage("Find:" + String.valueOf(BaseFinder.mc.player.method_19538()) + " Count:" + chest);
            chest = 0;
        }
    }
}
