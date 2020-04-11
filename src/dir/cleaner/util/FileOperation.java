package dir.cleaner.util;

import java.io.File;
import java.util.Optional;

public class FileOperation {
    /**
     * get all extensions from String path directory
     * if file dont have extension return: optional[empty]
     *
     * @param filename path to the directory
     * @return optional packed extension from gained file
     */
    private static Optional<String> getOptionalExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }


    /**
     * @param file only file, not directory
     * @return Extension if file have, Extension with space name if file dont have extension
     */
    public static Extension getExtension(File file) {
        String fileName = file.getAbsolutePath();
        Optional obj = getOptionalExtension(fileName);
        if (obj.isPresent()) {
            return new Extension((String) obj.get());
        } else {
            return new Extension(" ");
        }
    }

    /**
     * move file to the other directory
     *
     * @param file file what need to move
     * @param dir  String path to the dir
     * @return true if all ok and file is moved, and false if something go wrong (mainly directory security problem)
     */
    public static boolean moveTo(File file, String dir) {
        return file.renameTo(new File(dir + "\\" + file.getName()));
    }


}
