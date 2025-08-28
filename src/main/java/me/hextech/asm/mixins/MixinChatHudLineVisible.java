package me.hextech.asm.mixins;

import me.hextech.api.interfaces.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ChatHudLine.Visible.class})
public class MixinChatHudLineVisible
implements IChatHudLine {
    @Unique
    private int id = 0;

    @Override
    public int nullpoint_nextgen_master$getId() {
        return this.id;
    }

    @Override
    public void nullpoint_nextgen_master$setId(int id) {
        this.id = id;
    }
}
