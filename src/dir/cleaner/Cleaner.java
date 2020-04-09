package dir.cleaner;

import dir.cleaner.data.Data;
import javafx.scene.control.TableView;

import java.io.File;

public class Cleaner {

    /**
     * get files from current work directory (Directory.workingDirectoryPath)
     * get extensions from gui Table, add list of always clean extensions (from Data.alwaysCleanFileList)
     * filter files with extensions what's need to cleaned
     * clean all them with putting there appropriate directory's
     *
     * @param extensionTableView get from Controller class
     * @return true if all okay, false if directory is empty
     */
    @SuppressWarnings("all") //for remove folderToMove.mkdir() "result is ignored" warning
    public static boolean cleanDirectory(TableView<Extension> extensionTableView) {
        File[] files;
        try {
            files = Directory.getAllFiles();
        } catch (NullPointerException e) {
            System.out.println("Your folder already clear :)");
            return false;
        }
        for (File file : files) {
            Extension fileExtension;
            fileExtension = MyFile.getExtension(file);
            if (fileExtension.getName().equals(" ")) {
                continue;
            }
            if (Data.collectExtClearList(extensionTableView).contains(fileExtension)) {
                String moveToDirPath = Directory.getExtDirPath(fileExtension.getName());
                File folderToMove = new File(moveToDirPath);
                folderToMove.mkdir();
                MyFile.moveTo(file, moveToDirPath);
            }
        }
        return true;
    }
}
