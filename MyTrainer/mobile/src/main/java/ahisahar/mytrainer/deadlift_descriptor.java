package ahisahar.mytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import static ahisahar.mytrainer.R.styleable.Toolbar;

public class deadlift_descriptor extends AppCompatActivity {

    CarouselView carouselView;
    Button launch;
    int[] sampleImages = {R.drawable.dead0, R.drawable.dead2, R.drawable.dead1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlift_descriptor);
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
        Intent intent = new Intent(this, squat_descriptor.class);
        intent.putExtra("id",2);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
