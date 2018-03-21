package ahisahar.mytrainer.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import ahisahar.mytrainer.MainActivity;
import ahisahar.mytrainer.R;
import ahisahar.mytrainer.authentication.LogInActivity;


public class settings extends Fragment {


    private View mContent;
    private Button logout;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public static Fragment newInstance() {
        Fragment frag = new settings();
        Bundle args = new Bundle();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_settings, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views
        mContent = view.findViewById(R.id.fragment_content);
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(imgButtonHandler1);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    View.OnClickListener imgButtonHandler1 = new View.OnClickListener() {

        public void onClick(View v) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            mFirebaseAuth.signOut();
            loadLogInView();


        }
    };

    private void loadLogInView() {
        Intent intent = new Intent(this.getContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}
