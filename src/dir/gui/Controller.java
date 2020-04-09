package dir.gui;

import dir.cleaner.Cleaner;
import dir.cleaner.Directory;
import dir.cleaner.Extension;
import dir.cleaner.data.Data;
import dir.cleaner.data.DataType;
import dir.cleaner.settings.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;

public class Controller {
    private static boolean allCheckBoxClicked;
    ObservableList<Extension> extensionsData = FXCollections.observableArrayList();
    ObservableList<String> alwaysIgnoreList = FXCollections.observableArrayList();
    ObservableList<String> alwaysCleanList = FXCollections.observableArrayList();

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

    @FXML
    public void browseButtonHandler() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) mainField.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            String dirAbsolutePath = file.getAbsolutePath();
            pathTextField.setText(dirAbsolutePath);
            Directory.workingDirectoryPath = dirAbsolutePath;
            Update.optionalExtensionList(extensionsData, extensionsCheckboxTable);
            Settings.save();
        }
    }

    @FXML
    public void closeApplication() {
        Settings.save();
        System.exit(0);
    }

    @FXML
    void initialize() {
        Settings.loadWorkingPath();
        Settings.load(DataType.CLEAN_DATA);
        Settings.load(DataType.IGNORE_DATA);

        Update.optionalExtensionList(extensionsData, extensionsCheckboxTable);

        pathTextField.setText(Directory.workingDirectoryPath);

        if (Data.alwaysIgnoreFileList.size() > 0) {
            Data.alwaysIgnoreFileList.stream()
                    .map(Extension::getName)
                    .forEach((s) -> alwaysIgnoreList.add(s));
        }

        if (Data.alwaysCleanFileList.size() > 0) {
            Data.alwaysCleanFileList.stream()
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

    @FXML
    void aiAddHandler() {
        String text = aiTextField.getText();
        if (text.length() != 0 && !aiExtList.getItems().contains(text)) {
            aiExtList.getItems().add(text);
            Data.alwaysIgnoreFileList.add(new Extension(text));
            Update.optionalExtensionList(extensionsData, extensionsCheckboxTable);
            Settings.save();
        }
    }

    @FXML
    void acAddHandler() {
        String text = acTextField.getText();
        if (text.length() != 0 && !acExtList.getItems().contains(text)) {
            acExtList.getItems().add(text);
            Data.alwaysCleanFileList.add(new Extension(text));
            Update.optionalExtensionList(extensionsData, extensionsCheckboxTable);
            Settings.save();
        }
    }

    @FXML
    void aiDeleteHandler() {
        if(aiExtList.getSelectionModel().getSelectedItem() != null) {
            String selected = aiExtList.getSelectionModel().getSelectedItem();
            Data.alwaysIgnoreFileList.remove(new Extension(selected));
            aiExtList.getItems().remove(selected);
            Update.optionalExtensionList(extensionsData, extensionsCheckboxTable);
            Settings.save();
        }
    }

    @FXML
    void acDeleteHandler() {
        if(acExtList.getSelectionModel().getSelectedItem() != null) {
            String selected = acExtList.getSelectionModel().getSelectedItem();
            Data.alwaysCleanFileList.remove(new Extension(selected));
            acExtList.getItems().remove(selected);
            Update.optionalExtensionList(extensionsData, extensionsCheckboxTable);
            Settings.save();
        }
    }
}
