package ahisahar.mytrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;


public class home extends Fragment {

    private PieChart piechart;
    private View mContent;
    private float[] yData = {2,3,1,0,1};// nº of exercise each day
    private String[] xData = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};

    public static Fragment newInstance() {
        Fragment frag = new home();
        Bundle args = new Bundle();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views

        piechart = new PieChart(this.getContext());
        piechart = (PieChart) view.findViewById(R.id.chartactivity);
        //mainlayout.addView(piechart);
        //RelativeLayout fragment_content = (RelativeLayout) view.findViewById(R.id.main);
        //fragment_content.removeView(fragment_content.getParent());
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.fragment_content);
        TextView name = (TextView) view.findViewById(R.id.text);
        //parent.removeView(parent);
        parent.removeAllViews();
        //((ViewGroup)fragment_content.getParent()).removeView(fragment_content);
        //fragment_content.removeAllViews();
        parent.addView(piechart);
        parent.addView(name);
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
                //Toast.makeText(home.this, xData[(int) e.getX()]+ " = "+ e.getData() + "%", Toast.LENGTH_SHORT).show();

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
        mContent = view.findViewById(R.id.fragment_content);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

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

}
