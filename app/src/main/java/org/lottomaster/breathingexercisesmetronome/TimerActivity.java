package org.lottomaster.breathingexercisesmetronome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;

import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


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
            Intent settingsActivity = new Intent(getBaseContext(),
                    PrefFragment.class);
            try{
                startActivity(settingsActivity);
            }catch (Exception ex) {
                return false;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);


    }
    public static class PlaceholderFragment extends Fragment {



        private TextView tvMainCounter;
        private TextView tvExercise;
        private TextView tvClock;
        private Date startTime;
        Context myContext;

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
            this.tvClock = (TextView) getActivity().findViewById(R.id.ExerciseClock);
        }

        private void OnCircleTapEvent() {

            this.startTime = new Date();
            AnimationParameters params;
            params = new AnimationParameters();
            params.textView = this.tvMainCounter;
            params.type = AnimationMode.ANIMATION_SINGLE;
            MyAnimationThread animation = new MyAnimationThread(params);
            handlerTimer.postDelayed(animation,0);

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

        private String ExerciseTime(Date currentTime) {

            DateFormat dateFormatMy = new SimpleDateFormat("HH:mm:ss");
            Date elapsedTime = new Date(currentTime.getTime() - startTime.getTime());
            return dateFormatMy.format(elapsedTime);
        }

        private final class MyAnimationThread implements Runnable{

            private AnimationMachine animation;

            public  MyAnimationThread(AnimationParameters params) {
                animation =  AnimationFactory.CreateAnimation(params);
            }
            @Override
            public void run()
            {
                tvClock.setText(ExerciseTime(new Date()));
                this.animation.RunAnimation();
                handlerTimer.postDelayed(this, AnimationTimes.TIME_CLOCK);
            }
        }

    }

}
