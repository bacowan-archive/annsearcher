package moe.cowan.b.annsearcher.backend.database;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by user on 27/07/2015.
 */
public interface RequestGetter {

    String getRequestByUrl(String url) throws ExecutionException, InterruptedException, TimeoutException;

}
