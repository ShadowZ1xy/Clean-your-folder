package dir.gui;

import dir.cleaner.Cleaner;
import dir.cleaner.data.Data;
import dir.cleaner.data.DataType;
import dir.cleaner.settings.Settings;
import dir.cleaner.util.Directory;
import dir.cleaner.util.Extension;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    private static boolean allCheckBoxClicked;
    private ObservableList<Extension> extensionsData = FXCollections.observableArrayList();
    private ObservableList<String> alwaysIgnoreList = FXCollections.observableArrayList();
    private ObservableList<String> alwaysCleanList = FXCollections.observableArrayList();

    @FXML
    private TableView<Extension> extensionsCheckboxTable;

    @FXML
    private TableColumn<Extension, String> extensionCol;

    @FXML
    private TableColumn<Extension, Boolean> checkboxCol;

    @FXML
    private CheckBox selectAllCheckbox;


    @FXML
    private Button aiAdd;
    @FXML
    private Button aiDelete;

    @FXML
    private Button acAdd;
    @FXML
    private Button acDelete;


    @FXML
    private TextField aiTextField;
    @FXML
    private TextField acTextField;

    @FXML
    private ListView<String> aiExtList;
    @FXML
    private ListView<String> acExtList;


    @FXML
    private Button browseButton;

    @FXML
    private Button cleanButton;

    @FXML
    private TextField pathTextField;

    @FXML
    private AnchorPane mainField;

    /**
     * For extension table all checkbox select/deselect via one checkbox
     */
    @FXML
    public void allCheckBoxHandler() {
        allCheckBoxClicked = selectAllCheckbox.isSelected();
        for (Extension ext : extensionsData) {
            ext.setCheck(allCheckBoxClicked);
        }
    }

    @FXML
    public void cleanButtonHandler() {
        Cleaner.cleanDirectory(extensionsCheckboxTable);
    }

    /**
     * browse directory with explorer folder choose option
     * save that to Directory.workingDirectoryPath
     * update extensions data and save them into data.json file
     */
    @FXML
    public void browseButtonHandler() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) mainField.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            String dirAbsolutePath = file.getAbsolutePath();
            pathTextField.setText(dirAbsolutePath);
            Directory.workingDirectoryPath = dirAbsolutePath;
            Update.updateExtensionTableInGui(extensionsData, extensionsCheckboxTable);
            Settings.save(Data.getAlwaysCleanFileList(), Data.getAlwaysIgnoreFileList());
        }
    }


    @FXML
    public void closeApplication() {
        Settings.save(Data.getAlwaysCleanFileList(), Data.getAlwaysIgnoreFileList());
        System.exit(0);
    }

    /**
     * load data from data.json file
     * setup them into Data and Directory classes
     */
    @FXML
    void initialize() {
        Settings.loadWorkingPath();
        Settings.load(DataType.CLEAN_DATA);
        Settings.load(DataType.IGNORE_DATA);

        if (Directory.workingDirectoryPath != null && !Directory.workingDirectoryPath.equals("")) {
            Update.updateExtensionTableInGui(extensionsData, extensionsCheckboxTable);
        }
        pathTextField.setText(Directory.workingDirectoryPath);

        if (Data.getAlwaysIgnoreFileList().size() > 0) {
            Data.getAlwaysIgnoreFileList().stream()
                    .map(Extension::getName)
                    .forEach((s) -> alwaysIgnoreList.add(s));
        }

        if (Data.getAlwaysCleanFileList().size() > 0) {
            Data.getAlwaysCleanFileList().stream()
                    .map(Extension::getName)
                    .forEach((s) -> alwaysCleanList.add(s));
        }

        extensionCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkboxCol.setCellValueFactory(new PropertyValueFactory<>("check"));
        selectAllCheckbox.setSelected(true);
        extensionsCheckboxTable.setPlaceholder(new Label(" "));

        aiExtList.setItems(alwaysIgnoreList);
        acExtList.setItems(alwaysCleanList);
    }

    /**
     * always ignore list "add" button handler
     */
    @FXML
    void aiAddHandler() {
        String text = aiTextField.getText();
        if (text.length() != 0 && !aiExtList.getItems().contains(text)) {
            aiExtList.getItems().add(text);
            Data.getAlwaysIgnoreFileList().add(new Extension(text));
            Update.updateExtensionTableInGui(extensionsData, extensionsCheckboxTable);
            Settings.save(Data.getAlwaysCleanFileList(), Data.getAlwaysIgnoreFileList());
        }
    }

    /**
     * always clean list "add" button handler
     */
    @FXML
    void acAddHandler() {
        String text = acTextField.getText();
        if (text.length() != 0 && !acExtList.getItems().contains(text)) {
            acExtList.getItems().add(text);
            Data.getAlwaysCleanFileList().add(new Extension(text));
            Update.updateExtensionTableInGui(extensionsData, extensionsCheckboxTable);
            Settings.save(Data.getAlwaysCleanFileList(), Data.getAlwaysIgnoreFileList());
        }
    }

    /**
     * always ignore list "delete" button handler
     */
    @FXML
    void aiDeleteHandler() {
        if (aiExtList.getSelectionModel().getSelectedItem() != null) {
            String selected = aiExtList.getSelectionModel().getSelectedItem();
            Data.getAlwaysIgnoreFileList().remove(new Extension(selected));
            aiExtList.getItems().remove(selected);
            Update.updateExtensionTableInGui(extensionsData, extensionsCheckboxTable);
            Settings.save(Data.getAlwaysCleanFileList(), Data.getAlwaysIgnoreFileList());
        }
    }

    /**
     * always clean list "delete" button handler
     */
    @FXML
    void acDeleteHandler() {
        if (acExtList.getSelectionModel().getSelectedItem() != null) {
            String selected = acExtList.getSelectionModel().getSelectedItem();
            Data.getAlwaysCleanFileList().remove(new Extension(selected));
            acExtList.getItems().remove(selected);
            Update.updateExtensionTableInGui(extensionsData, extensionsCheckboxTable);
            Settings.save(Data.getAlwaysCleanFileList(), Data.getAlwaysIgnoreFileList());
        }
    }
}
