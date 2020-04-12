package dir.cleaner.util;

import dir.cleaner.data.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class Directory {
    public static String workingDirectoryPath;

    public final static String programDirectory = System.getProperty("user.dir");

    private static final String dirThirdKeyWord = "files";

    /**
     * get only files from directory (exclude folders)
     *
     * @return array of files
     */
    public static File[] getAllFiles() {
        File directory = new File(workingDirectoryPath);
        return directory.listFiles(File::isFile);
    }

    public static Hashtable<Extension, File> getAllExtDirectories() {
        File directory = new File(workingDirectoryPath);
        File[] files = directory.listFiles(File::isDirectory);
        Hashtable<Extension, File> resultList = new Hashtable<>();
        if (files != null) {
            ArrayList<File> list = (ArrayList<File>) Arrays.stream(files)
                    .filter((s) -> {
                        String[] temp = s.getName().split("_");
                        return !(temp.length != 3 || !temp[2].equals(dirThirdKeyWord));
                    }).collect(Collectors.toList());
            list.forEach((s) -> resultList.put(new Extension(s.getName().split("_")[1]), s));
            return resultList;
        } else {
            return new Hashtable<>();
        }
    }

    @SuppressWarnings("all") //for remove dir.mkdir() result is ignored warning
    public static File getExtDirPath(Extension ext) {
        Hashtable<Extension, File> list = getAllExtDirectories();
        File dir;
        if (list.containsKey(ext)) {
            dir = list.get(ext);
        } else {
            dir = new File(workingDirectoryPath + "\\" + "0_" + ext + "_" + dirThirdKeyWord);
            dir.mkdir();
        }
        return dir;
    }


    /**
     * get files extensions for show them in gui extension table
     *
     * @param files get'ed from Directory.getAllFiles()
     * @return list of extensions without duplicates, without Data.always"ignore/clean"List elements and files without extensions
     */
    public static ObservableList<Extension> getOnlyShowFilesExtensions(File[] files) {
        HashSet<Extension> hashSet = new HashSet<>();
        ObservableList<Extension> resultList = FXCollections.observableArrayList();
        if (files != null) {
            Arrays.stream(files)
                    .map(FileOperation::getExtension)
                    .filter((s) -> !s.getName().equals(" "))
                    .filter(hashSet::add)
                    .filter((s) -> !Data.getAlwaysIgnoreFileList().contains(s))
                    .filter((s) -> !Data.getAlwaysCleanFileList().contains(s))
                    .forEach(resultList::add);
        }
        return resultList;
    }
}
