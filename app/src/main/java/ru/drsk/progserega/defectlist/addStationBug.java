package ru.drsk.progserega.defectlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class addStationBug extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("addStationBug.onCreate()", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station_bug);
        Log.d("addStationBug.onCreate()", "2");
    }
}
