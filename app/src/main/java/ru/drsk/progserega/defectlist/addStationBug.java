package ru.drsk.progserega.defectlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class addStationBug extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("addStationBug.onCreate()", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station_bug);
        Log.d("addStationBug.onCreate()", "2");
    }

    /**
     * Called when the user clicks the Send button
     */
    public void stationSaveBug(View view) {
        Log.d("addStationBug.stationSaveBug()", "start save data");

        // TODO добавить сохранение в базу
        SqliteStorage sqliteStorage = SqliteStorage.getInstance(getApplicationContext());
        if (sqliteStorage==null)
        {
            Log.e("MainActivity.onCreate()", "sqliteStorage.getInstance() error");
        }
        EditText editText = (EditText) findViewById(R.id.station_bug_comment);
        String comment = editText.getText().toString();

        if (sqliteStorage.add_station_defect(1,1,comment)==true)
        {
            msbox("message","success save to db!");
        }
        else
        {
            msbox("message","Error save to db!");
        }


        // успешный выход из активности:
        setResult(Activity.RESULT_OK);
        Log.d("addStationBug.stationSaveBug()", "close activity");
        //finish();
    }

    private void msbox(String str,String str2)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setTitle(str);
        dlgAlert.setMessage(str2);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 finish();
            }
       });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
