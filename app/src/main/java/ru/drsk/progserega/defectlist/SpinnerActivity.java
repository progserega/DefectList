package ru.drsk.progserega.defectlist;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private View rootView;
    private SqliteStorage sqliteStorage;

    public SpinnerActivity(View view, SqliteStorage sqlStorage) {
        rootView = view;
        sqliteStorage = sqlStorage;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Log.d("onItemSelected()", "text: " + parent.getItemAtPosition(pos).toString());
        Spinner sp_spinner = (Spinner) rootView.findViewById(R.id.sp_selector);
        Spinner res_spinner = (Spinner) rootView.findViewById(R.id.res_selector);
        Spinner station_spinner = (Spinner) rootView.findViewById(R.id.station_selector);
        Button station_add_bug = (Button) rootView.findViewById(R.id.station_add_bug);
        Log.d("onItemSelected()", "text: " + parent.getItemAtPosition(pos).toString());
        if (parent.equals(sp_spinner))
        {
            Log.i("onItemSelected()", "sp_selector: " + parent.getItemAtPosition(pos).toString());

            // заполнение списка РЭС:
            String current_sp_text = sp_spinner.getSelectedItem().toString();
            Log.d("onItemSelected()", "current selected SP:" + current_sp_text);

            List<String> res=sqliteStorage.getAllResBySpName(current_sp_text);
            if(res==null)
            {
                Log.e("onItemSelected()", "sqliteStorage.getAllResBySpName() error");
                res = new ArrayList<String>();
            }
            Log.i("onItemSelected()", "size of list res: " + res.size());

            ArrayAdapter<String> res_adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.one_row, R.id.text, res);
            res_spinner.setAdapter(res_adapter);
        }
        else if (parent.equals(res_spinner)) {
            Log.i("onItemSelected()", "res_selector: " + parent.getItemAtPosition(pos).toString());

            // заполнение списка подстанций:
            String current_sp_text = sp_spinner.getSelectedItem().toString();
            Log.d("OnItemSelected()", "current selected SP:" + current_sp_text);
            String current_res_text = res_spinner.getSelectedItem().toString();
            Log.d("OnItemSelected()", "current selected SP:" + current_res_text);

            List<String> stations = sqliteStorage.getAllStationByResName(current_sp_text, current_res_text);
            if (stations == null) {
                Log.e("onItemSelected()", "sqliteStorage.getAllStationByResName() error");
                stations = new ArrayList<String>();
                // кнопку добавления ошибки делаем ненажимаемой:
                station_add_bug.setEnabled(false);
            }
            Log.d("onItemSelected()", "size of list res: " + stations.size());

            ArrayAdapter<String> res_adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.one_row, R.id.text, stations);
            station_spinner.setAdapter(res_adapter);

        } else if (parent.equals(station_spinner)) {
            Log.d("onItemSelected()", "station_sp_spinner: " + parent.getItemAtPosition(pos).toString());

            // кнопку добавления ошибки делаем ненажимаемой:
            station_add_bug.setEnabled(true);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        Log.i("onNothingSelected()", "Nothing");
    }
}
