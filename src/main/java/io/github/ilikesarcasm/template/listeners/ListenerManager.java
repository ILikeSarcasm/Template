package io.github.ilikesarcasm.template.listeners;

import io.github.ilikesarcasm.template.AbstractPlugin;

import org.bukkit.Bukkit;

public class ListenerManager {

    private static ListenerManager instance;

    public static ListenerManager getInstance() {
        if (ListenerManager.instance == null) {
            ListenerManager.instance = new ListenerManager();
        }

        return ListenerManager.instance;
    }

    public void loadListeners(AbstractPlugin plugin) {
        /**
         * Insert any Listener
         * 
         * Bukkit.getPluginManager().registerEvents(<listener>, AbstractPlugin.getInstance());
         */
    }

}
