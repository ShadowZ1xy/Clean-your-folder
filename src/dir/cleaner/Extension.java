package dir.cleaner;

import javafx.scene.control.CheckBox;

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


    /**
     * override hashcode for correct work in HashSet
     *
     * @return hashcode based on Extension.name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * override equals for correct work in HashSet
     * @param obj for comparison with this class
     * @return true if objects are identical, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Extension) {
            return this.getName().equals(obj.toString());
        } else {
            throw new IllegalArgumentException("Wrong object, object need to be Extension type.");
        }
    }
}
