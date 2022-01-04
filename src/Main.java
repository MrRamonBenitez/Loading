import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {

        openZip("D:/Games/savegames/save.zip", "D:/Games/savegames");

        GameProgress gameProgress = openProgress("D:/games/savegames/save3.dat");

        System.out.println(gameProgress.toString());

    }

    private static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

    private static void openZip(String path, String dir) {
        ZipInputStream zip = null;
        try {
            zip = new ZipInputStream(new FileInputStream(path));
            ZipEntry entry;
            String name;
            while ((entry = zip.getNextEntry()) != null) {
                name = entry.getName();
                File file = new File(dir, name);
                byte[] buffer = new byte[BUFFER_SIZE];
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                int count;
                while ((count = zip.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                zip.closeEntry();
                out.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
