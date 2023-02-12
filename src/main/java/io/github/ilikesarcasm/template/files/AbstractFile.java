package io.github.ilikesarcasm.template.files;

import io.github.ilikesarcasm.template.AbstractPlugin;

import java.io.File;

public abstract class AbstractFile extends File {

    protected final AbstractPlugin plugin;

    private static String getFileName(String name) {
        return AbstractPlugin.getInstance().getPluginFolder() + File.separator + name;
    }

    public AbstractFile(String name) {
        super(AbstractFile.getFileName(name));
        this.plugin = AbstractPlugin.getInstance();
    }
    
}
