package dir.cleaner.settings;

import dir.cleaner.Directory;
import dir.cleaner.Extension;
import dir.cleaner.data.Data;
import dir.cleaner.data.DataType;
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

    public static void save() {
        JSONObject jsonObject = new JSONObject();
        writeList(Data.alwaysCleanFileList, DataType.CLEAN_DATA, jsonObject);
        writeList(Data.alwaysIgnoreFileList, DataType.IGNORE_DATA, jsonObject);
        saveWorkingPath(jsonObject);
    }

    public static void load(DataType type) {
        switch (type) {
            case IGNORE_DATA:
                Data.alwaysIgnoreFileList = loadList(DataType.IGNORE_DATA);
                break;
            case CLEAN_DATA:
                Data.alwaysCleanFileList = loadList(DataType.CLEAN_DATA);
                break;
        }
    }

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
