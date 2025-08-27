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
        message.method_27658((style, messagex) -> {
            textCollector.method_27462(StringVisitable.method_29431((String)MixinChatMessages.method_1849(messagex), (Style)style));
            return Optional.empty();
        }, Style.field_24360);
        ArrayList list = Lists.newArrayList();
        textRenderer.method_27527().method_29971(textCollector.method_27463(), width, Style.field_24360, (text, lastLineWrapped) -> {
            OrderedText orderedText = Language.method_10517().method_30934(text);
            OrderedText o = lastLineWrapped != false ? OrderedText.method_30742((OrderedText)field_25263, (OrderedText)orderedText) : orderedText;
            list.add(o);
            ChatSetting_qVnAbgCzNciNTevKRovy.chatMessage.put(o, message);
        });
        cir.setReturnValue(list.isEmpty() ? Lists.newArrayList(OrderedText.field_26385) : list);
    }
}
