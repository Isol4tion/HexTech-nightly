package me.hextech.remapped;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

import me.hextech.remapped.api.utils.Wrapper;
import me.hextech.remapped.mod.modules.impl.setting.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class TPUtil
implements Wrapper {
    public static ArrayList<Vec3> tp(Runnable runnable, Vec3d vec) {
        List<Vec3> tpPath = PathUtils.computePath(vec);
        tpPath.forEach(vec3 -> TPUtil.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(vec3.getX(), vec3.getY(), vec3.getZ(), false)));
        runnable.run();
        tpPath = Lists.reverse(tpPath);
        tpPath.forEach(vec3 -> TPUtil.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(vec3.getX(), vec3.getY(), vec3.getZ(), false)));
        if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.test.getValue()) {
            TPUtil.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0.0, -0.354844, 0.0, false));
            TPUtil.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0.0, 0.325488, 0.0, false));
            TPUtil.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0.0, -0.15441, 0.0, false));
            TPUtil.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0.0, -0.15444, 0.0, false));
        }
        return new ArrayList<Vec3>(tpPath);
    }
}
