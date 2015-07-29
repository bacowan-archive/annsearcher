package moe.cowan.b.annsearcher.backend.database;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by user on 27/07/2015.
 */
public class BasicRequestGetter implements RequestGetter {

    private int REQUEST_TIMEOUT = 10;

    private Context context;

    public BasicRequestGetter(Context context) {
        this.context = context;
    }

    @Override
    public String getRequestByUrl(String url) throws ExecutionException, InterruptedException, TimeoutException {
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.GET,  url, future, future);

        RequestQueue queue = Volley.newRequestQueue(context);

        queue.add(request);

        return future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
    }

}
