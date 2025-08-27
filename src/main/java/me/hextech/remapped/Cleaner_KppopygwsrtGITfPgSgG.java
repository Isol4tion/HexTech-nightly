package me.hextech.remapped;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.hextech.remapped.Cleaner;
import me.hextech.remapped.Cleaner_iFwqnooxsJEmHoVteFeQ;
import me.hextech.remapped.Wrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public static class Cleaner_KppopygwsrtGITfPgSgG {
    final PlayerEntity predict;
    final /* synthetic */ Cleaner_iFwqnooxsJEmHoVteFeQ this$0;

    public Cleaner_KppopygwsrtGITfPgSgG(Cleaner_iFwqnooxsJEmHoVteFeQ this$0, Cleaner_iFwqnooxsJEmHoVteFeQ cleaner, BlockPos pos) {
        this.this$0 = this$0;
        this.predict = new Cleaner(this, (World)Wrapper.mc.world, pos.method_10074(), 0.0f, new GameProfile(UUID.fromString("66123666-1234-5432-6666-667563866600"), "PredictEntity339"), this$0);
        this.predict.method_33574(pos.toCenterPos().method_1031(0.0, -1.0, 0.0));
        this.predict.method_6033(20.0f);
        this.predict.method_24830(true);
    }
}
