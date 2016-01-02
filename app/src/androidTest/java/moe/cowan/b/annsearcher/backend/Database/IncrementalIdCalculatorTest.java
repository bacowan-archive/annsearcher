package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import moe.cowan.b.annsearcher.backend.Callback;
import moe.cowan.b.annsearcher.backend.Ids.IncrementalIdCalculator;

/**
 * Created by KDCowan on 12/22/2015.
 */
public class IncrementalIdCalculatorTest extends InstrumentationTestCase {

    private IncrementalIdCalculator calculator;
    private static final int lastId = 1;
    private static final int nextId = 2;
    private static final Callback<Integer> lastIdCallback = new Callback<Integer>() {
        @Override
        public Integer call() {
            return lastId;
        }
    };

    public void setUp() throws Exception {
        calculator = new IncrementalIdCalculator(lastIdCallback);
    }

    public void test_GetLargestInternalId() {
        assertEquals(Integer.toString(nextId), calculator.calculateNewId());
    }

}
