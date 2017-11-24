package ahisahar.mytrainer.selectors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ahisahar.mytrainer.R;
import ahisahar.mytrainer.descriptors.bench_press_descriptor;
import ahisahar.mytrainer.descriptors.deadlift_descriptor;
import ahisahar.mytrainer.descriptors.row_descriptor;
import ahisahar.mytrainer.descriptors.squat_descriptor;

public class power_select_exercise extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_select_exercise);
        Button deadlift = (Button) findViewById(R.id.deadlift);
        Button bench = (Button) findViewById(R.id.bench);
        Button squat = (Button) findViewById(R.id.squat);
        Button row = (Button) findViewById(R.id.row);
        deadlift.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadDeadliftView();
            }
        });
        bench.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadBenchPress();
            }
        });
        squat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadSquatView();
            }
        });
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadRowView();
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private void loadDeadliftView() {
        Intent intent = new Intent(this, deadlift_descriptor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadBenchPress() {
        Intent intent = new Intent(this, bench_press_descriptor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadSquatView() {
        Intent intent = new Intent(this, squat_descriptor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadRowView() {
        Intent intent = new Intent(this, row_descriptor.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
