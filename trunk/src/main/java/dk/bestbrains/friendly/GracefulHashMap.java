package dk.bestbrains.friendly;

import java.util.HashMap;

public class GracefulHashMap extends HashMap {

    public GracefulHashMap() {
        super();
    }

    @Override
    public Object get(Object key) {
        Object value = super.get(key);

        if(value == null)
            return "";

        return value;
    }

}
