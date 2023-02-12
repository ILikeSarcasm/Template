package io.github.ilikesarcasm.template.files;

import io.github.ilikesarcasm.template.AbstractPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;

public abstract class AbstractJarFile extends AbstractFile {

    protected String jarPath;

    private static void copyFileFromJar(String jarPath, File dest) {
        try {
            FileUtils.copyToFile(AbstractPlugin.getInstance().getResource(jarPath), dest);
        } catch (Exception e) {
            AbstractPlugin.getInstance().error("Couldn't copy file " + jarPath + " to file " + dest.getAbsolutePath());
        }
    }

    private static void copyFolderFromJar(String jarPath, File dest) {

        File srcFolder;
        try {
            srcFolder = new File(AbstractPlugin.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            AbstractPlugin.getInstance().error("Couldn't copy folder " + jarPath + " to folder " + dest.getAbsolutePath());
            return;
        }

        ZipFile srcFolderAsZip;
        try {
            srcFolderAsZip = new ZipFile(srcFolder);
        } catch (IOException e) {
            AbstractPlugin.getInstance().error("Couldn't copy folder " + jarPath + " to folder " + dest.getAbsolutePath());
            return;
        }

        Enumeration<? extends ZipEntry> entries = srcFolderAsZip.entries();
        
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if(!entry.isDirectory() && entryName.startsWith(jarPath + "/")) {
                String entryFileName = entryName.substring(entryName.lastIndexOf("/"));
                File entryDestFile = new File(dest.getAbsolutePath() + File.separator + entryFileName);
                AbstractJarFile.copyFileFromJar(entry.getName(), entryDestFile);
            }
        }

        try {
            srcFolderAsZip.close();
        } catch (IOException e) {
            AbstractPlugin.getInstance().error("Couldn't copy folder " + jarPath + " to folder " + dest.getAbsolutePath());
            return;
        }
    }

    public AbstractJarFile(String filename) {
        super(filename);
        this.jarPath = filename;
    }

    public AbstractJarFile(String filename, String jarPath) {
        super(filename);
        this.jarPath = jarPath;
    }

    public String getJarPath() {
        return this.jarPath;
    }

    public void copyFromJar() {
        if (this.getName().contains(".")) {
            AbstractJarFile.copyFileFromJar(this.jarPath, this);
        } else {
            AbstractJarFile.copyFolderFromJar(this.jarPath, this);
        }
    }

}