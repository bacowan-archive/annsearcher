package moe.cowan.b.annsearcher.backend.database;

import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 03/08/2015.
 */
public class AuthenticatedRequest extends StringRequest {

    private String username;
    private String password;

    public AuthenticatedRequest(String username, String password, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        this.username = username;
        this.password = password;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put(
                "Authorization",
                String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", username, password).getBytes(), Base64.DEFAULT)));
        return params;
    }

}
