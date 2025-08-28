package me.hextech.remapped;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.api.managers.CommandManager;
import me.hextech.remapped.mod.modules.impl.setting.ChatSetting_qVnAbgCzNciNTevKRovy;
import me.hextech.remapped.mod.modules.settings.Setting;
import me.hextech.remapped.mod.modules.settings.impl.BindSetting;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import me.hextech.remapped.mod.modules.settings.impl.ColorSetting;
import me.hextech.remapped.mod.modules.settings.impl.EnumSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;

public abstract class Module_eSdgMXWuzcxgQVaJFmKZ
extends Mod {
    public final BooleanSetting drawnSetting;
    private final Module_JlagirAibYQgkHtbRnhw category;
    private final BindSetting bindSetting;
    private final List<Setting> settings = new ArrayList<Setting>();
    public boolean state;
    private String description;

    public Module_eSdgMXWuzcxgQVaJFmKZ(String name, Module_JlagirAibYQgkHtbRnhw category) {
        this(name, "", category);
    }

    public Module_eSdgMXWuzcxgQVaJFmKZ(String name, String description, Module_JlagirAibYQgkHtbRnhw category) {
        super(name);
        this.category = category;
        this.description = description;
        ModuleManager.lastLoadMod = this;
        this.bindSetting = new BindSetting("Key", name.equalsIgnoreCase("UI") ? 89 : -1);
        this.drawnSetting = this.add(new BooleanSetting("Drawn", true));
        this.drawnSetting.hide();
    }

    public static void sendNotify(String string) {
        Notify_EXlgYplaRzfgofOPOkyB.notifyList.add(new Notify(string));
    }

    public static void sendSequencedPacket(SequencedPacketCreator packetCreator) {
        if (mc.getNetworkHandler() == null || Module_eSdgMXWuzcxgQVaJFmKZ.mc.world == null) {
            return;
        }
        try (PendingUpdateManager pendingUpdateManager = Module_eSdgMXWuzcxgQVaJFmKZ.mc.world.getPendingUpdateManager().incrementSequence()){
            int i = pendingUpdateManager.getSequence();
            mc.getNetworkHandler().sendPacket(packetCreator.predict(i));
        }
    }

    public static boolean nullCheck() {
        return Module_eSdgMXWuzcxgQVaJFmKZ.mc.player == null || Module_eSdgMXWuzcxgQVaJFmKZ.mc.world == null;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Module_JlagirAibYQgkHtbRnhw getCategory() {
        return this.category;
    }

    public BindSetting getBind() {
        return this.bindSetting;
    }

    public boolean isOn() {
        return this.state;
    }

    public boolean isOff() {
        return !this.isOn();
    }

    public void toggle() {
        if (this.isOn()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public void enable() {
        if (this.state) {
            return;
        }
        if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck() && this.drawnSetting.getValue()) {
            int id = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
            if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Both) {
                switch (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                    case HexTech: {
                        CommandManager.sendChatMessageWidthId("\u00a7f" + this.getName() + " \u00a7a\u221a", id);
                        break;
                    }
                    case Earth: {
                        CommandManager.sendChatMessageWidthIdNoSync("\u00a7l" + this.getName() + " \u00a7aEnabled", id);
                    }
                }
                Module_eSdgMXWuzcxgQVaJFmKZ.sendNotify("\u00a7f[\u00a7b" + this.getName() + "\u00a7f]\u00a77 toggled \u00a7aon");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Notify) {
                Module_eSdgMXWuzcxgQVaJFmKZ.sendNotify("\u00a7f[\u00a7b" + this.getName() + "\u00a7f]\u00a77 toggled \u00a7aon");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Chat) {
                switch (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                    case HexTech: {
                        CommandManager.sendChatMessageWidthId("\u00a7f" + this.getName() + " \u00a7a\u221a", id);
                        break;
                    }
                    case Earth: {
                        CommandManager.sendChatMessageWidthIdNoSync("\u00a7l" + this.getName() + " \u00a7aEnabled", id);
                    }
                }
            }
        }
        this.state = true;
        HexTech.EVENT_BUS.subscribe(this);
        this.onToggle();
        try {
            this.onEnable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        if (!this.state) {
            return;
        }
        if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck() && this.drawnSetting.getValue()) {
            int id = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
            if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Both) {
                switch (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                    case HexTech: {
                        CommandManager.sendChatMessageWidthId("\u00a7f" + this.getName() + " \u00a7cX", id);
                        break;
                    }
                    case Earth: {
                        CommandManager.sendChatMessageWidthIdNoSync("\u00a7l" + this.getName() + " \u00a7cDisabled", id);
                    }
                }
                Module_eSdgMXWuzcxgQVaJFmKZ.sendNotify("\u00a7f[\u00a7b" + this.getName() + "\u00a7f]\u00a77 toggled \u00a74off");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Notify) {
                Module_eSdgMXWuzcxgQVaJFmKZ.sendNotify("\u00a7f[\u00a7b" + this.getName() + "\u00a7f]\u00a77 toggled \u00a74off");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify._hvcAdwcUFPZabyUezEMv.Chat) {
                switch (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                    case HexTech: {
                        CommandManager.sendChatMessageWidthId("\u00a7f" + this.getName() + " \u00a7cX", id);
                        break;
                    }
                    case Earth: {
                        CommandManager.sendChatMessageWidthIdNoSync("\u00a7l" + this.getName() + " \u00a7cDisabled", id);
                    }
                }
            }
        }
        this.state = false;
        HexTech.EVENT_BUS.unsubscribe(this);
        this.onToggle();
        this.onDisable();
    }

    public void setState(boolean state) {
        if (this.state == state) {
            return;
        }
        if (state) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public boolean setBind(String rkey) {
        int key;
        if (rkey.equalsIgnoreCase("none")) {
            this.bindSetting.setKey(-1);
            return true;
        }
        try {
            key = InputUtil.fromTranslationKey("key.keyboard." + rkey.toLowerCase()).getCode();
        }
        catch (NumberFormatException e) {
            if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
                CommandManager.sendChatMessage("\u00a7c[!] \u00a7fBad key!");
            }
            return false;
        }
        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        if (key == 0) {
            return false;
        }
        this.bindSetting.setKey(key);
        return true;
    }

    public void addSetting(Setting setting) {
        this.settings.add(setting);
    }

    public StringSetting add(StringSetting setting) {
        this.addSetting(setting);
        return setting;
    }

    public ColorSetting add(ColorSetting setting) {
        this.addSetting(setting);
        return setting;
    }

    public SliderSetting add(SliderSetting setting) {
        this.addSetting(setting);
        return setting;
    }

    public BooleanSetting add(BooleanSetting setting) {
        this.addSetting(setting);
        return setting;
    }

    public <T extends Enum<T>> EnumSetting<T> add(EnumSetting<T> setting) {
        this.addSetting(setting);
        return setting;
    }

    public BindSetting add(BindSetting setting) {
        this.addSetting(setting);
        return setting;
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public void onDisable() {
    }

    public void onEnable() throws IOException {
    }

    public void onToggle() {
    }

    public void onRender3D(MatrixStack matrixStack) {
    }

    public void onUpdate() {
    }

    public void onThread() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onRender2D(DrawContext drawContext, float tickDelta) {
    }

    public void onRender3D(MatrixStack matrixStack, float partialTicks) {
    }

    public void sendPacket(Packet<?> packet) {
        if (mc.getNetworkHandler() == null) {
            return;
        }
        mc.getNetworkHandler().sendPacket(packet);
    }

    public final boolean isCategory(Module_JlagirAibYQgkHtbRnhw category) {
        return category == this.category;
    }

    public String getArrayName() {
        return this.getName() + this.getArrayInfo();
    }

    public String getArrayInfo() {
        return this.getInfo() == null ? "" : " \u00a77[" + this.getInfo() + "\u00a77]";
    }

    public String getInfo() {
        return null;
    }
}
