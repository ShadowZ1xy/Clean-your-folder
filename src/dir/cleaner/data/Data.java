package dir.cleaner.data;

import dir.cleaner.util.Extension;
import javafx.scene.control.TableView;

import java.util.HashSet;

public class Data {
    private static HashSet<Extension> alwaysIgnoreFileList = new HashSet<>();
    private static HashSet<Extension> alwaysCleanFileList = new HashSet<>();

    /**
     * Collect list of checked extensions from gui table, in that list add "alwaysClean" list from data
     *
     * @param table table from ui where user check/uncheck extensions what's need to be cleaned on folder
     * @return set of extensions what need to be cleaned
     */
    public static HashSet<Extension> collectExtClearList(TableView<Extension> table) {
        HashSet<Extension> resultList = new HashSet<>();
        table.getItems().stream()
                .filter((s) -> s.getCheck().isSelected())
                .forEach(resultList::add);
        resultList.addAll(alwaysCleanFileList);
        return resultList;
    }

    public static HashSet<Extension> getAlwaysIgnoreFileList() {
        return alwaysIgnoreFileList;
    }

    public static void setAlwaysIgnoreFileList(HashSet<Extension> alwaysIgnoreFileList) {
        Data.alwaysIgnoreFileList = alwaysIgnoreFileList;
    }

    public static HashSet<Extension> getAlwaysCleanFileList() {
        return alwaysCleanFileList;
    }

    public static void setAlwaysCleanFileList(HashSet<Extension> alwaysCleanFileList) {
        Data.alwaysCleanFileList = alwaysCleanFileList;
    }
}
