package ru.drsk.progserega.defectlist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

        // успешный выход из активности:
        setResult(Activity.RESULT_OK);
        Log.d("addStationBug.stationSaveBug()", "close activity");
        finish();
    }

}
