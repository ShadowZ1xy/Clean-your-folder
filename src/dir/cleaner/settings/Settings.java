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
import java.util.Arrays;
import java.util.HashSet;

public class Settings {
    private static final String pathToSettings = Directory.programDirectory + "\\src\\dir\\resources\\data.json";
    private static final String cleanListKey = "cleanListKey";
    private static final String ignoreListKey = "ignoreListKey";
    private static final String pathKey = "pathKey";

    /**
     * save: cleanList, ignoreLeast and directory what need to be cleaned in data.json file
     */
    public static void save(HashSet<Extension> cleanList, HashSet<Extension> ignoreList) {
        JSONObject jsonObject = new JSONObject();
        writeList(cleanList, DataType.CLEAN_DATA, jsonObject);
        writeList(ignoreList, DataType.IGNORE_DATA, jsonObject);
        saveWorkingPath(jsonObject);
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
    @SuppressWarnings("unchecked") //adding this line because json-simple library uses old java version
    private static void writeList(HashSet<Extension> set, DataType type, JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();

        set.forEach((s) -> jsonArray.add(s.getName()));
        try {
            if (type == DataType.IGNORE_DATA) {
                jsonObject.put(ignoreListKey, jsonArray);
            } else {
                jsonObject.put(cleanListKey, jsonArray);
            }
            FileWriter fileWriter = new FileWriter(new File(pathToSettings));
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
    @SuppressWarnings("unchecked") //adding this line because json-simple library uses old java version
    private static HashSet<Extension> loadList(DataType type) {
        HashSet<Extension> resultHashSet = new HashSet<>();
        String key;
        if (type == DataType.IGNORE_DATA) {
            key = ignoreListKey;
        } else {
            key = cleanListKey;
        }
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(pathToSettings));
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
    @SuppressWarnings("unchecked") //adding this line because json-simple library uses old java version
    private static void saveWorkingPath(JSONObject jsonObject) {
        jsonObject.put(pathKey, Directory.workingDirectoryPath);
        try {
            FileWriter fileWriter = new FileWriter(new File(pathToSettings));
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
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(pathToSettings));
            Directory.workingDirectoryPath = (String) jsonObject.get(pathKey);
        } catch (Exception e) {
            System.out.println("Settings:loadWorkingPath exception");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
