package at.meikel.javaunzip;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JavaUnzip {

    private File sourceFile;
    private File targetDir;
    private boolean dropTarget;
    private boolean updateTarget;
    private boolean omitTimestamps;
    private Long useTimestamp;
    private long startTimestamp;

    public JavaUnzip() {
        startTimestamp = System.currentTimeMillis();
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(File targetDir) {
        this.targetDir = targetDir;
    }

    public boolean isDropTarget() {
        return dropTarget;
    }

    public void setDropTarget(boolean dropTarget) {
        this.dropTarget = dropTarget;
    }

    public boolean isUpdateTarget() {
        return updateTarget;
    }

    public void setUpdateTarget(boolean updateTarget) {
        this.updateTarget = updateTarget;
    }

    public boolean isOmitTimestamps() {
        return omitTimestamps;
    }

    public void setOmitTimestamps(boolean omitTimestamps) {
        this.omitTimestamps = omitTimestamps;
    }

    public void setUseTimestamp(Long useTimestamp) {
        this.useTimestamp = useTimestamp;
    }

    public Long getUseTimestamp() {
        return useTimestamp;
    }

    public void unzip() {
        if (targetDir.exists()) {
            if (dropTarget) {
                try {
                    FileUtils.deleteDirectory(targetDir);
                } catch (IOException e) {
                    System.err.println("Import failed, unzip dir '" + targetDir.getAbsolutePath() + "' already exists, drop-target option ist set, but directory can't be deleted.");
                    return;
                }
            } else if (!updateTarget) {
                System.err.println("Import failed, unzip dir '" + targetDir.getAbsolutePath() + "' already exists.");
                return;
            }
        }

        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println("Import failed, unzip dir '" + targetDir.getAbsolutePath() + "' can't be created.");
                return;
            }
        }

        try {
            FileInputStream stream = new FileInputStream(sourceFile);
            ZipInputStream zis = new ZipInputStream(stream);

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File spec = new File(entry.getName());
                String entryPath = spec.getParent();
                String entryName = spec.getName();
                File targetDir = new File(this.targetDir, entryPath == null ? "" : entryPath);
                File targetFile = new File(targetDir, entryName);
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                if (entry.isDirectory()) {
                    targetDir.setLastModified(getTimestamp(entry));
                    continue;
                }

                // if ((!targetFile.exists()) || (targetFile.lastModified() < entry.getTime())) {
                    System.out.println("Unzipping " + targetFile.getAbsolutePath());
                    FileOutputStream fout = new FileOutputStream(targetFile);
                    for (int c = zis.read(); c != -1; c = zis.read()) {
                        fout.write(c);
                    }
                    zis.closeEntry();
                    fout.close();
                    if (!omitTimestamps) {
                        targetFile.setLastModified(getTimestamp(entry));
                    }
                // } else {
                //     System.out.println("Ignoring " + targetFile.getAbsolutePath());
                // }
            }
            zis.close();
        } catch (FileNotFoundException e) {
            System.err.println("Exception: " + e.toString());
            e.printStackTrace();
            return;
        } catch (IOException e) {
            System.err.println("Exception: " + e.toString());
            e.printStackTrace();
            return;
        }
    }

    private long getTimestamp(ZipEntry entry) {
        if (omitTimestamps) {
            return startTimestamp;
        }

        if (useTimestamp != null) {
            return useTimestamp.longValue();
        }

        return entry.getTime();
    }

}
