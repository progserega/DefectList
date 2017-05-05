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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        String sp_url ="http://prx.rs.int/sp.json";

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, sp_url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                Log.d("json",response);/// Инициализация приложения:
                // внутренняя sqlite-база:
                SqliteStorage sqliteStorage = SqliteStorage.getInstance(appContext);
                if (sqliteStorage==null)
                {
                    Log.e("apiJsonSync.syncDicts()", "sqliteStorage.getInstance() error");
                }
                // очищаем базу:
                sqliteStorage.clear_db();

                if(!updateSpFromJson(response))
                {
                    Log.e("apiJsonSync.syncDicts()","updateSpFromJson()");
                    // TODO отобразить ошибку пользователю
                }
                // СП обновили успешно, пробуем обновить РЭС-ы:
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
    private boolean updateSpFromJson(String response)
    {
        // parse json:
        JSONArray sp = null;
        JSONObject item = null;
        //JSONArray contacts = null;
        JSONObject contacts = null;
        SqliteStorage sqliteStorage = SqliteStorage.getInstance(appContext);
        if (sqliteStorage==null)
        {
            Log.e("apiJsonSync.syncDicts()", "sqliteStorage.getInstance() error");
            return false;
        }
        try {
            sp = new JSONObject(response).getJSONArray("sp");
            // get a JSONArray from inside an object
            for (int index=0; index < sp.length(); index++)
            {
                int sp_id=0;
                String sp_name=null;
                JSONObject sp_item = sp.getJSONObject(index);
                sp_name = sp_item.getString("name");
                sp_id = sp_item.getInt("sp_id");
                Log.d("sp data from json:", "sp_id="+sp_id+" sp_name="+sp_name);
                if(!sqliteStorage.add_sp(sp_id,sp_name))
                {
                    Log.e("SyncDicts:", "sqliteStorage.add_sp("+sp_id+","+sp_name);
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}
