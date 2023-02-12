package io.github.ilikesarcasm.template.proxies;

import io.github.ilikesarcasm.template.AbstractPlugin;

public abstract class AbstractProxy {

    private final AbstractPlugin plugin;
    
    private String error;

    public AbstractProxy(AbstractPlugin plugin, String error) {
        this.plugin = plugin;
        this.error = error;
    }
    
    public boolean load() {
        return true;
    }

    public String getError() {
        return this.error;
    }
    
}
