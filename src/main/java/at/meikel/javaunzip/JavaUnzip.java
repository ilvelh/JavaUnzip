package at.meikel.javaunzip;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JavaUnzip {

    public static void unzip(File file, File targetDir, boolean dropTarget) {
        if (targetDir.exists()) {
            if (dropTarget) {
                try {
                    FileUtils.deleteDirectory(targetDir);
                } catch (IOException e) {
                    System.err.println("Import failed, unzip dir '" + targetDir.getAbsolutePath() + "' already exists, drop-target option ist set, but directory can't be deleted.");
                    return;
                }
            } else {
                System.err.println("Import failed, unzip dir '" + targetDir.getAbsolutePath() + "' already exists.");
                return;
            }
        }

        if (!targetDir.mkdirs()) {
            System.err.println("Import failed, unzip dir '" + targetDir.getAbsolutePath() + "' can't be created.");
            return;
        }

        try {
            FileInputStream stream = new FileInputStream(file);
            ZipInputStream zis = new ZipInputStream(stream);

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                File spec = new File(entry.getName());
                String entryPath = spec.getParent();
                String entryName = spec.getName();
                File targetPath = new File(targetDir, entryPath);
                File target = new File(targetPath, entryName);
                if (! targetPath.exists()) {
                    targetPath.mkdirs();
                }
                System.out.println("Unzipping " + entryName + " to " + target.getAbsolutePath());
                FileOutputStream fout = new FileOutputStream(target);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fout.write(c);
                }
                zis.closeEntry();
                fout.close();
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

}
