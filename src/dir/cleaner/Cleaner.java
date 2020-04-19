package dir.cleaner;

import dir.cleaner.data.Data;
import dir.cleaner.util.Directory;
import dir.cleaner.util.Extension;
import dir.cleaner.util.FileOperation;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class Cleaner {

    /**
     * get files from current work directory (Directory.workingDirectoryPath)
     * get extensions from gui Table, add list of always clean extensions (from Data.alwaysCleanFileList)
     * filter files with extensions what's need to cleaned
     * clean all them with putting there appropriate directory's
     * rename directories first part of name depending how many files are in it
     *
     * @param extensionTableView get from Controller class
     * @return true if all okay, false if directory is empty
     */
    @SuppressWarnings("all") //for remove renameTo "result is ignored" warning
    public static boolean cleanDirectory(TableView<Extension> extensionTableView) {
        HashMap<Extension, Integer> logStatistic = new HashMap<>();
        File[] files;
        try {
            files = Directory.getAllFiles();
        } catch (NullPointerException e) {
            return false;
        }
        for (File file : files) {
            Extension fileExtension;
            fileExtension = FileOperation.getExtension(file);
            if (fileExtension.getName().equals(" ")) {
                continue;
            }
            if (Data.collectExtClearList(extensionTableView).contains(fileExtension)) {
                //regionstart logger region
                if (logStatistic.containsKey(fileExtension)) {
                    logStatistic.put(fileExtension, logStatistic.get(fileExtension) + 1);
                } else {
                    logStatistic.put(fileExtension, 1);
                }
                //endregion
                String moveToDirPath = Directory.getExtDirPath(fileExtension).getAbsolutePath();
                FileOperation.moveTo(file, moveToDirPath);
            }
        }
        renameDirectories();
        showCleanerStatistic(logStatistic);
        return true;
    }

    /**
     * shows how many files and what types have been cleaned
     *
     * @param statistic list of extensions and number of files with corresponding extensions that have been cleaned
     */
    private static void showCleanerStatistic(HashMap<Extension, Integer> statistic) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cleaned files list.");
        alert.setHeaderText(null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cleaned files: \n \n");
        statistic.forEach((e, i) -> stringBuilder.append(e.getName()
                .toUpperCase())
                .append(": ")
                .append(i)
                .append("\n"));
        alert.setContentText(stringBuilder.toString());
        alert.showAndWait();
    }

    /**
     * renames directories according to how many files it contains
     * in the format (count_extension_"files")
     */
    private static void renameDirectories() {
        Directory.getAllExtDirectories().forEach((e, f) -> {
            String parentFolder = f.getParent();
            String[] tempArr = f.getName().split("_");
            int count = Objects.requireNonNull(f.list()).length;
            String moveToDir = parentFolder + "\\" + count + "_" + tempArr[1] + "_" + tempArr[2];
            if (!f.renameTo(new File(moveToDir))) {
                System.out.println("directory cant be renamed");
                System.out.println("problem is on: " + e.getName() + " extension");
            }
        });
    }
}
