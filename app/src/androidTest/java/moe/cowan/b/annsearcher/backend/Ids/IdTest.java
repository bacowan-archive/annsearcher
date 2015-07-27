package moe.cowan.b.annsearcher.backend.Ids;

import junit.framework.TestCase;

/**
 * Created by user on 26/07/2015.
 */
public class IdTest extends TestCase {

    public void testConstructorSetsInternalId() {
        String stringId = "123";
        Id id = new Id(stringId);
        assertEquals(stringId, id.getId(StringIdKey.INTERNAL));
    }

    public void testInternalIdStartsAsBlank() {
        Id id1 = new Id();

        assertEquals(id1.getId(StringIdKey.INTERNAL), "");
    }

    public void testEmptyInternalStringsNotEqual() {
        Id id1 = createId(StringIdKey.INTERNAL, "");
        Id id2 = createId(StringIdKey.INTERNAL, "");

        assertTrue(!id1.equals(id2));
    }

    public void testEqualsComparesInternalIds() {
        String idString = "1233";
        Id id1 = createId(StringIdKey.INTERNAL, idString);
        Id id2 = createId(StringIdKey.INTERNAL, idString);

        assertEquals(id1, id2);
    }

    public void testEqualsDoesNotCompareOtherIds() {
        String idString = "1233";
        Id id1 = createId(StringIdKey.ANN, idString);
        Id id2 = createId(StringIdKey.ANN, idString);

        assertTrue(!id1.equals(id2));
    }

    private Id createId(StringIdKey stringIdKey, String stringId) {
        Id id = new Id();
        id.addId(stringIdKey, stringId);
        return id;
    }

}
