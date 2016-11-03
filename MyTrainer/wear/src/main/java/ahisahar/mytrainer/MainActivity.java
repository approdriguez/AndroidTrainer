package ahisahar.mytrainer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
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

public class MainActivity extends WearableActivity implements SensorEventListener, DataApi.DataListener, MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String KEY = "SensorService";
    private static final String ITEM_0="/accelerometer0";
    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    private static final String TAG = "AccelerometerData";
    //private final static int SENS_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mGyro;

    GoogleApiClient apiClient;
    TextView x,y,z;
    PutDataMapRequest putDataMapReq;
    PendingResult<DataApi.DataItemResult> resultado;
//  float[] accelerometer = new float[120000];
    float[] accelerometer = new float[6];
    PutDataRequest putDataReq;
    Boolean stop;
    Boolean send;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        stop = false; //Parar medición
        send = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_UI);

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

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            if(!stop) {
                x.setText(Float.toString(event.values[0]));
                y.setText(Float.toString(event.values[1]));
                z.setText(Float.toString(event.values[2]));
                accelerometer[0] = event.values[0];
                accelerometer[1] = event.values[1];
                accelerometer[2] = event.values[2];
            }
        }

        else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            if(!stop) {
                accelerometer[3] = event.values[0];
                accelerometer[4] = event.values[1];
                accelerometer[5] = event.values[2];
                stop = true;
                send = true;
            }
        }
        if(send){
            putDataMapReq = PutDataMapRequest.create(ITEM_0);
            putDataMapReq.getDataMap().putFloatArray(KEY, accelerometer);
            putDataReq = putDataMapReq.asPutDataRequest();
            resultado = Wearable.DataApi.putDataItem(apiClient, putDataReq);
            Wearable.DataApi.putDataItem(apiClient, putDataReq);
            Log.d(TAG, "Connected mensaje enviado");
            stop=false;
            send=false;
        }
            //i=0;
        //}

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
    }

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


    private void enviarMensaje(final String path, final String texto){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodos=Wearable.NodeApi.getConnectedNodes(apiClient).await();
                Log.d(TAG, "sincronizacion betaalfa");
                for (Node nodo: nodos.getNodes()){
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
