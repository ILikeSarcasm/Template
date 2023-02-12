package io.github.ilikesarcasm.template.commands;

import io.github.ilikesarcasm.template.AbstractPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private static final String BASE_COMMAND = "houseupgrade";

    private static CommandManager instance;

    private final AbstractPlugin plugin;

    private AbstractCommandHub baseCommandHub;

    public static CommandManager getInstance() {
        if (CommandManager.instance == null) {
            CommandManager.instance = new CommandManager();
        }

        return CommandManager.instance;
    }

    private CommandManager() {
        this.plugin = AbstractPlugin.getInstance();
        this.baseCommandHub = new BaseCommandHub();
    }

    public void loadCommands() {
        PluginCommand command = this.plugin.getServer().getPluginCommand(CommandManager.BASE_COMMAND);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        this.baseCommandHub.execute(sender, new String[] {}, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(this.baseCommandHub.getFilteredCompleters(sender, args));
    }

}
