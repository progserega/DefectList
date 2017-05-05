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

        // внутренняя sqlite-база:
        SqliteStorage sqliteStorage = SqliteStorage.getInstance(appContext);
        if (sqliteStorage==null)
        {
            Log.e("syncDicts()", "sqliteStorage.getInstance() error");
        }
        // очищаем базу:
        assert sqliteStorage != null;
        sqliteStorage.clear_db();

        String url ="http://prx.rs.int/sp.json";
        syncSpFromUrl(mRequestQueue,url);
        url ="http://prx.rs.int/res.json";
        syncResFromUrl(mRequestQueue,url);
        url ="http://prx.rs.int/ps.json";
        syncPsFromUrl(mRequestQueue,url);

    }

    private boolean syncSpFromUrl(RequestQueue rq, String url)
    {
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Do something with the response

                    if(!updateSpFromJson(response))
                    {
                        Log.e("syncSpFromUrl()","updateSpFromJson()");
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
        rq.add(stringRequest);
        return true;
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
            Log.e("updatePsFromJson()", "sqliteStorage.getInstance() error");
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
                    Log.e("updateSpFromJson():", "sqliteStorage.add_sp("+sp_id+","+sp_name);
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean syncResFromUrl(RequestQueue rq, String url)
    {
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response

                        if(!updateResFromJson(response))
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
        rq.add(stringRequest);
        return true;
    }
    private boolean updateResFromJson(String response)
    {
        // parse json:
        JSONArray sp = null;
        JSONObject item = null;
        //JSONArray contacts = null;
        JSONObject contacts = null;
        SqliteStorage sqliteStorage = SqliteStorage.getInstance(appContext);
        if (sqliteStorage==null)
        {
            Log.e("updateResFromJson()", "sqliteStorage.getInstance() error");
            return false;
        }
        try {
            sp = new JSONObject(response).getJSONArray("res");
            // get a JSONArray from inside an object
            for (int index=0; index < sp.length(); index++)
            {
                int sp_id=0;
                int res_id=0;
                String res_name=null;
                JSONObject sp_item = sp.getJSONObject(index);
                res_name = sp_item.getString("name");
                sp_id = sp_item.getInt("sp_id");
                res_id = sp_item.getInt("res_id");
                Log.d("res data from json:", "sp_id="+sp_id+", res_id=" + res_id + " sp_name="+res_name);
                if(!sqliteStorage.add_res(sp_id,res_id,res_name))
                {
                    Log.e("updateResFromJson():", "sqliteStorage.add_res("+sp_id+","+res_id+","+res_name);
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean syncPsFromUrl(RequestQueue rq, String url)
    {
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response

                        if(!updatePsFromJson(response))
                        {
                            Log.e("syncPsFromUrl()","updateSpFromJson()");
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
        rq.add(stringRequest);
        return true;
    }
    private boolean updatePsFromJson(String response)
    {
        // parse json:
        JSONArray sp = null;
        JSONObject item = null;
        //JSONArray contacts = null;
        JSONObject contacts = null;
        SqliteStorage sqliteStorage = SqliteStorage.getInstance(appContext);
        if (sqliteStorage==null)
        {
            Log.e("updatePsFromJson()", "sqliteStorage.getInstance() error");
            return false;
        }
        try {
            sp = new JSONObject(response).getJSONArray("ps");
            // get a JSONArray from inside an object
            for (int index=0; index < sp.length(); index++)
            {
                int sp_id=0;
                int res_id=0;
                int ps_id=0;
                int uniq_id=0;
                int np_id=0;
                String name=null;
                JSONObject sp_item = sp.getJSONObject(index);
                name = sp_item.getString("name");
                sp_id = sp_item.getInt("sp_id");
                res_id = sp_item.getInt("res_id");
                ps_id = sp_item.getInt("ps_id");
                uniq_id = sp_item.getInt("ps_uniq_id");
                Log.d("ps data from json:", "sp_id="+sp_id+", res_id=" + res_id + ", uniq_id="+uniq_id+", ps_name="+name);
                if(!sqliteStorage.add_ps(sp_id,res_id,ps_id,np_id,uniq_id,name))
                {
                    Log.e("updatePsFromJson:", "sqliteStorage.add_res("+sp_id+","+res_id+","+name);
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}
