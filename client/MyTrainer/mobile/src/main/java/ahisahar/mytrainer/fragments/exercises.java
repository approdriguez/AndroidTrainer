package ahisahar.mytrainer.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ahisahar.mytrainer.R;
import ahisahar.mytrainer.descriptors.bench_press_descriptor;
import ahisahar.mytrainer.descriptors.deadlift_descriptor;
import ahisahar.mytrainer.descriptors.row_descriptor;
import ahisahar.mytrainer.descriptors.squat_descriptor;

public class exercises extends Fragment {


    private View mContent;


    public static Fragment newInstance() {
        Fragment frag = new exercises();

        Bundle args = new Bundle();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_exercises, container, false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView image1 = (ImageView) view.findViewById(R.id.exercise_photo);
        ImageView image2 = (ImageView) view.findViewById(R.id.exercise_photo1);
        ImageView image3 = (ImageView) view.findViewById(R.id.exercise_photo2);
        ImageView image4 = (ImageView) view.findViewById(R.id.exercise_photo3);
        //ImageView image5 = (ImageView) view.findViewById(R.id.exercise_photo4);

        TextView name1 = (TextView) view.findViewById(R.id.exercise_name);
        TextView name2 = (TextView) view.findViewById(R.id.exercise_name1);
        TextView name3 = (TextView) view.findViewById(R.id.exercise_name2);
        TextView name4 = (TextView) view.findViewById(R.id.exercise_name3);
        TextView name5 = (TextView) view.findViewById(R.id.exercise_name4);
        image1.setImageResource(R.drawable.bench0);
        name1.setText("Press de banca");
        image2.setImageResource(R.drawable.squat0);
        name2.setText("Sentadillas");
        image3.setImageResource(R.drawable.dead0);
        name3.setText("Peso muerto");
        image4.setImageResource(R.drawable.row);
        name4.setText("Remo");
        // initialize views

        //click listener

        image1.setOnClickListener(imgButtonHandler1);
        image2.setOnClickListener(imgButtonHandler2);
        image3.setOnClickListener(imgButtonHandler3);
        image4.setOnClickListener(imgButtonHandler4);
        mContent = view.findViewById(R.id.fragment_content);





    }

    View.OnClickListener imgButtonHandler1 = new View.OnClickListener() {

        public void onClick(View v) {

            Intent intent = new Intent(getContext(),bench_press_descriptor.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    };

    View.OnClickListener imgButtonHandler2 = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(getContext(), squat_descriptor.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
    };

    View.OnClickListener imgButtonHandler3 = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(getContext(), deadlift_descriptor.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
    };

    View.OnClickListener imgButtonHandler4 = new View.OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(getContext(), row_descriptor.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
    };



    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }



}


