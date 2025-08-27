package me.hextech.remapped;

import java.util.List;
import java.util.Objects;

public abstract class Command implements Wrapper {
   protected final String name;
   protected final String description;
   protected final String syntax;

   public Command(String name, String description, String syntax) {
      this.name = (String)Objects.requireNonNull(name);
      this.description = (String)Objects.requireNonNull(description);
      this.syntax = (String)Objects.requireNonNull(syntax);
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public String getSyntax() {
      return this.syntax;
   }

   public abstract void runCommand(String[] var1) throws Throwable;

   public abstract String[] getAutocorrect(int var1, List<String> var2);

   public void sendUsage() {
      CommandManager.sendChatMessage("§b[!] §fUsage: §e" + this.getName() + " " + this.getSyntax());
   }
}
