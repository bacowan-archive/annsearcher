package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by user on 03/08/2015.
 */
public class AuthenticatedRequestGetter {

    private int REQUEST_TIMEOUT = 10;

    private Context context;

    public AuthenticatedRequestGetter(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getRequestByUrl(String url, String username, String password) throws ExecutionException, InterruptedException, TimeoutException {
        RequestFuture<String> future = RequestFuture.newFuture();
        AuthenticatedRequest request = new AuthenticatedRequest(username, password, Request.Method.GET, url, future, future);

        RequestQueue queue = Volley.newRequestQueue(context);

        queue.add(request);

        return future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
    }
}
