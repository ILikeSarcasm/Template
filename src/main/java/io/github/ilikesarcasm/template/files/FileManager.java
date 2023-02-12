package io.github.ilikesarcasm.template.files;

import io.github.ilikesarcasm.template.AbstractPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FileManager {

    private static FileManager instance;

    private final AbstractPlugin plugin;
    private final HashMap<String, File> files;

    public static FileManager getInstance() {
        if (FileManager.instance == null) {
            FileManager.instance = new FileManager();
        }

        return FileManager.instance;
    }

    private FileManager() {
        this.plugin = AbstractPlugin.getInstance();
        this.files = new HashMap<String, File>() {{
            /**
             * Insert any AbstractFile
             * 
             * put(<name>, <abstractFile>);
             */
        }};
    }

    public void loadFiles() {
        /**
         * Load any AbstractFile
         */
    }

    public void createFileStructure() {
        for (File file: this.files.values()) {
            if (!file.exists()) {
                if (!(file instanceof AbstractJarFile)) {
                    try {
                        if (file.isFile() && !file.createNewFile()) {
                            this.plugin.error("Couldn't create file: " + file.getAbsolutePath());
                        } else if (file.isDirectory() && !file.mkdirs()) {
                            this.plugin.error("Couldn't create folder: " + file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        this.plugin.error(e.getMessage());
                    }
                } else {
                    ((AbstractJarFile)file).copyFromJar();
                }
            }
        }
    }

    public File get(String key) {
        return this.files.get(key);
    }

}
