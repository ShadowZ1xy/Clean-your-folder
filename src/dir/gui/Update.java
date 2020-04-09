package dir.gui;

import dir.cleaner.Directory;
import dir.cleaner.Extension;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class Update {
    static void optionalExtensionList(ObservableList<Extension> extensionsData, TableView<Extension> table) {
        extensionsData.setAll(Directory.getOnlyShowFilesExtensions(Directory.getAllFiles()));
        table.setItems(extensionsData);
    }
}
