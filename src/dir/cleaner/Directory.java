package dir.cleaner;

import dir.cleaner.data.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Directory {
    public static String workingDirectoryPath;

    public final static String programDirectory = System.getProperty("user.dir");

    private static File file;

    public Directory(String absolutePathToDirectory) {
        file = new File(absolutePathToDirectory);
        if (workingDirectoryPath == null) {
            workingDirectoryPath = absolutePathToDirectory;
        }
    }


    public static File[] getAllFiles() {
        File directory = new File(workingDirectoryPath);
        return directory.listFiles(File::isFile);
    }

    /**
     * @param ext String ext
     * @return path to the extension folder
     */
    public static String getExtDirPath(String ext) {
        return workingDirectoryPath + "\\" + "0_" + ext + "_files";
    }

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

    /*public static List<File> getOnlyNeedFiles(File[] files) {

    }

    private static HashSet<Extension> getOnlyNeedExtensions(String workingPath) {
        HashSet<Extension> set = new HashSet<>();

    }*/
}
