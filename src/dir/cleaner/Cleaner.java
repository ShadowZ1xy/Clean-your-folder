package dir.cleaner;

import dir.cleaner.data.Data;
import dir.cleaner.util.Directory;
import dir.cleaner.util.Extension;
import dir.cleaner.util.FileOperation;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.io.File;
import java.util.HashMap;

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
        HashMap<Extension, Integer> logger = new HashMap<>();
        File[] files;
        try {
            files = Directory.getAllFiles();
        } catch (NullPointerException e) {
            System.out.println("Your folder already clear :)");
            return false;
        }
        for (File file : files) {
            Extension fileExtension;
            fileExtension = FileOperation.getExtension(file);
            if (fileExtension.getName().equals(" ")) {
                continue;
            }
            if (Data.collectExtClearList(extensionTableView).contains(fileExtension)) {
                if (logger.containsKey(fileExtension)) {
                    logger.put(fileExtension, logger.get(fileExtension) + 1);
                } else {
                    logger.put(fileExtension, 1);
                }
                String moveToDirPath = Directory.getExtDirPath(fileExtension).getAbsolutePath();
                FileOperation.moveTo(file, moveToDirPath);
            }
        }
        Directory.getAllExtDirectories().forEach((e, f) -> {
            String currentAbsolutePath = f.getAbsolutePath();
            String parentFolder = f.getParent();
            String[] tempArr = f.getName().split("_");
            int count = f.list().length;
            String moveToDir = parentFolder + "\\" + count + "_" + tempArr[1] + "_" + tempArr[2];
            f.renameTo(new File(moveToDir));
        });
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cleaned files list.");
        alert.setHeaderText(null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cleaned files: \n \n");
        logger.forEach((e, i) -> stringBuilder.append(e.getName().toUpperCase() + ": " + i + "\n"));
        alert.setContentText(stringBuilder.toString());
        alert.showAndWait();
        return true;
    }
}
