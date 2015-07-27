package moe.cowan.b.annsearcher.backend.Ids;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 04/07/2015.
 */
public class Id implements Serializable {

    private Map<StringIdKey, String> ids = new HashMap<>();

    public Id(String internalId) {
        addId(StringIdKey.INTERNAL, internalId);
    }

    public Id() {
        this("");
    }

    public void addId(StringIdKey key, String id) {
        ids.put(key,id);
    }

    public String getId(StringIdKey key) {
        return ids.get(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Id))
            return false;
        if (ids.get(StringIdKey.INTERNAL).equals("") || ((Id)obj).getId(StringIdKey.INTERNAL).equals(""))
            return false;
        return ids.get(StringIdKey.INTERNAL).equals(((Id)obj).getId(StringIdKey.INTERNAL));
    }

    @Override
    public String toString() {
        return getId(StringIdKey.INTERNAL);
    }

}
