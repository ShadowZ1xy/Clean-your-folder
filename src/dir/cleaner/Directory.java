package dir.cleaner;

import dir.cleaner.data.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class Directory {
    public static String workingDirectoryPath;

    public final static String programDirectory = System.getProperty("user.dir");


    /**
     * get only files from directory (exclude folders)
     *
     * @return array of files
     */
    public static File[] getAllFiles() {
        File directory = new File(workingDirectoryPath);
        return directory.listFiles(File::isFile);
    }

    /**
     * @param ext String ext
     * @return path to the extension folder
     */
    static String getExtDirPath(String ext) {
        return workingDirectoryPath + "\\" + "0_" + ext + "_files";
    }

    /**
     * get files extensions for show them in gui extension table
     * @param files get'ed from Directory.getAllFiles()
     * @return list of extensions without duplicates, without Data.always"ignore/clean"List elements and files without extensions
     */
    public static ObservableList<Extension> getOnlyShowFilesExtensions(File[] files) {
        HashSet<Extension> hashSet = new HashSet<>();
        ObservableList<Extension> resultList = FXCollections.observableArrayList();

        Arrays.stream(files)
                .map(MyFile::getExtension)
                .filter((s) -> !s.getName().equals(" "))
                .filter(hashSet::add)
                .filter((s) -> !Data.alwaysIgnoreFileList.contains(s))
                .filter((s) -> !Data.alwaysCleanFileList.contains(s))
                .forEach(resultList::add);
        return resultList;
    }
}
