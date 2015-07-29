package moe.cowan.b.annsearcher.BackendFuntionalTest;

import android.test.AndroidTestCase;

import moe.cowan.b.annsearcher.backend.database.BasicRequestGetter;

/**
 * Created by user on 28/07/2015.
 */
public class BasicRequestGetterTest extends AndroidTestCase {

    private BasicRequestGetter rg;

    public void setUp() {
        rg = new BasicRequestGetter(getContext());
    }

    public void testWithDebugger() throws Exception {
        long unixTime = System.currentTimeMillis();
        String output = rg.getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime=4658");
        long timespan = System.currentTimeMillis() - unixTime;

    }

}
