package ahisahar.mytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import java.util.ArrayList;
import java.util.List;
import android.support.design.widget.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private PieChart piechart;
    private float[] yData = {};
    private float[] xData = {};
    private Button logout, powerButton;
    GraphView graph;
    private BottomBar mBottomBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();


            ////////////////
            piechart = new PieChart(this);
            piechart = (PieChart) findViewById(R.id.chartactivity);
            graph = (GraphView) findViewById(R.id.graph);


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

        }
        logout = (Button) findViewById(R.id.logoutbutton);


        powerButton = (Button) findViewById(R.id.powerbutton);
        powerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadPowerView();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadLogInView();
            }
        });

    }




    private void addData() {

        List<PieEntry> entries = new ArrayList<>();

        for(int i=0;i<yData.length && i<xData.length ;i++)
            entries.add(new PieEntry(yData[i],xData[i]));


        PieDataSet dataSet = new PieDataSet(entries, "Mi actividad");



        dataSet.setSliceSpace(5);
        dataSet.setSelectionShift(5);

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





}
