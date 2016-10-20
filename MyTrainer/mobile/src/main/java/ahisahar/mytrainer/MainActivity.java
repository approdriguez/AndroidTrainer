package ahisahar.mytrainer;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import jkalman.JKalman;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Messages
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataApi.DataListener,  GoogleApiClient.ConnectionCallbacks,  GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private PieChart piechart;
    private float[] yData = {5,10,15,20,40};
    private String[] xData = {"Lunes","Martes","Miercoles","Jueves","Viernes"};
    private Button logout,powerButton;
    private static final String TAG = "AccelerometerData";

    //messages
    private static final String KEY = "SensorService";
    private static final String ITEM_0="/accelerometer0";
    private static final String ITEM_1="/accelerometer1";
    private static final String ITEM_2="/accelerometer2";

    private float acel_x=0,acel_y=0,acel_z=0;
    float [] acel = new float[3];




    GoogleApiClient apiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();


            ////////////////
            //piechart = new PieChart(this);
            piechart = (PieChart) findViewById(R.id.chartactivity);

            //mainlayout.addView(piechart);

            piechart.setUsePercentValues(true);
            piechart.setDescription("Tu actividad esta semana");
            piechart.setDrawHoleEnabled(true);
            piechart.setHoleColor(0);
            piechart.setHoleRadius(7);

            piechart.setRotationAngle(0);
            piechart.setRotationEnabled(true);

            piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (e == null)
                        return;
                    Toast.makeText(MainActivity.this, xData[(int) e.getX()]+ " = "+ e.getData() + "%", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected() {

                }
            });
            addData();
            Legend l = piechart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);

            ///////////////
            // Add items via the Button and EditText at the bottom of the view.


            // Use Firebase to populate the list.
            //Adding and configuring charts of home page

        }
        logout = (Button) findViewById(R.id.logoutbutton);
        logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mFirebaseAuth.signOut();
                loadLogInView();
            }
        });

        powerButton = (Button) findViewById(R.id.powerbutton);
        powerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadPowerView();
            }
        });
        ((TextView) findViewById(R.id.z)).setText(Float.toString(1));
        //Receive the message




/*
        PendingResult<DataItemBuffer> resultado= Wearable.DataApi.getDataItems(apiClient);
        resultado.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(DataItemBuffer dataItems) {

                for (DataItem dataItem : dataItems) {

                    /*if (dataItem.getUri().getPath().equals(ITEM_0)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);

                        acel_x = dataMapItem.getDataMap().getFloat(KEY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.x)).setText(Float.toString(acel_x));

                            }
                        });
                    }*/

             /*       if (dataItem.getUri().getPath().equals(ITEM_1)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
                        acel = dataMapItem.getDataMap().getFloatArray(KEY);
                       /* runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ((TextView) findViewById(R.id.y)).setText(Float.toString((Float)acel[1]));

                            }
                        });*/
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this) //nos notifica cuando estamos conectados
                .addOnConnectionFailedListener(this)// ofrece el resultado del error
                .build();


    }
                    /*
                    if (dataItem.getUri().getPath().equals(ITEM_2)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);

                        acel_z = dataMapItem.getDataMap().getInt(KEY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.z)).setText(Float.toString(acel_z));

                            }
                        });
                    }*/
          /*      }
                dataItems.release();
                ((TextView) findViewById(R.id.z)).setText(Float.toString(23));
            }
        });

    }*/

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(apiClient, this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(apiClient, this);
        apiClient.disconnect();
    }


    private void addData(){

        List<PieEntry> entries = new ArrayList<>();

        for(int i=0;i<yData.length && i<xData.length ;i++)
            entries.add(new PieEntry(yData[i],xData[i]));

        PieDataSet dataSet = new PieDataSet(entries, "Mi actividad");
        /*
        dataSet.setSliceSpace(5);
        dataSet.setSelectionShift(5);
        */
        //Add some colors

        ArrayList<Integer> colors= new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //Instantiate pie data

        PieData pieData = new PieData(dataSet);
        piechart.setData(pieData);
        piechart.invalidate();
        piechart.setNoDataText("Error generating the chart");

    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void loadPowerView() {
        Intent intent = new Intent(this, power_select_exercise.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onDataChanged(DataEventBuffer eventos) {

            for (DataEvent event : eventos) {
                if (event.getType() == DataEvent.TYPE_CHANGED){
                    DataItem item =event.getDataItem();
                    /*if (item.getUri().getPath().equals(ITEM_0)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(item);

                        acel_x = dataMapItem.getDataMap().getFloat(KEY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.x)).setText(Float.toString(acel_x));

                            }
                        });
                    }*/Log.d(TAG, "Connected cambio de datos detectado");

                if (item.getUri().getPath().compareTo(ITEM_0) == 1) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(item);
                        ((TextView) findViewById(R.id.x)).setText("LLego");
                        acel = dataMapItem.getDataMap().getFloatArray(KEY);
                        if(acel!=null){


                                    ((TextView) findViewById(R.id.y)).setText(Float.toString((Float)acel[2]));



                        }
                    }

                    /*if (item.getUri().getPath().equals(ITEM_2)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(item);

                        acel_z = dataMapItem.getDataMap().getInt(KEY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.z)).setText(Float.toString(acel_z));

                            }
                        });
                    }*/
                } else if(event.getType()==DataEvent.TYPE_DELETED){//algun item a sido borrado

                }



            }
        apiClient.disconnect();
        apiClient.connect();
        }


}
