package moe.cowan.b.annsearcher.backend.Ids;

/**
 * Created by user on 26/07/2015.
 */
public class StringIdSetter {

    private StringIdKey stringIdKey;

    public StringIdSetter(StringIdKey stringIdKey) {
        this.stringIdKey = stringIdKey;
    }

    public void setString(Id id, String str) {
        id.addId(stringIdKey, str);
        checkAndSetInternalId(id, str);
    }

    private void checkAndSetInternalId(Id id, String str) {
        if (stringIdKey == StringIdKey.ANN)
            id.addId(StringIdKey.INTERNAL, str);
    }

}
