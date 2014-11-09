package org.lottomaster.breathingexercisesmetronome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimerActivity extends ActionBarActivity {



    private SoundPool tickerPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

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
        } else if(id == R.id.action_exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);


    }
    public static class PlaceholderFragment extends Fragment {



        private TextView tvMainCounter;
        private TextView tvExercise;
        private TextView tvClock;
        private boolean running = false;
        private SoundPool tickerPlayer;
        private boolean loadedMetronomeSound = false;
        private boolean loadedDongSound = false;
        private int poolIdTicker = 0;
        private int poolIdDong = 0;
        private int tickerStreamId = 0;
        private short counterExercises = 0;
        private short counterTouches = 0;
        private short timeInExercise = 0;

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

            if (!this.running) {
                AnimationThreadParameters params;
                params = new AnimationThreadParameters();
                params.TextViewExercise = this.tvMainCounter;
                params.TextViewTouch = this.tvExercise;
                params.TimeInExercise = this.timeInExercise;
                params.TypeAnimationExercise = AnimationMode.ANIMATION_SINGLE;
                params.TypeAnimationTouch = AnimationMode.ANIMATION_SMALL;
                //TODO: replace (short)R.integer.exercise_repeats with settings readed
                params.MaxCounterExercise = this.counterExercises;
                params.MaxCounterTouch = this.counterTouches;
                MyAnimationThread animation = new MyAnimationThread(params);
                handlerTimer.postDelayed(animation, 0);
            }
            else {
                //TODO:stop timer
            }


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
            this.tickerPlayer = new SoundPool(2, AudioManager.STREAM_MUSIC,0);

            this.tickerPlayer.setOnLoadCompleteListener( new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int loadedId, int i2) {

                   if(loadedId == poolIdTicker) {
                       loadedMetronomeSound = true;
                   } else
                   if (loadedId == poolIdDong)  {
                       loadedDongSound = true;
                   }
                }
            });

            this.poolIdTicker = this.tickerPlayer.load(myContext,R.raw.metronome,1);
            this.poolIdDong = this.tickerPlayer.load(myContext,R.raw.dong,1);
            LoadPreferences();
        }

        @Override
        public void onResume() {
            super.onResume();
            ShowPleaseTapToast();
            GetViewHandlers();
            SetCirclesOnClickListener();
        }

        @Override
        public void onDestroy() {

            tickerPlayer.autoPause();
            tickerPlayer.release();
            super.onDestroy();
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

        private void LoadPreferences() {

            try {
                SharedPreferences sPreference = PreferenceManager.getDefaultSharedPreferences(this.myContext);
                this.counterExercises = Short.valueOf(sPreference.getString("ExercisesInTouch",getResources().getString(R.string.exercise_repeats)));
                this.counterTouches = Short.valueOf(sPreference.getString("TouchMax", getResources().getString(R.string.exercise_touch)));
                this.timeInExercise = Short.valueOf(sPreference.getString("TimeInExercise", getResources().getString(R.string.time_in_exercise)));
            }
            catch (Exception ex) {
                this.counterExercises = 0;
            }

        }

        private final class MyAnimationThread implements Runnable{

            private AnimationMachine animationSingle;
            private AnimationMachine animationSmall;
            private short exerciseMax;
            private short exerciseOld = 0;
            private short touchOld = 0;
            private Date startTime;

            private String ExerciseTime(Date currentTime) {

                DateFormat dateFormatMy = new SimpleDateFormat("mm:ss");

                Date elapsedTime = new Date(currentTime.getTime() - startTime.getTime());
                return dateFormatMy.format(elapsedTime);
            }

            public  MyAnimationThread(AnimationThreadParameters params) {

                this.exerciseMax = params.MaxCounterExercise;
                this.animationSingle =  AnimationFactory.CreateAnimation(ExtractThreadParametersExercise(params));

                this.animationSmall = AnimationFactory.CreateAnimation(ExtractThreadParametersTouch(params));
                this.startTime = new Date();
            }

            @Override
            public void run()
            {
                tvClock.setText(ExerciseTime(new Date()));

                short currentExercise = this.animationSingle.RunAnimation();

                if (loadedMetronomeSound) {
                    TickerExercise(currentExercise);
                }

                if (currentExercise == this.exerciseMax) {
                    short currentTouch = animationSmall.RunAnimation();
                    if (loadedDongSound) {
                        TickerTouch(currentTouch);
                    }
                }
                else
                {
                    animationSmall.ClearToDefault();
                }

                handlerTimer.postDelayed(this, AnimationTimes.TIME_CLOCK);
            }

            private void TickerExercise(short currentExercise) {

                if (currentExercise != this.exerciseOld) {
                    tickerStreamId = tickerPlayer.play(poolIdTicker, 1.0f, 1.0f, 1, 0, 1.0f);
                    this.exerciseOld = currentExercise;
                }
            }

            private void TickerTouch(short currentTouch) {

                if (this.touchOld != currentTouch) {
                    tickerStreamId = tickerPlayer.play(poolIdDong, 1.0f, 1.0f, 1, 0, 1.0f);
                    this.touchOld = currentTouch;
                }
            }

            private AnimationParameters ExtractThreadParametersExercise(AnimationThreadParameters params){

                AnimationParameters threadParameters = new AnimationParameters();

                threadParameters.TextView = params.TextViewExercise;
                threadParameters.MaxCounterToReset = params.MaxCounterExercise;
                threadParameters.Type = params.TypeAnimationExercise;
                return threadParameters;
            }

            private AnimationParameters ExtractThreadParametersTouch(AnimationThreadParameters params) {

                AnimationParameters threadParameters = new AnimationParameters();

                threadParameters.TextView = params.TextViewTouch;
                threadParameters.MaxCounterToReset = params.MaxCounterTouch;
                threadParameters.Type = params.TypeAnimationTouch;
                return threadParameters;
            }

        }

    }

}
