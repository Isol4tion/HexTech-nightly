package me.hextech.mod.commands;

import me.hextech.api.managers.CommandManager;
import me.hextech.api.utils.Wrapper;

import java.util.List;
import java.util.Objects;

public abstract class Command
implements Wrapper {
    protected final String name;
    protected final String description;
    protected final String syntax;

    public Command(String name, String description, String syntax) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.syntax = Objects.requireNonNull(syntax);
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
        CommandManager.sendChatMessage("\u00a7b[!] \u00a7fUsage: \u00a7e" + this.getName() + " " + this.getSyntax());
    }
}
