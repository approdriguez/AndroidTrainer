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

public class squat_descriptor extends AppCompatActivity {

    CarouselView carouselView;
    Button launch;
    int[] sampleImages = {R.drawable.squat0, R.drawable.squat1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat_descriptor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        launch = (Button) findViewById(R.id.start);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        //super.onBackPressed();
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
