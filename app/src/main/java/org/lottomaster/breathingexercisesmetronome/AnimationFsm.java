package org.lottomaster.breathingexercisesmetronome;

import android.view.animation.AlphaAnimation;
import android.widget.TextView;

/**
 * Created by Rodion on 02.11.2014.
 */
public final class AnimationFsm implements AnimationMachine{

    private int animationInternalCounter = 0;
    private int animationState = AnimationStates.STATE_INIT;
    private TextView textViewCounter;
    private short exerciseMax = 0;
    public short exerciseCounter = 0;
    public short touchCounter = 0;

    public AnimationFsm(AnimationParameters params) {
        this.textViewCounter = params.textView;
    }

    public void RunAnimation() {

        switch(this.animationState){

            case AnimationStates.STATE_INIT:
                GoPause();
                break;

            case AnimationStates.STATE_PAUSE_SINGLE:

                if (this.animationInternalCounter > AnimationTimes.TIME_PAUSE_LARGE) {
                    GoPulse();
                }

                break;

            case AnimationStates.STATE_PULSE_SINGLE:

                if (this.animationInternalCounter > AnimationTimes.TIME_PULSE_LARGE) {
                    GoPause();
                }
                break;

            case AnimationStates.STATE_EXIT:
                break;

        }

        this.animationInternalCounter++;
    }

    private void GoPulse() {
        this.textViewCounter.setBackgroundResource(R.drawable.ligthcircle);
        AlphaAnimation anim = new AlphaAnimation(0.4f,1.0f);
        anim.setDuration(AnimationTimes.TIME_PAUSE_LARGE);
        this.textViewCounter.startAnimation(anim);
        this.exerciseCounter++;
        this.textViewCounter.setText(String.valueOf(this.exerciseCounter));
        this.animationState = AnimationStates.STATE_PULSE_SINGLE;
        this.animationInternalCounter = 0;
    }

    private void GoPause() {
        this.textViewCounter.setBackgroundResource(R.drawable.bigcircle);
        this.animationState = AnimationStates.STATE_PAUSE_SINGLE;
        this.animationInternalCounter = 0;
    }

}
