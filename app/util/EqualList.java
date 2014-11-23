package util;

import java.util.ArrayList;

/**
 * Created by florian on 23/11/14.
 */
public class EqualList<T> extends ArrayList<T> {

    public boolean add(T item) {
        if (!this.contains(item)) {
            super.add(item);
            return true;
        }
        return false;
    }


}
