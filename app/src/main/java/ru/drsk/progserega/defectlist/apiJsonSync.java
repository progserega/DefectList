package ru.drsk.progserega.defectlist;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by serega on 03.05.17.
 */

public class apiJsonSync {
    Context appContext;
    RequestQueue mRequestQueue;

    public apiJsonSync(Context context) {
        appContext=context;
    }

    public void syncDicts()
    {

        // Instantiate the cache
        Cache cache = new DiskBasedCache(appContext.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        String url ="http://prx.rs.int/test.json";

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                Log.d("json",response);
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                    Log.e("json", String.valueOf(error));
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }
}
