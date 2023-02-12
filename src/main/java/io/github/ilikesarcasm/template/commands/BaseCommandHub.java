package io.github.ilikesarcasm.template.commands;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

public class BaseCommandHub extends AbstractCommandHub {

    private static final String NAME = "houseupgrade";
    private static final String DEFAULT_COMMAND_NAME = "menu";

    public BaseCommandHub() {
        super(BaseCommandHub.NAME, BaseCommandHub.DEFAULT_COMMAND_NAME);
    }

    @Override
    protected void loadCommands(List<Map.Entry<Integer, AbstractCommand>> x) {
        List<Map.Entry<Integer, AbstractCommand>> commandsToLoad = new ArrayList<Map.Entry<Integer, AbstractCommand>>() {{
            /**
             * Insert any AbstractCommand or AbstractCommandHub
             *
             * add(new AbstractMap.SimpleEntry<Integer, AbstractCommand>(<startIndex>, <abstractCommandHub>));
             * add(new AbstractMap.SimpleEntry<Integer, AbstractCommand>(<startIndex>, <abstractCommand>));
             */
        }};

        super.loadCommands(commandsToLoad);
    }

    @Override
    public TreeSet<String> getCompleters(CommandSender sender, String[] args) {
        return this.getCompleters(sender, new String[] {}, args);
    }

}
