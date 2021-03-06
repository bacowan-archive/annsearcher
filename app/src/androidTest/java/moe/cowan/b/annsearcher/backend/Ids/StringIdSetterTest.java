package moe.cowan.b.annsearcher.backend.Ids;

import junit.framework.TestCase;

/**
 * Created by user on 26/07/2015.
 */
public class StringIdSetterTest extends TestCase {

    public void testSetId() {
        StringIdSetter setter = new StringIdSetter(StringIdKey.MAL);
        Id id = new Id();
        String stringId = "12345";

        setter.setString(id, stringId);

        assertEquals(stringId, id.getString(StringIdKey.MAL));
    }

}
