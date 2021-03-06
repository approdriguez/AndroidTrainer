package ahisahar.mytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Messages
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.nio.*;

import ahisahar.mytrainer.authentication.LogInActivity;
import ahisahar.mytrainer.orientation.GravityCompensation;
import ahisahar.mytrainer.orientation.Orientation;

public class exercise extends AppCompatActivity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private float[] yData = {};
    private float[] xData = {};
    //ArrayList<Float> yData = new ArrayList<>();
    //ArrayList<Float> xData = new ArrayList<>();
    private Button logout, powerButton;
    private static final String TAG = "AccelerometerData";

    private Orientation orientacion = new Orientation(1, 1, 1, 0, 0, 0);
    private GravityCompensation gravityCompensation = new GravityCompensation();

    //messages
    private static final String KEY = "SensorService";
    private static final String ITEM_0 = "/accelerometer0";
    private static final String ITEM_1 = "/accelerometer1";
    private static final String ITEM_2 = "/accelerometer2";

    static final float ALPHA = 0.25f;
    private float acel_x = 0, acel_y = 0, acel_z = 0;
    float[] acel = new float[9];
    float[] acelfixed = new float[3];
    float[] acelnotfiltered = new float[192];
    float[] acelfiltered = new float[192];
    Orientation.Quaternion quaternion;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    GraphView graph;
    int count = 0;
    Boolean timecount = false;
    long startTime, difference;
    double modulo = 0;
    ArrayList<Float> accelerometer = new ArrayList();
    ArrayList<Float> gyroscope = new ArrayList();
    int filter = 0;
    boolean f = false;
    double[] ventana = new double[5];
    int cventana = 0;
    int dif = 0;
    double[] datos = new double[5];
    double  time;
    float velocity = 0, previousAcceleration = 0, acceleration = 0, force = 0,weight = 5,power = 0,maxpower = 0,s=1;
    GoogleApiClient apiClient;
    Chronometer chronometer;
    final float windowUp = 0.1f;
    final float windowDown = -0.1f;
    String strDate,tittl,hour;
    EditText powerValue;
    Ex exinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        tittl = getIntent().getStringExtra("NAME");

        //Weight by default

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sff = new SimpleDateFormat("HH:mm:ss");
        hour = sff.format(c.getTime());
        strDate = sdf.format(c.getTime());
        /*if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {*/
        mUserId = mFirebaseUser.getUid();

        //Bundle b = getIntent().getExtras();
        //int id = b.getInt("id");
        ////////////////

        graph = (GraphView) findViewById(R.id.graph);

        //Texedit inmutables

        EditText powerLabel = (EditText) findViewById(R.id.powerLabel);
        powerValue = (EditText) findViewById(R.id.powerValue);
        final EditText weightLabel = (EditText) findViewById(R.id.weightLabel);
        EditText weightValue = (EditText) findViewById(R.id.weightValue);
        TextView tittle = (TextView) findViewById(R.id.titulo);
        powerLabel.setKeyListener(null);
        weightLabel.setKeyListener(null);
        powerValue.setKeyListener(null);
        weightValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        weightValue.setText("5");
        weightValue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                weight = Integer.parseInt(weightLabel.getText().toString());
            }
        });
        tittle.setText(tittl);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        ///////////////
        // Refresh graph

        final Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // clean graph
                graph.removeAllSeries();
                series = new LineGraphSeries<>();
                chronometer.setBase(SystemClock.elapsedRealtime());
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sff = new SimpleDateFormat("HH:mm:ss");
                hour = sff.format(c.getTime());
                velocity = 0;
                acceleration = 0;
                s++;

            }
        });


        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this) //nos notifica cuando estamos conectados
                .addOnConnectionFailedListener(this)// ofrece el resultado del error
                .build();


    }


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


    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


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
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                    /*if (item.getUri().getPath().equals(ITEM_0)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(item);

                        acel_x = dataMapItem.getDataMap().getFloat(KEY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.x)).setText(Float.toString(acel_x));

                            }
                        });
                    }*/
                Log.d(TAG, "Connected cambio de datos detectado");

                if (item.getUri().getPath().compareTo(ITEM_0) == 0) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(item);
                    //((TextView) findViewById(R.id.x)).setText("LLego");
                    chronometer.start();
                    acel = dataMapItem.getDataMap().getFloatArray(KEY);
                    if (!timecount) {
                        timecount = true;
                        startTime = SystemClock.elapsedRealtime();
                    }
                    /*  Unfiltered accelerometer data */
                    /*
                    mDatabase.child("users").child(mUserId).child("test").child(Integer.toString(count)).child("x").setValue(acel[0]);
                    mDatabase.child("users").child(mUserId).child("test").child(Integer.toString(count)).child("y").setValue(acel[1]);
                    mDatabase.child("users").child(mUserId).child("test").child(Integer.toString(count)).child("z").setValue(acel[2]);
                    difference = SystemClock.elapsedRealtime() - startTime;
                    mDatabase.child("users").child(mUserId).child("test").child(Integer.toString(count)).child("time").setValue(difference);
                    //*//* save raw data */
                    /*
                    mDatabase.child("users").child(mUserId).child("gyroscopeStopped4").child(Integer.toString(count)).child("x").setValue(acel[3]);
                    mDatabase.child("users").child(mUserId).child("gyroscopeStopped4").child(Integer.toString(count)).child("y").setValue(acel[4]);
                    mDatabase.child("users").child(mUserId).child("gyroscopeStopped4").child(Integer.toString(count)).child("z").setValue(acel[5]);
                    mDatabase.child("users").child(mUserId).child("gyroscopeStopped4").child(Integer.toString(count)).child("time").setValue(difference);
                    */
                    quaternion = orientacion.update((double) acel[0], (double) acel[1], (double) acel[2], (double) acel[3], (double) acel[4], (double) acel[5],(double) acel[6],(double) acel[7],(double) acel[8]);
                    //quaternion = orientacion.create((double)acel[3],(double)acel[4],(double)acel[5],(double)acel[6]);
                    //acelfixed = gravityCompensation.fixAccelerometerData(quaternion, acel[0], acel[1], acel[2]);
                    acelfixed = gravityCompensation.fixAccelerometerData(quaternion, acel[0], acel[1], acel[2]);

                    //          Save filtered data/
                    //strDate

                        mDatabase.child("users").child(mUserId).child(strDate).child(tittl).child(hour).child(Integer.toString(count)).child("x").setValue(acel[0]);
                        mDatabase.child("users").child(mUserId).child(strDate).child(tittl).child(hour).child(Integer.toString(count)).child("y").setValue(acel[1]);
                        mDatabase.child("users").child(mUserId).child(strDate).child(tittl).child(hour).child(Integer.toString(count)).child("z").setValue(acel[2]);
                        mDatabase.child("users").child(mUserId).child(strDate).child(tittl).child(hour).child(Integer.toString(count)).child("velocity").setValue(velocity);
                        difference = SystemClock.elapsedRealtime() - startTime;
                        mDatabase.child("users").child(mUserId).child(strDate).child(tittl).child(hour).child(Integer.toString(count)).child("time").setValue(difference);


                        mDatabase.child("users").child(mUserId).child(strDate).child("Resumen "+tittl).setValue(new Ex(velocity,maxpower,s,weight));

                    //modulo = sqrt(pow(acelfixed[2], 2)); //Solo teniendo en cuenta el eje Z
                    //Acceleration asgined
                    //acceleration = acelfixed[0];
                    acceleration = acel[0];
                    //********************acceleration = acel[0];
                    Log.e("valorSinCalibrar",Float.toString(acceleration));
                    //

                    //Check if there is real movement
                    acceleration = mechanicalFilteringWindow(acceleration);
                    //calculate force
                    force = acceleration * weight;
                    Log.e("valorConVentana",Float.toString(acceleration));

                    //Integrate acceleration to calculate velocity

                    velocity = computeVelocity(previousAcceleration, velocity, acceleration);
                    power = velocity * force;
                    if(power>=maxpower){
                        maxpower = power;
                        powerValue = (EditText) findViewById(R.id.powerValue);
                        powerValue.setText(Float.toString(maxpower));
                    }
                    //Last acceleration value
                    previousAcceleration = acceleration;
                    /*Window
                    //modulo = sqrt(pow(acelfixed[0], 2) * pow(acelfixed[1], 2) * pow(acelfixed[2], 2));
                    if(cventana<5){
                        ventana[cventana]=modulo;
                        cventana++;
                    }

                    if(cventana==4){
                        for(int i=0;i<ventana.length-1;i++){
                            dif += abs(ventana[i]-ventana[i+1])/(abs(ventana[i]-ventana[i+1])/2);
                        }
                        dif = dif / ventana.length;
                        if(dif<=0.4)
                            for(int i=0;i<ventana.length;i++)
                                ventana[i] = 0;
                        for(int i=0;i<ventana.length;i++)
                            datos[i]=ventana[i];
                    }*/


                    /*

                    Proceso de filtrado usando un filtro paso bajo

                    modulo = 0;
                    if(filter<189){
                        acelnotfiltered[filter]=(float)acelfixed[0];
                        acelnotfiltered[filter+1]=(float)acelfixed[1];
                        acelnotfiltered[filter+2]=(float)acelfixed[2];
                        filter=filter+3;
                    }
                    //if(acelnotfiltered[191]!=0)
                        f = true;
                    acelfiltered = lowPass(acelnotfiltered,acelfiltered);
                    for(int i=0;i<acelfiltered.length && f;i+=3){
                        modulo = sqrt(pow(acelfiltered[i+2], 2));
                        series.appendData(new DataPoint(count, modulo), false, 200);
                        count++;
                        graph.addSeries(series);
                        System.out.print(i);
                        System.out.print(filter);
                        if(i==acelfiltered.length-1)
                            filter = 0;f = false;
                    }

                    *//*
                    if(cventana==4) {
                        for(int i=0;i<datos.length;i++){
                            series.appendData(new DataPoint(count, datos[i]), false, 200);
                            count++;
                            graph.addSeries(series);
                        }
                        cventana=0;
                    }
                    */
                    //if (modulo < 0.4) modulo = 0;
                    //modulo = aceleracion en m/s^2
                    modulo = velocity;
                    //modulo = sqrt(pow(modulo,2));
                    time = SystemClock.elapsedRealtime() - startTime;
                    //velocity = modulo * time;
                    //force = modulo * weight;
                    
                    series.appendData(new DataPoint(count, modulo), false, 200);
                    count++;
                    graph.addSeries(series);


                }


            } else if (event.getType() == DataEvent.TYPE_DELETED) {//algun item a sido borrado

            }
            chronometer.stop();


        }
    }

    public static byte[] encode(float floatArray[]) {
        byte byteArray[] = new byte[floatArray.length * 4];

// wrap the byte array to the byte buffe
//
//
//
//
//
//
// r
        ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);

// create a view of the byte buffer as a float buffer
        FloatBuffer floatBuf = byteBuf.asFloatBuffer();

// now put the float array to the float buffer,
// it is actually stored to the byte array
        floatBuf.put(floatArray);

        return byteArray;
    }

    //Low-pass filter using alpha=0.25
    public float[] lowPass(float[] input, float[] output) {
        if (output == null)
            return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    //Mechanical filtering window for zero movement condition
    public float mechanicalFilteringWindow(float acceleration) {

        if (acceleration <= windowUp && acceleration >= windowDown)
            acceleration = 0;
        return acceleration;
    }

    //First integration from acceleration in order to calculate velocity

    public float computeVelocity(float previousAcceleration, float previousVelocity, float currentAcceleration) {

        return (previousVelocity + previousAcceleration + ((int) (currentAcceleration - previousAcceleration) >> 1));

    }


}

