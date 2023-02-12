package io.github.ilikesarcasm.template.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommandHub extends AbstractCommand {

    protected final Map<Integer, Map<String, AbstractCommand>> commands = new HashMap<>();

    private String defaultCommandName;

    public AbstractCommandHub(String name, String defaultCommandName) {
        super(name);
        this.defaultCommandName = defaultCommandName;
        this.loadCommands(new ArrayList<>());
    }

    protected void loadCommands(List<Map.Entry<Integer, AbstractCommand>> commandsToLoad) {
        for (Map.Entry<Integer, AbstractCommand> commandPair: commandsToLoad) {
            int subCommandPosition = commandPair.getKey();
            AbstractCommand command = commandPair.getValue();

            if (!this.commands.containsKey(subCommandPosition)) {
                this.commands.put(subCommandPosition, new HashMap<>());
            }

            this.commands.get(subCommandPosition).put(command.getName(), command);
        }
    }

    @Override
    public void execute(CommandSender sender, Map<String, Object> args) {}

    public void execute(CommandSender sender, String[] preArgs, String[] args) {
        for (Map.Entry<Integer, Map<String, AbstractCommand>> commandGroup: this.commands.entrySet()) {
            int subCommandPosition = commandGroup.getKey();
            Map<String, AbstractCommand> commands = commandGroup.getValue();

            Map<String, Object> processedArgs = this.processArgs(preArgs, args, subCommandPosition);
            String commandName = args.length > subCommandPosition ? 
                (String)processedArgs.get("name") : this.defaultCommandName;
            String[] commandPreArgs = (String[])processedArgs.get("pre");
            String[] commandPostArgs = (String[])processedArgs.get("post");
            String[] commandArgs = (String[])processedArgs.get("all");
            
            if (commands.containsKey(commandName)) {
                AbstractCommand command = commands.get(commandName);

                if (!sender.hasPermission(command.getPermission())) {
                    this.plugin.message(sender, "cmd.missing-permission");
                    return;
                }

                if (command.isPlayerOnly() && !(sender instanceof Player)) {
                    this.plugin.message(sender, "cmd.player-only");
                    return;
                }
        
                if (!command.canExecute(commandName, commandArgs)) {
                    String messageKey = command.getHelp(sender);
                    this.plugin.message(sender, messageKey);
                    return;
                }
                
                if (command instanceof AbstractCommandHub) {
                    ((AbstractCommandHub)command).execute(sender, commandPreArgs, commandPostArgs);
                } else if (command.verify(sender, commandArgs)) {
                    Map<String, Object> parsedArgs = command.parseArgs(sender, commandArgs);
                    command.execute(sender, parsedArgs);
                }

                return;
            }
        }
        
        this.plugin.message(sender, "cmd.does-not-exist");
    }
    
    public TreeSet<String> getCompleters(CommandSender sender, String[] preArgs, String[] args) {
        if(!sender.hasPermission("houseupgrade.tabComplete")) {
            return new TreeSet<>();
        }

        TreeSet<String> completers = new TreeSet<>();
        for (Map.Entry<Integer, Map<String, AbstractCommand>> commandGroup: this.commands.entrySet()) {
            int subCommandPosition = commandGroup.getKey();

            Map<String, Object> processedArgs = this.processArgs(preArgs, args, subCommandPosition);
            String commandName = (String)processedArgs.get("name");
            String[] commandPreArgs = (String[])processedArgs.get("pre");
            String[] commandPostArgs = (String[])processedArgs.get("post");
            String[] commandArgs = (String[])processedArgs.get("all");

            if (args.length - 1 < subCommandPosition) {
                completers.addAll(this.getFilteredCompleters(sender, commandArgs));
            } else if (args.length - 1 == subCommandPosition) {
                completers.addAll(this.getCommandNameCompleters(sender, commandName, subCommandPosition));
            } else {
                TreeSet<String> commandParamCompleters = this.getCommandParamsCompleters(sender, commandName, commandPreArgs, commandPostArgs, commandArgs, subCommandPosition);
                if (!commandParamCompleters.isEmpty()) {
                    completers = commandParamCompleters;
                    break;
                }
            }
        }
        
        return completers;
    }

    private Map<String, Object> processArgs(String[] preArgs, String[] args, int subCommandPosition) {
        final String name = args.length > subCommandPosition ?
            args[subCommandPosition].toLowerCase() : "";

        final String[] pre;
        final String[] post;
        if (args.length > subCommandPosition) {
            pre = Arrays.copyOf(preArgs, preArgs.length + subCommandPosition);
            System.arraycopy(args, 0, pre, preArgs.length, subCommandPosition);
            if (args.length > subCommandPosition + 1 /* subCommand.nameTotalWords */) {
                post = Arrays.copyOfRange(args, subCommandPosition + 1 /* subCommand.nameTotalWords */, args.length);
            } else {
                post = new String[] {};
            }
        } else {
            pre = Arrays.copyOf(preArgs, preArgs.length + args.length);
            System.arraycopy(args, 0, pre, preArgs.length, args.length);
            post = new String[] {};
        }
        
        final String[] all = Arrays.copyOf(pre, pre.length + post.length);
        System.arraycopy(post, 0, all, pre.length, post.length);

        return new HashMap<String, Object>() {{
            put("name", name);
            put("pre", pre);
            put("post", post);
            put("all", all);
        }};
    }

    private TreeSet<String> getCommandNameCompleters(CommandSender sender, String commandName, int subCommandPosition) {
        return this.commands.get(subCommandPosition)
                            .values().stream()
                            .filter(command -> sender.hasPermission(command.getPermission()) && 
                                                command.getName().startsWith(commandName))
                            .map(AbstractCommand::getName)
                            .collect(Collectors.toCollection(TreeSet::new));
    }

    private TreeSet<String> getCommandParamsCompleters(CommandSender sender, String commandName, String[] commandPreArgs, String[] commandPostArgs, String[] commandArgs, int subCommandPosition) {
        TreeSet<String> completers = new TreeSet<>();

        Iterator<AbstractCommand> it = this.commands.get(subCommandPosition).values().iterator();
        while (it.hasNext()) {
            AbstractCommand command = it.next();
            if(sender.hasPermission(command.getPermission()) && command.canExecute(commandName, commandArgs)) {
                if (command instanceof AbstractCommandHub) {
                    completers = ((AbstractCommandHub)command).getCompleters(sender, commandPreArgs, commandPostArgs);
                } else {
                    completers = command.getFilteredCompleters(sender, commandArgs);
                }
                break;
            }
        }

        return completers;
    }

}
