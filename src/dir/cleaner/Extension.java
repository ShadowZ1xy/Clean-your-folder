package dir.cleaner;

import javafx.scene.control.CheckBox;

import java.util.HashSet;
import java.util.Iterator;

public class Extension {
    private CheckBox check;
    private String name;

    public CheckBox getCheck() {
        return check;
    }

    public void setCheck(Boolean x) {
        if (x) {
            check.setSelected(true);
        } else {
            check.setSelected(false);
        }
    }

    public String getName() {
        return name;
    }

    public Extension(String name) {
        check = new CheckBox();
        check.setSelected(true);
        this.name = name;
    }

    public static String extListToString(HashSet<Extension> list) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static HashSet<Extension> stringToExtList(String str) {
        String[] array = str.split(", ");
        HashSet<Extension> extList = new HashSet<>();
        for (String s : array) {
            extList.add(new Extension(s));
        }
        return extList;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Extension) {
            return this.getName().equals(obj.toString());
        } else {
            throw new IllegalArgumentException("Wrong object, object need to be Extension type.");
        }
    }
}
