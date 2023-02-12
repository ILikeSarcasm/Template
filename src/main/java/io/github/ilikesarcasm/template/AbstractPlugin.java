package io.github.ilikesarcasm.template;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.ilikesarcasm.messages.LanguageManager;
import io.github.ilikesarcasm.messages.Message;
import io.github.ilikesarcasm.template.commands.CommandManager;
import io.github.ilikesarcasm.template.files.FileManager;
import io.github.ilikesarcasm.template.listeners.ListenerManager;
import io.github.ilikesarcasm.template.proxies.ProxyManager;

public abstract class AbstractPlugin extends JavaPlugin {

    private static final String CONFIG_FILE = "config.yml";
    private static final HashMap<String, String> CONFIG_PARAMS = new HashMap<String, String>() {{
        /**
         * Insert any config path
         * 
         * put(<name>, <path>);
         */
    }};

    private static AbstractPlugin instance;

    private File pluginFolder;
    private YamlConfiguration config;
    private LanguageManager languageManager;
    private ProxyManager proxyManager;
    private FileManager fileManager;
    private ListenerManager listenerManager;
    private CommandManager commandManager;

    public static AbstractPlugin getInstance() {
        return AbstractPlugin.instance;
    }

    @Override
    public void onEnable() {
        AbstractPlugin.instance = this;

        this.pluginFolder = this.getDataFolder();

        this.proxyManager = ProxyManager.getInstance();
        this.proxyManager.loadProxies(this);
        
        this.fileManager = FileManager.getInstance();
        this.fileManager.loadFiles();
        this.fileManager.createFileStructure();
        
        File configFile = new File(this.pluginFolder + File.separator + AbstractPlugin.CONFIG_FILE);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        this.languageManager = LanguageManager.getInstance();
        File languageFile = new File(this.fileManager.get("langFolder").getAbsolutePath() + File.separator + this.config.get(AbstractPlugin.CONFIG_PARAMS.get("languageFileName")));
        this.languageManager.loadLanguage(languageFile);
        Message.setLanguageManager(this.languageManager);

        this.listenerManager = ListenerManager.getInstance();
        this.listenerManager.loadListeners(this);

        this.commandManager = CommandManager.getInstance();
        this.commandManager.loadCommands();

        this.loadLogic(this);

        this.info("Initialized");
    }

    protected void loadLogic(AbstractPlugin plugin) {
        /**
         * Insert any logic loading
         */
    }

    @Override
    public void onDisable() {
        this.info("Stopped");
    }

    public File getPluginFolder() {
        return this.pluginFolder;
    }

    public void emit(Event e) {
        Bukkit.getPluginManager().callEvent(e);
    }

    /**
     * Sends the message associated to a key to a Player instance
     * @param target Player to send the message to
     * @param key    Key to get the message from
     * @param params The optional parameters to edit the message
     */
    public void message(Player target, String key, Object... params) {
        Message.fromKey(key, params).sendTo(target);
    }

    /**
     * Sends the message associated to a key event to a CommandSender instance
     * @param target CommandSender to send the message to
     * @param key    Key to get the message from
     * @param params The optional parameters to edit the message
     */
    public void message(CommandSender target, String key, Object... params) {
        Message.fromKey(key, params).sendTo(target);
    }

    /**
     * Print an information to the console
     * @param message The message to print
     */
    public void info(String message) {
        this.getLogger().info(message);
    }

    /**
     * Print a warning to the console
     * @param message The message to print
     */
    public void warn(String message) {
        this.getLogger().warning(message);
    }

    /**
     * Print an error to the console
     * @param message The message to print
     */
    public void error(String message) {
        this.getLogger().severe(message);
    }
    
}
