package dir.cleaner.settings;

import dir.cleaner.data.Data;
import dir.cleaner.data.DataType;
import dir.cleaner.util.Directory;
import dir.cleaner.util.Extension;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class Settings {
    private static final String CLEAN_YOUR_FOLDER = "Clean your folder";
    private static final String DATA_FOLDER_NAME = "data";
    private static final String DATA_FILE_NAME = "data";
    private static final Extension DATA_FILE_EXTENSION = new Extension("json");

    private static final String PATH_TO_SETTINGS_FOLDER;
    private static final String PATH_TO_DATA_DIR;
    private static final String PATH_TO_DATA_FILE;

    private static final String CLEAN_LIST_KEY = "cleanListKey";
    private static final String IGNORE_LIST_KEY = "ignoreListKey";
    private static final String PATH_KEY = "pathKey";

    private static File dataFile;

    static {
        PATH_TO_SETTINGS_FOLDER = System.getenv("AppData") + "\\" + CLEAN_YOUR_FOLDER;
        PATH_TO_DATA_DIR = PATH_TO_SETTINGS_FOLDER + "\\" + DATA_FOLDER_NAME;
        PATH_TO_DATA_FILE = PATH_TO_DATA_DIR + "\\" + DATA_FILE_NAME + "." + DATA_FILE_EXTENSION.getName();

        File dir = new File(PATH_TO_SETTINGS_FOLDER);
        File midDir = new File(PATH_TO_DATA_DIR);
        File file = new File(PATH_TO_DATA_FILE);
        if (!dir.exists() || !midDir.exists() || !file.exists()) {
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    System.out.println("Roaming/CYF folder dont created");
                }
            }
            if (!midDir.exists()) {
                if (!midDir.mkdir()) {
                    System.out.println("Roaming/CYF/MID folder dont created");
                }
            }
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        System.out.println("Roaming/CYF/MID/data.json file dont created");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            save(new HashSet<>(), new HashSet<>(), "");
        }
        dataFile = file;
    }


    /**
     * save: cleanList, ignoreLeast and directory what need to be cleaned in data.json file
     */
    public static void save(HashSet<Extension> cleanList, HashSet<Extension> ignoreList, String path) {
        JSONObject jsonObject = new JSONObject();
        writeList(cleanList, DataType.CLEAN_DATA, jsonObject);
        writeList(ignoreList, DataType.IGNORE_DATA, jsonObject);
        saveWorkingPath(jsonObject, path);
    }

    /**
     * load list from data.json file and place it in appropriate Data class cell
     *
     * @param type data type what need to be load
     */
    public static void load(DataType type) {
        switch (type) {
            case IGNORE_DATA:
                Data.setAlwaysIgnoreFileList(loadList(DataType.IGNORE_DATA));
                break;
            case CLEAN_DATA:
                Data.setAlwaysCleanFileList(loadList(DataType.CLEAN_DATA));
                break;
        }
    }

    /**
     * write HashSet<Extension> from Data class to data.json file
     *
     * @param set        HashSet from Data class
     * @param type       what identificate key to save in json
     * @param jsonObject object gained outside for using in Settings.save() function (common for two calls for save)
     */
    @SuppressWarnings("unchecked") //adding this line because json-simple library use old java version
    private static void writeList(HashSet<Extension> set, DataType type, JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();

        set.forEach((s) -> jsonArray.add(s.getName()));
        try {
            if (type == DataType.IGNORE_DATA) {
                jsonObject.put(IGNORE_LIST_KEY, jsonArray);
            } else {
                jsonObject.put(CLEAN_LIST_KEY, jsonArray);
            }
            FileWriter fileWriter = new FileWriter(dataFile);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Settings:writeList() exception");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * load list from data.json and put it to Data class storage
     *
     * @param type of list to load
     * @return set of extensions
     */
    @SuppressWarnings("unchecked") //adding this line because json-simple library use old java version
    private static HashSet<Extension> loadList(DataType type) {
        HashSet<Extension> resultHashSet = new HashSet<>();
        String key;
        if (type == DataType.IGNORE_DATA) {
            key = IGNORE_LIST_KEY;
        } else {
            key = CLEAN_LIST_KEY;
        }
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(dataFile));
            JSONArray jsonArray = (JSONArray) jsonObject.get(key);
            jsonArray.forEach((s) -> resultHashSet.add(new Extension((String) s)));
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return resultHashSet;
    }

    /**
     * save path to data.json file for load it after program restart
     *
     * @param jsonObject common for save functions calls, see - Settings.save() function
     */
    @SuppressWarnings("unchecked") //adding this line because json-simple library use old java version
    private static void saveWorkingPath(JSONObject jsonObject, String path) {
        jsonObject.put(PATH_KEY, path);
        try {
            FileWriter fileWriter = new FileWriter(dataFile);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Settings:saveWorkingPath exception");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * load working path from data.json and put it in Directory.workingDirectoryPath string
     */
    public static void loadWorkingPath() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(dataFile));
            Object obj = jsonObject.get(PATH_KEY);
            if (obj == null) {
                Directory.workingDirectoryPath = "";
            } else {
                Directory.workingDirectoryPath = (String) obj;
            }
        } catch (Exception e) {
            System.out.println("Settings:loadWorkingPath exception");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
