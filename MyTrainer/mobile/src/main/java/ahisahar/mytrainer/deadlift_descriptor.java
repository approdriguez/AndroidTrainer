package ahisahar.mytrainer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import static ahisahar.mytrainer.R.styleable.Toolbar;

public class deadlift_descriptor extends AppCompatActivity {

    CarouselView carouselView;

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




}
