package dir.cleaner.data;

import dir.cleaner.Extension;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.HashSet;

public class Data {
    public static HashSet<Extension> alwaysIgnoreFileList = new HashSet<>();
    public static HashSet<Extension> alwaysCleanFileList = new HashSet<>();

    public static HashSet<Extension> collectExtClearList(TableView<Extension> table) {
        HashSet<Extension> resultList = new HashSet<>();
        table.getItems().stream()
                .filter((s) -> s.getCheck().isSelected())
                .forEach(resultList::add);
        resultList.addAll(alwaysCleanFileList);
        return resultList;
    }
}
