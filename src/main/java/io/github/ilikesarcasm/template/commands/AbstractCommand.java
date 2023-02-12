package io.github.ilikesarcasm.template.commands;

import io.github.ilikesarcasm.template.AbstractPlugin;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class AbstractCommand {

    // The AbstractPlugin plugin instance
    protected final AbstractPlugin plugin;

    // The name of the command
    protected String name;
    // The number of word in the command name
    protected int nameTotalWords;
    // The permission needed for the command execution
    protected String permission;
    // Truth value whether or not the command is only usable by an actual player
    protected boolean playerOnly;
    // Minimum number of argument required
    protected int minArgNumber;

    /**
     * Constructor.
     * @param name The name of the command
     */
    public AbstractCommand(String name) {
        this.plugin = AbstractPlugin.getInstance();
        this.name = name;
        this.nameTotalWords = name.split(" ").length;
        this.playerOnly = false;
        this.minArgNumber = 0;
    }

    /**
     * Get the argument that comes after the base command that this command reacts to.
     * @return The string that should be in front of the command for this class to act
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the number of word that compose the command name.
     * @return The number of word in the command name
     */
    public int getNameTotalWords() {
        return this.nameTotalWords;
    }

    /**
     * Returns the default permission needed for executing this command.
     * @return The permission as a string or null if no permission are required
     */
    public String getPermission() {
        return this.permission;
    }
    
    /**
     * Sets the permission needed to execute the command.
     * @param permission The permission needed to execute the command
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Returns a truth value whether or not the command is only usable by an actual player.
     * @return The only-player usability truth value
     */
    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    /**
     * Sets the truth value for only-player usability.
     * @param playerOnly The only-player usability truth value
     */
    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    /**
     * Returns the minimum number of argument for the command to be executable
     * @return The minimum number of argument
     */
    public int getMinArgNumber() {
        return this.minArgNumber;
    }

    /**
     * Sets the minimum number of argument for the command to be executable
     * @param minArgNumber The minimum number of argument
     */
    public void setMinArgNumber(int minArgNumber) {
        this.minArgNumber = minArgNumber;
    }

    /**
     * Check if the given command and arguments can be executed by this instance.
     * @param commandName The name of the command to check for execution
     * @param args        The arguments to check
     * @return A truth value whether or not the instance can execute the command
     */
    public boolean canExecute(String commandName, String[] args) {
        return commandName.toLowerCase().startsWith(this.getName().toLowerCase());
    }

    /**
     * Verify if the command is valid or not.
     * @param sender The entity that exectued the command
     * @param args   The raw parameters 
     * @return A truth value whether or not the command is valid
     */
    public boolean verify(CommandSender sender, String[] args) {
        if (args.length < this.minArgNumber) {
            this.plugin.message(sender, "cmd.too-few-arguments", this.name);
            return false;
        }

        return true;
    }

    /**
     * Parses the command parameters.
     * @param sender The entity that exectued the command
     * @param args   The raw parameters
     * @return The parsed command parameters
     */
    public Map<String, Object> parseArgs(CommandSender sender, String[] args) {
        return new HashMap<>();
    }

    /**
     * Execute a (sub)command if the conditions are met.
     * @param sender The entity that executed the command
     * @param args   The command arguments
     */
    public abstract void execute(CommandSender sender, Map<String, Object> args);

    /**
     * Get a list of string to complete a command with
     * @param sender     The entity that wants to tab complete
     * @param args       The already given start of the command
     * @return A set of completers for the command
     */
    public Collection<String> getCompleters(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    /**
     * Get a list of filtered and ordered command completers
     * @param sender     The entity that wants to tab complete
     * @param args       The already given start of the command
     * @return A filtered and ordered set of completers for the command
     */
    public TreeSet<String> getFilteredCompleters(CommandSender sender, String[] args) {
        String toCompleteArg = args[args.length -1];

        return this.getCompleters(sender, args).stream()
                                               .filter(completer -> completer.startsWith(toCompleteArg))
                                               .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Returns the correct help string key to be used on the help page.
     * @param target The command caller
     * @return The help message key according to the permissions of the receiver
     */
    public String getHelp(CommandSender target) {
        return "help.no-help";
    }

}
