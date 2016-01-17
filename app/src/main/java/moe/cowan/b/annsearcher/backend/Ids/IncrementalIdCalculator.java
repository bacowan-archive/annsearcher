package moe.cowan.b.annsearcher.backend.Ids;

import moe.cowan.b.annsearcher.backend.Callback;

/**
 * Created by KDCowan on 12/22/2015.
 */
public class IncrementalIdCalculator implements NewIdCalculator {

    private Callback<Integer> callback;

    public IncrementalIdCalculator(Callback<Integer> getLastIdCallback) {
        this.callback = getLastIdCallback;
    }

    public String calculateNewId() {
        return Integer.toString(getLargestInternalId()+1);
    }

    private int getLargestInternalId() {
        return callback.call();
    }

}
