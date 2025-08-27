package me.hextech.remapped;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;

public abstract class Module_eSdgMXWuzcxgQVaJFmKZ extends Mod {
   public final BooleanSetting drawnSetting;
   private final Module_JlagirAibYQgkHtbRnhw category;
   private final BindSetting bindSetting;
   private final List<Setting> settings = new ArrayList();
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
      if (mc.method_1562() != null && mc.field_1687 != null) {
         PendingUpdateManager pendingUpdateManager = mc.field_1687.method_41925().method_41937();

         try {
            int i = pendingUpdateManager.method_41942();
            mc.method_1562().method_52787(packetCreator.predict(i));
         } catch (Throwable var5) {
            if (pendingUpdateManager != null) {
               try {
                  pendingUpdateManager.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (pendingUpdateManager != null) {
            pendingUpdateManager.close();
         }
      }
   }

   public static boolean nullCheck() {
      return mc.field_1724 == null || mc.field_1687 == null;
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
      if (!this.state) {
         if (!nullCheck() && this.drawnSetting.getValue()) {
            if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Both) {
               int id = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
               switch ((ChatSetting)ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                  case HexTech:
                     CommandManager.sendChatMessageWidthId("§f" + this.getName() + " §a√", id);
                     break;
                  case Earth:
                     CommandManager.sendChatMessageWidthIdNoSync("§l" + this.getName() + " §aEnabled", id);
               }

               sendNotify("§f[§b" + this.getName() + "§f]§7 toggled §aon");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
               && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Notify) {
               sendNotify("§f[§b" + this.getName() + "§f]§7 toggled §aon");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Chat) {
               int id = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
               switch ((ChatSetting)ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                  case HexTech:
                     CommandManager.sendChatMessageWidthId("§f" + this.getName() + " §a√", id);
                     break;
                  case Earth:
                     CommandManager.sendChatMessageWidthIdNoSync("§l" + this.getName() + " §aEnabled", id);
               }
            }
         }

         this.state = true;
         me.hextech.HexTech.EVENT_BUS.subscribe(this);
         this.onToggle();

         try {
            this.onEnable();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }
   }

   public void disable() {
      if (this.state) {
         if (!nullCheck() && this.drawnSetting.getValue()) {
            int id = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
            if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Both) {
               switch ((ChatSetting)ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                  case HexTech:
                     CommandManager.sendChatMessageWidthId("§f" + this.getName() + " §cX", id);
                     break;
                  case Earth:
                     CommandManager.sendChatMessageWidthIdNoSync("§l" + this.getName() + " §cDisabled", id);
               }

               sendNotify("§f[§b" + this.getName() + "§f]§7 toggled §4off");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn()
               && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Notify) {
               sendNotify("§f[§b" + this.getName() + "§f]§7 toggled §4off");
            } else if (Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.isOn() && Notify_EXlgYplaRzfgofOPOkyB.INSTANCE.type.getValue() == Notify_hvcAdwcUFPZabyUezEMv.Chat) {
               switch ((ChatSetting)ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageStyle.getValue()) {
                  case HexTech:
                     CommandManager.sendChatMessageWidthId("§f" + this.getName() + " §cX", id);
                     break;
                  case Earth:
                     CommandManager.sendChatMessageWidthIdNoSync("§l" + this.getName() + " §cDisabled", id);
               }
            }
         }

         this.state = false;
         me.hextech.HexTech.EVENT_BUS.unsubscribe(this);
         this.onToggle();
         this.onDisable();
      }
   }

   public void setState(boolean state) {
      if (this.state != state) {
         if (state) {
            this.enable();
         } else {
            this.disable();
         }
      }
   }

   public boolean setBind(String rkey) {
      if (rkey.equalsIgnoreCase("none")) {
         this.bindSetting.setKey(-1);
         return true;
      } else {
         int key;
         try {
            key = InputUtil.method_15981("key.keyboard." + rkey.toLowerCase()).method_1444();
         } catch (NumberFormatException var4) {
            if (!nullCheck()) {
               CommandManager.sendChatMessage("§c[!] §fBad key!");
            }

            return false;
         }

         if (rkey.equalsIgnoreCase("none")) {
            key = -1;
         }

         if (key == 0) {
            return false;
         } else {
            this.bindSetting.setKey(key);
            return true;
         }
      }
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

   public <T extends java.lang.Enum<T>> EnumSetting<T> add(EnumSetting<T> setting) {
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
      if (mc.method_1562() != null) {
         mc.method_1562().method_52787(packet);
      }
   }

   public final boolean isCategory(Module_JlagirAibYQgkHtbRnhw category) {
      return category == this.category;
   }

   public String getArrayName() {
      return this.getName() + this.getArrayInfo();
   }

   public String getArrayInfo() {
      return this.getInfo() == null ? "" : " §7[" + this.getInfo() + "§7]";
   }

   public String getInfo() {
      return null;
   }
}
