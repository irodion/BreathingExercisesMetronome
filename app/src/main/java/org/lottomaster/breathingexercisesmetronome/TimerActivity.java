package org.lottomaster.breathingexercisesmetronome;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


public class TimerActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window;
        window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);


    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TextView tvMainCounter;
        private TextView tvExercise;

        Context myContext;

        private int exerciseCounter;
        private int exerciseLevel = 32;
        private int metronomeState = 0;
        final private Handler handlerTimer = new Handler();

        /**
         * Setup circle on touch listener
         */
        private void SetCirclesOnClickListener() {

            if ((this.tvMainCounter != null) && (this.tvExercise != null)) {
                this.tvMainCounter.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        OnCircleTapEvent();
                    }
                });
            }

        }

        protected void GetViewHandlers() {

            this.tvMainCounter = (TextView) getActivity().findViewById(R.id.MainRythm);
            this.tvExercise = (TextView) getActivity().findViewById(R.id.ExerciseCounter);
        }

        private void OnCircleTapEvent() {

            this.metronomeState = 0;
            handlerTimer.postDelayed(UpdateExerciseTimer,0);

        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            this.myContext = activity.getApplicationContext();
        }

        @Override
        public void onResume() {
            super.onResume();
            ShowPleaseTapToast();
            GetViewHandlers();
            SetCirclesOnClickListener();
        }

        private void ShowPleaseTapToast() {

            int duration = Toast.LENGTH_LONG;

            if (this.myContext != null) {
                String text;
                text = getString(R.string.tap_to_start);
                Toast toast;
                toast = Toast.makeText(this.myContext, text, duration);
                toast.show();
            }
        }

        private Runnable UpdateExerciseTimer = new Runnable() {
            @Override
            public void run() {


                    tvMainCounter.setBackgroundResource(R.drawable.bigcircle);
                    //// основной отсчет
                    exerciseCounter++;
                    if(exerciseCounter >= exerciseLevel) {
                        exerciseCounter = 0;
                    }
                    try {
                        tvMainCounter.setText(String.valueOf(exerciseCounter));
                        tvMainCounter.setBackgroundResource(R.drawable.ligthcircle);
                        AlphaAnimation anim = new AlphaAnimation(0.4f,1.0f);
                        anim.setDuration(800);
                        tvMainCounter.startAnimation(anim);

                    }catch (Exception ex) {
                        exerciseCounter = 0;
                    }



                metronomeState++;
                handlerTimer.postDelayed(this, 1000);
            }
        };



    }




}
