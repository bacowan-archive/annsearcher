package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by user on 27/07/2015.
 */
public interface RequestGetter {

    String getRequestByUrl(String url) throws ExecutionException, InterruptedException, TimeoutException;
    void setContext(Context context);

}
