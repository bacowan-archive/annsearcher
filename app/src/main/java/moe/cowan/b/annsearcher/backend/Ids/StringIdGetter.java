package moe.cowan.b.annsearcher.backend.Ids;

/**
 * Created by user on 26/07/2015.
 */
public class StringIdGetter {

    private StringIdKey stringIdKey;

    public StringIdGetter(StringIdKey stringIdKey) {
        this.stringIdKey = stringIdKey;
    }

    public String getStringId(Id id) {
        return id.getString(stringIdKey);
    }

}
