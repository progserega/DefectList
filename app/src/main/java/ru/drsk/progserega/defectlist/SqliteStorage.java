package ru.drsk.progserega.defectlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serega on 31.01.17.
 */

public class SqliteStorage {

    private Context context = null;
    private StationDbHelper dbHelper = null;
    private SQLiteDatabase db;

    public SqliteStorage(Context applicationcontext) {
        context = applicationcontext;
        dbHelper = new StationDbHelper(context);
    }
    public List<String> getAllSp()
    {
        Cursor cur;
        List<Long> ids = new ArrayList<Long>();
        List<String> sp = new ArrayList<String>();

        try
        {
            cur = db.query("sp_tbl", new String[]{"id", "name"},null,null,null,null,null);
            if (cur.getCount()==0)
            {
                Log.e("getAllSp()","select return 0 records!");
                cur.close();
                return null;
            }
            else
            {
                while(cur.moveToNext()) {
                    long id=cur.getLong(cur.getColumnIndexOrThrow("id"));
                    String name=cur.getString(cur.getColumnIndexOrThrow("name"));
                    Log.i("getAllSp()", "get SP from db: id=" + id + " name=" + name);
                    ids.add(id);
                    sp.add(name);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return sp;
    }
    public int getSpIdbyName(String sp_name)
    {
        Cursor cur;
        int sp_id = -1;
        String selection = "name = ?";
        String[] selectionArgs = {sp_name};

        try
        {
            cur = db.query("sp_tbl", new String[]{"id"},selection,selectionArgs,null,null,null);
            if (cur.getCount()==0) {
                Log.e("getSpIdbyName()", "select return 0 records!");
                cur.close();
                return -1;
            }
            else
            {
                while(cur.moveToNext()) {
                    sp_id=cur.getInt(cur.getColumnIndexOrThrow("id"));
                    Log.d("getSpIdbyName()", "get SP id from db: " + sp_id);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        return sp_id;
    }
    public int getResIdbyName(int sp_id, String res_name)
    {
        Cursor cur;
        int res_id = -1;
        String selection = "sp_id = ? and name = ?";
        String[] selectionArgs = {String.valueOf(sp_id), res_name};

        try
        {
            cur = db.query("res_tbl", new String[]{"id"},selection,selectionArgs,null,null,null);
            if (cur.getCount()==0) {
                Log.e("getResIdbyName()", "select return 0 records!");
                cur.close();
                return -1;
            }
            else
            {
                while(cur.moveToNext()) {
                    res_id=cur.getInt(cur.getColumnIndexOrThrow("id"));
                    Log.d("getResIdbyName()", "get Res id from db: " + res_id);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        return res_id;
    }

    public List<String> getAllStationByResName(String sp_name, String res_name)
    {
        int sp_id=getSpIdbyName(sp_name);
        if (sp_id == -1)
        {
            Log.e("getAllStationByResName()", "error getSpIdbyName()");
            return null;
        }
        int res_id=getResIdbyName(sp_id, res_name);
        if (res_id == -1)
        {
            Log.e("getAllStationByResName()", "error getResIdbyName()");
            return null;
        }
        return getAllStationsByResId(res_id);
    }

    public List<String> getAllStationsByResId(int res_id)
    {
        Cursor cur;
        List<Long> ids = new ArrayList<Long>();
        List<String> stations = new ArrayList<String>();
        List<Integer> np_ids = new ArrayList<Integer>();
        int sp_id,np_id;

        String selection = "res_id = ?";
        String[] selectionArgs = {String.valueOf(res_id)};

        try
        {
 //           cur = db.query("res_tbl", new String[]{"id", "name", "sp_id"},null,null,null,null,null);
            cur = db.query("station_tbl", new String[]{"id", "name", "sp_id", "np_id"},selection,selectionArgs,null,null,null);
            if (cur.getCount()==0) {
                Log.e("getAllStationsByResId()", "select return 0 records!");
                cur.close();
                return null;
            }
            else
            {
                while(cur.moveToNext()) {
                    long id=cur.getLong(cur.getColumnIndexOrThrow("id"));
                    sp_id= cur.getInt(cur.getColumnIndexOrThrow("sp_id"));
                    np_id= cur.getInt(cur.getColumnIndexOrThrow("np_id"));
                    String name=cur.getString(cur.getColumnIndexOrThrow("name"));
                    Log.d("getAllStationsByResId()", "id=" + id + " name=" + name + " res_id=" + res_id);
                    ids.add(id);
                    stations.add(name);
                    np_ids.add(np_id);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return stations;
    }

    public List<String> getAllResBySpName(String sp_name)
    {
        int sp_id=getSpIdbyName(sp_name);
        if (sp_id == -1)
        {
            Log.e("getAllResBySpName()", "error getSpIdbyName()");
            return null;
        }
        return getAllResBySpId(sp_id);
    }

    public List<String> getAllResBySpId(int sp_id)
    {
        Cursor cur;
        List<Long> ids = new ArrayList<Long>();
        List<String> res = new ArrayList<String>();

        String selection = "sp_id = ?";
        String[] selectionArgs = {String.valueOf(sp_id)};

        try
        {
 //           cur = db.query("res_tbl", new String[]{"id", "name", "sp_id"},null,null,null,null,null);
            cur = db.query("res_tbl", new String[]{"id", "name", "sp_id"},selection,selectionArgs,null,null,null);
            if (cur.getCount()==0) {
                Log.e("getResBySpId()", "select return 0 records!");
                cur.close();
                return null;
            }
            else
            {
                while(cur.moveToNext()) {
                    long id=cur.getLong(cur.getColumnIndexOrThrow("id"));
                    sp_id= cur.getInt(cur.getColumnIndexOrThrow("sp_id"));
                    String name=cur.getString(cur.getColumnIndexOrThrow("name"));
                    Log.i("getResBySpId()", "id=" + id + " name=" + name + " sp_id=" + sp_id);
                    ids.add(id);
                    res.add(name);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public boolean init_db()
    {
        // Gets the data repository in write mode
        try
        {
            db = dbHelper.getWritableDatabase();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        // TODO if empty db:
        //if (checkEmptyDb())
        if (true)
        {
            if (!test_fill_db())
            {
                Log.e("init_db()", "error test_fill_db()");
            }
        }
        return true;
    }
    protected boolean checkEmptyDb()
    {
        Cursor cur;
        try
        {
            cur = db.query("sp_tbl", new String[]{"id", "name"},null,null,null,null,null);
            if (cur.getCount()==0)
            {
                Log.i("checkEmptyDb()","select return 0 records!");
                cur.close();
                return true;
            }
            else
            {
                Log.i("checkEmptyDb()","select return " + cur.getCount() + "records");
                cur.close();
                return true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return true;
        }
    }

    protected boolean test_fill_db()
    {
        dbHelper.onUpgrade(db,1,1);
        // Заполняем СП:
        ContentValues values = new ContentValues();
        try
        {
            String[] sp= new String[]{"ЮЭС", "ЦЭС","СЭС","ЗЭС"};
            for(int index = 0; index< sp.length; index++)
            {
                values.put("id", index);
                values.put("name", sp[index]);


                long newRowId = db.insert("sp_tbl", null, values);
                if (newRowId==-1)
                {
                    Log.e("test_fill_db()", "error insert row: " + values.toString());
                    return false;
                }
            }


        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        // Заполняем РЭС:
        values = new ContentValues();
        int res_index=0;
        try
        {
            String[] res= new String[]{"РЭС3", "РЭС5","РЭС6","РЭС7"};
            for(int index = 0; index< res.length; index++)
            {
                values.put("id", res_index);
                values.put("name", res[index]);
                values.put("sp_id", 0);


                long newRowId = db.insert("res_tbl", null, values);
                if (newRowId==-1)
                {
                    Log.e("test_fill_db()", "error insert row: " + values.toString());
                    return false;
                }
                res_index++;
            }
            res = new String[]{"РЭС2", "РЭС1"};
            for(int index = 0; index< res.length; index++)
            {
                values.put("id", res_index);
                values.put("name", res[index]);
                values.put("sp_id", 1);


                long newRowId = db.insert("res_tbl", null, values);
                if (newRowId==-1)
                {
                    Log.e("test_fill_db()", "error insert row: " + values.toString());
                    return false;
                }
                res_index++;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        // Заполняем подстанции:
        values = new ContentValues();
        int uniq_id=1;
        try
        {
            String[] stations= new String[]{"ПС 110/35 Бурная", "ПС 110/6 Орлиная"};



            for(int index = 0; index< stations.length; index++)
            {
                values.put("name", stations[index]);
                values.put("uniq_id", uniq_id);
                values.put("sp_id", 0);
                values.put("res_id", 0);
                values.put("np_id", 0);
                uniq_id++;

                long newRowId = db.insert("station_tbl", null, values);
                if (newRowId==-1)
                {
                    Log.e("test_fill_db()", "error insert row: " + values.toString());
                    return false;
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
