package me.hextech.asm.mixins;

import java.util.HashMap;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.ChatSetting_qVnAbgCzNciNTevKRovy;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.FadeUtils_DPfHthPqEJdfXfNYhDbG;
import me.hextech.remapped.IChatHud;
import me.hextech.remapped.IChatHudLine;
import me.hextech.remapped.ReceiveMessageEvent;
import me.hextech.remapped.TextUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ChatHud.class})
public abstract class MixinChatHud
implements IChatHud {
    @Unique
    private final HashMap<ChatHudLine.Visible, FadeUtils_DPfHthPqEJdfXfNYhDbG> map = new HashMap();
    @Shadow
    @Final
    private List<ChatHudLine.Visible> field_2064;
    @Shadow
    @Final
    private List<ChatHudLine> field_2061;
    @Unique
    private int nullpoint_nextId = 0;
    @Unique
    private ChatHudLine.Visible last;

    @Shadow
    public abstract void method_1812(Text var1);

    @Override
    public void nullpoint_nextgen_master$add(Text message, int id) {
        this.nullpoint_nextId = id;
        this.method_1812(message);
        this.nullpoint_nextId = 0;
    }

    @Inject(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"}, at={@At(value="INVOKE", target="Ljava/util/List;add(ILjava/lang/Object;)V", ordinal=0, shift=At.Shift.AFTER)})
    private void onAddMessageAfterNewChatHudLineVisible(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        ((IChatHudLine)this.field_2064.get(0)).nullpoint_nextgen_master$setId(this.nullpoint_nextId);
    }

    @Inject(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"}, at={@At(value="INVOKE", target="Ljava/util/List;add(ILjava/lang/Object;)V", ordinal=1, shift=At.Shift.AFTER)})
    private void onAddMessageAfterNewChatHudLine(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        ((IChatHudLine)this.field_2061.get(0)).nullpoint_nextgen_master$setId(this.nullpoint_nextId);
    }

    @Inject(at={@At(value="HEAD")}, method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"})
    private void onAddMessage(Text message, @Nullable MessageSignatureData signature, int ticks, @Nullable MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        if (this.nullpoint_nextId != 0) {
            this.field_2064.removeIf(msg -> msg == null || ((IChatHudLine)msg).nullpoint_nextgen_master$getId() == this.nullpoint_nextId);
            this.field_2061.removeIf(msg -> msg == null || ((IChatHudLine)msg).nullpoint_nextgen_master$getId() == this.nullpoint_nextId);
        }
    }

    @Redirect(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=2, remap=false))
    public int chatLinesSize(List<ChatHudLine.Visible> list) {
        return ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.isOn() && ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.infiniteChat.getValue() ? -2147483647 : list.size();
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private int drawStringWithShadow(DrawContext drawContext, TextRenderer textRenderer, OrderedText text, int x, int y, int color) {
        if (ChatSetting_qVnAbgCzNciNTevKRovy.chatMessage.containsKey(text) && ChatSetting_qVnAbgCzNciNTevKRovy.chatMessage.get(text).getString().startsWith("\u00a7(")) {
            if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.pulse.booleanValue) {
                return TextUtil.drawStringPulse(drawContext, text, x, y, ColorUtil.injectAlpha(ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.color.getValue(), color >> 24 & 0xFF), ColorUtil.injectAlpha(ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.pulse.getValue(), color >> 24 & 0xFF), ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.pulseSpeed.getValue(), ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.pulseCounter.getValueInt());
            }
            return drawContext.drawTextWithShadow(textRenderer, text, x, y, ColorUtil.injectAlpha(ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.color.getValue(), color >> 24 & 0xFF).getRGB());
        }
        return drawContext.drawTextWithShadow(textRenderer, text, x, y, color);
    }

    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Ljava/util/List;get(I)Ljava/lang/Object;", ordinal=0, remap=false))
    private int get(int i) {
        this.last = this.field_2064.get(i);
        if (this.last != null && !this.map.containsKey(this.last)) {
            this.map.put(this.last, new FadeUtils_DPfHthPqEJdfXfNYhDbG(ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.animateTime.getValueInt()).reset());
        }
        return i;
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I", ordinal=0, shift=At.Shift.BEFORE)})
    private void translate(DrawContext context, int currentTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.map.containsKey(this.last)) {
            context.getMatrices().translate(ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.animateOffset.getValue() * (1.0 - this.map.get(this.last).getQuad(ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.animQuad.getValue())), 0.0, 0.0);
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, cancellable=true)
    public void onAddMessage(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        ReceiveMessageEvent event = new ReceiveMessageEvent(message.getString());
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
