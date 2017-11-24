package ahisahar.mytrainer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
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
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import static android.R.attr.gravity;

public class MainActivity extends WearableActivity implements SensorEventListener, DataApi.DataListener, MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String KEY = "SensorService";
    private static final String ITEM_0 = "/accelerometer0";
    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    private static final String TAG = "AccelerometerData";
    //private final static int SENS_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mGyro;
    private Sensor mMag;

    GoogleApiClient apiClient;

    PutDataMapRequest putDataMapReq;
    PendingResult<DataApi.DataItemResult> resultado;
    //  float[] accelerometer = new float[120000];
    float[] accelerometer = new float[9];
    PutDataRequest putDataReq;
    //Parar medición
    Boolean stop = false, send = false, meassuring = false;
    int visibility = View.INVISIBLE;
    float[] mGravity = new float[3];
    float [] mMagneticfield = new float[3];
    float []mOrientation;float []mQuaternion;
    //LOW PASS FILTER
    float []gravity = new float[3];
    float []linear_acceleration = new float[3];
    final float alpha = 0.7501f;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        //*****UI objects****

        /*******************/
        final Button button = (Button) findViewById(R.id.button);
        final TextView info = (TextView) findViewById(R.id.info);
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        //Infinite bar
        bar.setIndeterminate(true);
        //  *************Start/Stop button*************
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                info.setVisibility(visibility);
                meassuring = !meassuring;
                if (meassuring) {
                    visibility = View.VISIBLE;
                    button.setText("STOP");
                } else {
                    visibility = View.INVISIBLE;
                    button.setText("START");
                }
                bar.setVisibility(visibility);


            }
        });
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        //x = (TextView) findViewById(R.id.x);
        //y = (TextView) findViewById(R.id.y);
        //z = (TextView) findViewById(R.id.z);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        mSensorManager.registerListener(this, mMag, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_FASTEST);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)//nos notifica cuando estamos conectados
                .addOnConnectionFailedListener(this)// ofrece el resultado del error
                .build();

        apiClient.connect();


    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Connected to Google Api Service");
        }
        Wearable.DataApi.addListener(apiClient, this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (meassuring) {

            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

                if (!stop) {

                /*
                    // Isolate the force of gravity with the low-pass filter.
                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                    // Remove the gravity contribution with the high-pass filter.
                    accelerometer[0] = event.values[0] - gravity[0];
                    accelerometer[1] = event.values[1] - gravity[1];
                    accelerometer[2] = event.values[2] - gravity[2];
                */
                    //x.setText(Float.toString(event.values[0]));
                    //y.setText(Float.toString(event.values[1]));
                    //z.setText(Float.toString(event.values[2]));
                    accelerometer[0] = event.values[0];
                    accelerometer[1] = event.values[1];
                    accelerometer[2] = event.values[2];


                    accelerometer[3] = event.values[0];
                    accelerometer[4] = event.values[1];
                    accelerometer[5] = event.values[2];
                    accelerometer[6] = event.values[0];
                    accelerometer[7] = event.values[1];
                    accelerometer[8] = event.values[2];
                    stop = true;
                    send = true;
                    Log.d("valorx", Float.toString(event.values[0]));
                    Log.d("valory", Float.toString(event.values[1]));
                    Log.d("valorz", Float.toString(event.values[2]));
                    Log.d("valorxx", Float.toString(accelerometer[0]));
                    Log.d("valoryy", Float.toString(accelerometer[1]));
                    Log.d("valorzz", Float.toString(accelerometer[2]));


                }
            }/* else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                if (!stop) {
                    accelerometer[3] = event.values[0];
                    accelerometer[4] = event.values[1];
                    accelerometer[5] = event.values[2];
                    Log.d("valora", Float.toString(event.values[0]));
                    Log.d("valorb", Float.toString(event.values[1]));
                    Log.d("valorc", Float.toString(event.values[2]));

                }
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                if (!stop) {
                    accelerometer[6] = event.values[0];
                    accelerometer[7] = event.values[1];
                    accelerometer[8] = event.values[2];
                    Log.d("valord", Float.toString(event.values[0]));
                    Log.d("valore", Float.toString(event.values[1]));
                    Log.d("valorf", Float.toString(event.values[2]));
                    stop = true;
                    send = true;
                }
            }*/
        }
            if (send) {
                putDataMapReq = PutDataMapRequest.create(ITEM_0);
                putDataMapReq.getDataMap().putFloatArray(KEY, accelerometer);
                putDataReq = putDataMapReq.asPutDataRequest();
                resultado = Wearable.DataApi.putDataItem(apiClient, putDataReq);
                Wearable.DataApi.putDataItem(apiClient, putDataReq);
                Log.d(TAG, "Connected mensaje enviado");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        stop = false;
                        //send = false;
                    }
                }, 333);
                //stop = false;
                send = false;
            }

        }

    //i=0;


    //enviarMensaje(ITEM_1, accelerometer.toString());
/*
        //Valor eje y
        putDataMapReq = PutDataMapRequest.create(ITEM_1);
        putDataMapReq.getDataMap().putFloat(KEY,  accelerometer[1]);
        putDataReq = putDataMapReq.asPutDataRequest();
        resultado = Wearable.DataApi.putDataItem(apiClient, putDataReq);
        //enviarMensaje(ITEM_1, Float.toString(accelerometer[1]));

        //Valor eje z
        putDataMapReq = PutDataMapRequest.create(ITEM_2);
        putDataMapReq.getDataMap().putFloat(KEY,  accelerometer[2]);
        putDataReq = putDataMapReq.asPutDataRequest();
        resultado = Wearable.DataApi.putDataItem(apiClient, putDataReq);
        //enviarMensaje(ITEM_2, Float.toString(accelerometer[2]));*/


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void enviarMensaje(final String path, final String texto) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodos = Wearable.NodeApi.getConnectedNodes(apiClient).await();
                Log.d(TAG, "sincronizacion betaalfa");
                for (Node nodo : nodos.getNodes()) {
                    Wearable.MessageApi.sendMessage(apiClient, nodo.getId(), path, texto.getBytes())
                            .setResultCallback(
                                    new ResultCallback<MessageApi.SendMessageResult>() {
                                        @Override
                                        public void onResult(MessageApi.SendMessageResult resultado) {
                                            if (!resultado.getStatus().isSuccess()) {
                                                Log.e("sincronizacion", "Error al enviar mensaje. Codigo" + resultado.getStatus().getStatusCode());
                                            }
                                        }
                                    }
                            );
                }

            }
        }).start();

    }

    @Override
    public void onStart() {
        apiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        apiClient.disconnect();
        super.onStop();

    }
}
