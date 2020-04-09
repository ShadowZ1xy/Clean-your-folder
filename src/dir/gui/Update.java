package dir.gui;

import dir.cleaner.Directory;
import dir.cleaner.Extension;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

class Update {
    /**
     * update "Extensions" list in gui
     *
     * @param extensionsData gained from Controller class
     * @param table          gained from Controller class
     */
    static void optionalExtensionList(ObservableList<Extension> extensionsData, TableView<Extension> table) {
        extensionsData.setAll(Directory.getOnlyShowFilesExtensions(Directory.getAllFiles()));
        table.setItems(extensionsData);
    }
}
