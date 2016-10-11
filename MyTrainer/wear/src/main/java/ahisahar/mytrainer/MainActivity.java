package ahisahar.mytrainer;

import android.content.Context;
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
    private static final String ITEM_1="/accelerometer1";
    private static final String ITEM_2="/accelerometer2";
    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    //private final static int SENS_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    GoogleApiClient apiClient;
    TextView x,y,z;
    PutDataMapRequest putDataMapReq;
    PendingResult<DataApi.DataItemResult> resultado;
    Float[] accelerometer = new Float[3];
    PutDataRequest putDataReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)//nos notifica cuando estamos conectados
                .addOnConnectionFailedListener(this)// ofrece el resultado del error
                .build();
        if(!apiClient.isConnected())
            apiClient.connect();

        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        accelerometer[0]=event.values[0];
        accelerometer[1]=event.values[1];
        accelerometer[2]=event.values[2];

        x.setText(Float.toString(event.values[0]));
        y.setText(Float.toString(event.values[1]));
        z.setText(Float.toString(event.values[2]));

        //Send X acceleration
        putDataMapReq = PutDataMapRequest.create(ITEM_0);


        float pepe []= new float[3];
        putDataMapReq.getDataMap().putFloatArray(KEY, pepe);
        putDataReq = putDataMapReq.asPutDataRequest();
        resultado = Wearable.DataApi.putDataItem(apiClient, putDataReq);
        Wearable.DataApi.putDataItem(apiClient,putDataReq);


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
        //enviarMensaje(ITEM_2, Float.toString(accelerometer[2]));
        x.setText("Mensaje enviado");
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
    public void onConnected(@Nullable Bundle bundle) {

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

    /*
    private void enviarMensaje(final String path, final String texto){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodos=Wearable.NodeApi.getConnectedNodes(apiClient).await();

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

    }*/

}
