package ahisahar.mytrainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class bench_press_descriptor extends AppCompatActivity {

    CarouselView carouselView;
    Button launch;

    int[] sampleImages = {R.drawable.bench0, R.drawable.bench1, R.drawable.bench2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat_descriptor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        //super.onBackPressed();
        launch = (Button) findViewById(R.id.start);

        launch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchExercise();
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void launchExercise(){
        Intent intent = new Intent(this, exercise.class);
        intent.putExtra("id",0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
