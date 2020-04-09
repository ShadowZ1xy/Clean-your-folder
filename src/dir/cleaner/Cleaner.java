package dir.cleaner;

import dir.cleaner.data.Data;
import dir.gui.Controller;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public class Cleaner {
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

    public static HashSet<Extension> getCleanExtList(ObservableList<Extension> list) {
        HashSet<Extension> set = new HashSet<>();
        list.stream()
                .filter((s) -> s.getCheck().isSelected())
                .forEach(set::add);
        set.addAll(Data.alwaysCleanFileList);
        return set;
    }
}
