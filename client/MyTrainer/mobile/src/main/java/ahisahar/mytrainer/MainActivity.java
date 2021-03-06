package ahisahar.mytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import ahisahar.mytrainer.authentication.LogInActivity;
import ahisahar.mytrainer.fragments.exercises;
import ahisahar.mytrainer.fragments.historico;
import ahisahar.mytrainer.fragments.home;
import ahisahar.mytrainer.fragments.notes;
import ahisahar.mytrainer.fragments.settings;
import ahisahar.mytrainer.selectors.power_select_exercise;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity {

    private int mSelectedItem;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    // bar fragments





    GraphView graph;
    private BottomNavigationView bottomNavigationView;



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
            Fragment frag = home.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, frag, frag.getTag());
            ft.commit();

            mUserId = mFirebaseUser.getUid();

            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.bottom_navigation);


            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return true;
                        }
                    });





// Colors for selected (active) and non-selected items (in color reveal mode).
            ////////////////

            graph = (GraphView) findViewById(R.id.graph);


            //mainlayout.addView(piechart);



        }
        //logout = (Button) findViewById(R.id.logoutbutton);



        /*logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadLogInView();
            }
        });*/

    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    private void selectFragment(MenuItem item) {
        Fragment fragaux = null;

        switch (item.getItemId()) {
            case R.id.exercise:
                fragaux = exercises.newInstance();
                mSelectedItem = item.getItemId();
                updateToolbarText(item.getTitle());
                break;
            case R.id.settings:
                //clearFragmentStack();
                //bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation); bottomNavigationView.setBackgroundColor(Color.RED);
                //bottomNavigationView.setItemBackgroundResource(Color.TRANSPARENT);
                fragaux = settings.newInstance();
                mSelectedItem = item.getItemId();
                updateToolbarText(item.getTitle());
                break;
            case R.id.main:
                clearFragmentStack();
                fragaux = home.newInstance();
                mSelectedItem = item.getItemId();
                updateToolbarText(item.getTitle());
                break;
            case R.id.notes:
                fragaux = notes.newInstance();
                mSelectedItem = item.getItemId();
                updateToolbarText(item.getTitle());
                break;
            case R.id.historial:
                fragaux = historico.newInstance();
                mSelectedItem = item.getItemId();
                updateToolbarText(item.getTitle());
                break;
        }
        if (fragaux != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            View cont = findViewById(R.id.container);
            //cont.ce
            ft.replace(R.id.container, fragaux, fragaux.getTag());
            ft.commit();
        }

    }

    private void clearFragmentStack(){
        android.app.FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fm.popBackStack();
        }
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

    private void loadSettings() {
        Intent intent = new Intent(this, settings.class);
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

        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        mFirebaseAuth.signOut();
        loadLogInView();
    }


}
