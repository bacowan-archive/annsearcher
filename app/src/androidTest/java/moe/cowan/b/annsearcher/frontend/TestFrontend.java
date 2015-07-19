package moe.cowan.b.annsearcher.frontend;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import moe.cowan.b.annsearcher.frontend.activities.TestLauncher;

/**
 * Created by user on 18/07/2015.
 */
public class TestFrontend extends ActivityInstrumentationTestCase2<TestLauncher> {

    public TestFrontend() throws Exception {
        super(TestLauncher.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
    }

    @MediumTest
    public void testManual() throws Throwable {
        getActivity();
    }

}
