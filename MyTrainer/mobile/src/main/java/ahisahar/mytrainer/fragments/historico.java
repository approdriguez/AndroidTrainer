package ahisahar.mytrainer.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ahisahar.mytrainer.R;
import ahisahar.mytrainer.historical.history_exercises;

public class historico extends Fragment {


    private View mContent;
    int day, month, year;

    public static Fragment newInstance() {
        Fragment frag = new historico();

        Bundle args = new Bundle();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_historico, container, false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker2);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                //Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                //Lanzamos el new intent con los parametros del DatePicker



                SimpleDateFormat dateFormatter =  new SimpleDateFormat("yyyy-MM-dd");

                //String strDate = dateFormatter.format(calendar.getTime());
                String strDate = Integer.toString(year) +"-"+Integer.toString(month+1) +"-"+Integer.toString(dayOfMonth);
                Log.d("date",strDate);
                Intent intent = new Intent(getContext(), history_exercises.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Date",strDate);
                startActivity(intent);

            }
        });

        mContent = view.findViewById(R.id.fragment_content);





    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }



}



