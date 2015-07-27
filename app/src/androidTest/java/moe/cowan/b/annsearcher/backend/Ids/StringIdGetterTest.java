package moe.cowan.b.annsearcher.backend.Ids;

import junit.framework.TestCase;

/**
 * Created by user on 26/07/2015.
 */
public class StringIdGetterTest extends TestCase {

    private StringIdGetter getter;

    public void setUp() {
        getter = new StringIdGetter(StringIdKey.ANN);
    }

    public void testSetId() {
        Id id = new Id();
        String expectedStringId = "12345";
        id.addId(StringIdKey.ANN, expectedStringId);

        String actualStringId = getter.getStringId(id);

        assertEquals(expectedStringId, actualStringId);
    }

}
