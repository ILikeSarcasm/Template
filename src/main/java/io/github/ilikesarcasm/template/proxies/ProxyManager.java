package io.github.ilikesarcasm.template.proxies;

import java.util.HashSet;
import java.util.Set;

import io.github.ilikesarcasm.template.AbstractPlugin;

public class ProxyManager {

    private static ProxyManager instance;

    public static ProxyManager getInstance() {
        if (ProxyManager.instance == null) {
            ProxyManager.instance = new ProxyManager();
        }

        return ProxyManager.instance;
    }

    public boolean loadProxies(AbstractPlugin plugin) {
        Set<AbstractProxy> proxies = new HashSet<AbstractProxy>() {{
            /**
             * Insert any AbstractProxy
             * 
             * add(<abstractProxy>)
             */
        }};

        for (AbstractProxy proxy: proxies) {
            if (!proxy.load()) {
                plugin.error(proxy.getError());
                return false;
            }
        }

        return true;
    } 
    
}
