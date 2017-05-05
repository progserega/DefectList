package ru.drsk.progserega.defectlist;

import java.util.Calendar;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static Boolean syncActive=false;
    public SqliteStorage sqliteStorage;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// Инициализация приложения:
        // внутренняя sqlite-база:
        sqliteStorage = SqliteStorage.getInstance(getApplicationContext());
        if (sqliteStorage==null)
        {
            Log.e("MainActivity.onCreate()", "sqliteStorage.getInstance() error");
        }
        Intent SyncService = new Intent(this, SyncService.class);
        startService(SyncService);
        ////////////==============

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Called when the user clicks the Send button
     */
    public void stationAddBug(View view) {
        Log.d("stationAddBug()", "1");

        // Do something in response to button
        //Intent intent = new Intent(this, addStationBug.class);
        Intent intent = new Intent(this, addStationBug.class);
            /*EditText editText = (EditText) findViewById(R.id.edit_message);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);*/
        Log.d("stationAddBug()", "2");
        startActivity(intent);
        Log.d("stationAddBug()", "3");

    }

    /**
     * Called when the user clicks the Send button
     */
    public void startSync(View view) {

        Log.d("startSync()", "1");
        if (!syncActive)
        {
            syncActive=true;
            Log.d("startSync()", "Sync not active - start it");
            Button startSyncButton = (Button) findViewById(R.id.sync_button);
            // устанавливаем кнопку отмены:
            startSyncButton.setText(R.string.sync_stop_button_text);
            ProgressBar syncProgress = (ProgressBar) findViewById(R.id.progressBar);
            syncProgress.setVisibility(View.VISIBLE);
            apiJsonSync sync = new apiJsonSync(getApplicationContext());
            sync.syncDicts();
        }
        else
        {
            syncActive=false;
            Log.d("startSync()", "Sync active - stop it");
            Button startSyncButton = (Button) findViewById(R.id.sync_button);
            // устанавливаем кнопку отмены:
            startSyncButton.setText(R.string.sync_button_text);
            ProgressBar syncProgress = (ProgressBar) findViewById(R.id.progressBar);
            syncProgress.setVisibility(View.INVISIBLE);
        }


        //syncOkCallback();
    }

    public void syncOkCallback() {

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        Log.d("syncOkCallback()", String.valueOf(c));
        EditText dateLastSyncLabel = (EditText) findViewById(R.id.sync_date);
        String dataString = null;
        dataString = hour + ":" + minutes + ":" + seconds + " " + day + "." + month + "." + year;
        Log.d("result_date_sync",dataString);

        syncActive=false;
        Log.d("startSync()", "Sync active - stop it");
        Button startSyncButton = (Button) findViewById(R.id.sync_button);
        // устанавливаем кнопку отмены:
        startSyncButton.setText(R.string.sync_button_text);
        ProgressBar syncProgress = (ProgressBar) findViewById(R.id.progressBar);
        syncProgress.setVisibility(View.INVISIBLE);
        dateLastSyncLabel.setText(dataString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                // Подстанции:
                Log.d("onCreateView()", "stations tab init");
                View rootView = inflater.inflate(R.layout.fragment_stations, container, false);
                //        TextView textView = (TextView) rootView.findViewById(R.id.section_station_label);
                //       textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                // кнопку добавления ошибки делаем ненажимаемой:
                Button station_add_bug = (Button) rootView.findViewById(R.id.station_add_bug);
                station_add_bug.setEnabled(false);

                /// Инициализация приложения:
                // внутренняя sqlite-база:
                SqliteStorage sqliteStorage = SqliteStorage.getInstance(this.getContext());
                if (sqliteStorage==null)
                {
                    Log.e("MainActivity.onCreate()", "sqliteStorage.getInstance() error");
                }
                ////////////==============

                // заполнение списка СП:
                List<String> sp=sqliteStorage.getAllSp();
                if(sp==null)
                {
                    Log.e("SelectStationActivity()", "sqliteStorage.getAllSp() error");
                    return rootView;
                }
                Spinner sp_spinner = (Spinner) rootView.findViewById(R.id.sp_selector);
                // выставляем оформление и содержимое:
                ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.one_row, R.id.text, sp);
                sp_spinner.setAdapter(sp_adapter);

                Spinner res_spinner = (Spinner) rootView.findViewById(R.id.res_selector);
                Spinner station_spinner = (Spinner) rootView.findViewById(R.id.station_selector);
                // прописываем обработчик:
                SpinnerActivity spinnerActivity = new SpinnerActivity(rootView, sqliteStorage);

                sp_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) spinnerActivity);
                res_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) spinnerActivity);
                station_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) spinnerActivity);

                return rootView;
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                // ТП:
                Log.d("onCreateView()", "tp tab init");
                View rootView = inflater.inflate(R.layout.fragment_tp, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_tp_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                // Опоры:
                Log.d("onCreateView()", "opors tab init");
                View rootView = inflater.inflate(R.layout.fragment_opora, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_opora_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                // Опоры:
                Log.d("onCreateView()", "sync tab init");
                View rootView = inflater.inflate(R.layout.fragment_sync, container, false);
 /*               TextView textView = (TextView) rootView.findViewById(R.id.section_opora_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                */
                return rootView;
            }

            return null;


        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Подстанции";
                case 1:
                    return "ТП";
                case 2:
                    return "Опоры";
                case 3:
                    return "Синхронизация";
            }
            return null;
        }
    }
}
