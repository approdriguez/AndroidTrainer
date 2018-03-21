package ahisahar.mytrainer.historical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import ahisahar.mytrainer.Ex;
import ahisahar.mytrainer.R;

public class history_exercises extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId,pepe;
    private String date = "GEAWFE";
    TextView series_remo, potencia_remo, velocidad_remo, peso_remo, series_peso, potencia_peso, velocidad_peso, peso_Peso;
    TextView series_sentadillas, potencia_sentadillas, velocidad_sentadillas, peso_sentadillas, series_press, potencia_press, velocidad_press, peso_press;
    Ex exercise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_exercises);

        series_remo = (TextView) findViewById(R.id.series_remo);
        potencia_remo = (TextView) findViewById(R.id.potencia_remo);
        velocidad_remo = (TextView) findViewById(R.id.velocidad_remo);
        peso_remo = (TextView) findViewById(R.id.peso_remo);

        series_peso = (TextView) findViewById(R.id.series_peso);
        potencia_peso = (TextView) findViewById(R.id.potencia_peso);
        velocidad_peso = (TextView) findViewById(R.id.velocidad_peso);
        peso_Peso  = (TextView) findViewById(R.id.peso_Peso);

        series_sentadillas= (TextView) findViewById(R.id.series_sentadillas);
        potencia_sentadillas= (TextView) findViewById(R.id.potencia_sentadillas);
        velocidad_sentadillas= (TextView) findViewById(R.id.velocidad_sentadillas);
        peso_sentadillas= (TextView) findViewById(R.id.peso_sentadillas);

        series_press= (TextView) findViewById(R.id.series_press);
        potencia_press= (TextView) findViewById(R.id.potencia_press);
        velocidad_press= (TextView) findViewById(R.id.velocidad_press);
        peso_press= (TextView) findViewById(R.id.peso_press);

        date = getIntent().getStringExtra("Date");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserId = mFirebaseUser.getUid();

        if (mDatabase.child("users").child(mUserId).child(date) == null) {

        }
        mDatabase.child("users").child(mUserId).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child: children
                     ) {
                    pepe = child.getKey();

                    Iterable<DataSnapshot> children1 = child.getChildren();
                    if (child.getKey().equals("Resumen Press de banca")) {
                        exercise = child.getValue(Ex.class);
                        potencia_press.setText("Potencia máxima: "+Float.toString(exercise.getMax_power()));
                        velocidad_press.setText("Velocidad máxima: "+Float.toString(exercise.getMax_velocity()));
                        series_press.setText("Series: "+Float.toString(exercise.getSeries()));
                        peso_press.setText("Peso: "+Float.toString(exercise.getPeso()));


                    }

                    if (child.getKey().equals("Resumen Peso muerto")) {
                        exercise = child.getValue(Ex.class);
                        potencia_peso.setText("Potencia máxima: "+Float.toString(exercise.getMax_power()));
                        velocidad_peso.setText("Velocidad máxima: "+Float.toString(exercise.getMax_velocity()));
                        series_peso.setText("Series: "+Float.toString(exercise.getSeries()));
                        peso_Peso.setText("Peso: "+Float.toString(exercise.getPeso()));


                    }

                    if (child.getKey().equals("Resumen Remo")) {
                        exercise = child.getValue(Ex.class);
                        potencia_remo.setText("Potencia máxima: "+Float.toString(exercise.getMax_power()));
                        velocidad_remo.setText("Velocidad máxima: "+Float.toString(exercise.getMax_velocity()));
                        series_remo.setText("Series: "+Float.toString(exercise.getSeries()));
                        peso_remo.setText("Peso: "+Float.toString(exercise.getPeso()));


                    }

                    if (child.getKey().equals("Resumen Sentadillas")) {
                        exercise = child.getValue(Ex.class);
                        potencia_sentadillas.setText("Potencia máxima: "+Float.toString(exercise.getMax_power()));
                        velocidad_sentadillas.setText("Velocidad máxima: "+Float.toString(exercise.getMax_velocity()));
                        series_sentadillas.setText("Series: "+Float.toString(exercise.getSeries()));
                        peso_sentadillas.setText("Peso: "+Float.toString(exercise.getPeso()));

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //mDatabase.child("users").child(mUserId).child(strDate).child("velocity").setValue(velocity);
        //Query query = mDatabase.child("users").child(mUserId).child(date).equalTo(0);



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
