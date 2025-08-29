package me.hextech.api.managers;

import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.world.BlockPosX;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerManager_fDCPIpFeekihQpSyxOff
        implements Wrapper {
    public Map<PlayerEntity, EntityAttribute> map = new ConcurrentHashMap<>();
    public CopyOnWriteArrayList<PlayerEntity> inWebPlayers = new CopyOnWriteArrayList();
    public boolean insideBlock = false;

    public void onUpdate() {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        this.inWebPlayers.clear();
        this.insideBlock = EntityUtil.isInsideBlock();
        for (PlayerEntity player : new ArrayList<>(PlayerManager_fDCPIpFeekihQpSyxOff.mc.world.getPlayers())) {
            this.map.put(player, new EntityAttribute(player.getArmor(), player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)));
            this.webUpdate(player);
        }
    }

    public boolean isInWeb(PlayerEntity player) {
        return this.inWebPlayers.contains(player);
    }

    private void webUpdate(PlayerEntity player) {
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                for (int y : new int[]{-1, 0, 1, 2}) {
                    BlockPos pos = new BlockPosX(player.getX() + (double) x, player.getY(), player.getZ() + (double) z).up(y);
                    if (!new Box(pos).intersects(player.getBoundingBox()) || PlayerManager_fDCPIpFeekihQpSyxOff.mc.world.getBlockState(pos).getBlock() != Blocks.COBWEB)
                        continue;
                    this.inWebPlayers.add(player);
                    return;
                }
            }
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public record EntityAttribute(int armor, double toughness) {
    }
}
