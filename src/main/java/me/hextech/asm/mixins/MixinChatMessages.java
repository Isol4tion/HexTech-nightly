package me.hextech.asm.mixins;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.hextech.remapped.ChatSetting_qVnAbgCzNciNTevKRovy;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.TextCollector;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ChatMessages.class})
public class MixinChatMessages {
    @Final
    @Shadow
    private static OrderedText field_25263;

    @Shadow
    private static String method_1849(String message) {
        return "";
    }

    @Inject(method={"breakRenderedChatMessageLines"}, at={@At(value="HEAD")}, cancellable=true)
    private static void breakRenderedChatMessageLinesHook(StringVisitable message, int width, TextRenderer textRenderer, CallbackInfoReturnable<List<OrderedText>> cir) {
        TextCollector textCollector = new TextCollector();
        message.visit((style, messagex) -> {
            textCollector.add(StringVisitable.styled((String)MixinChatMessages.method_1849(messagex), (Style)style));
            return Optional.empty();
        }, Style.EMPTY);
        ArrayList list = Lists.newArrayList();
        textRenderer.getTextHandler().wrapLines(textCollector.getCombined(), width, Style.EMPTY, (text, lastLineWrapped) -> {
            OrderedText orderedText = Language.getInstance().reorder(text);
            OrderedText o = lastLineWrapped != false ? OrderedText.concat((OrderedText)field_25263, (OrderedText)orderedText) : orderedText;
            list.add(o);
            ChatSetting_qVnAbgCzNciNTevKRovy.chatMessage.put(o, message);
        });
        cir.setReturnValue((Object)(list.isEmpty() ? Lists.newArrayList((Object[])new OrderedText[]{OrderedText.EMPTY}) : list));
    }
}
