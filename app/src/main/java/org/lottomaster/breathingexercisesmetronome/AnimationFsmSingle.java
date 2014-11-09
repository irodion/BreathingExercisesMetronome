package org.lottomaster.breathingexercisesmetronome;

import android.view.animation.AlphaAnimation;
import android.widget.TextView;

/**
 * Created by Rodion on 04.11.2014.
 */
public final class AnimationFsmSingle extends AnimationFsm implements AnimationMachine{


    public short exerciseMax = 0;

    public AnimationFsmSingle(AnimationParameters params) {

        this.textViewName = params.TextView;
        this.exerciseMax = params.MaxCounterToReset;
    }

    public short RunAnimation() {

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
        return  this.exerciseCounter;
    }

    public void ClearToDefault() {

    }

    private void GoPulse() {
        this.textViewName.setBackgroundResource(R.drawable.ligthcircle);
        AlphaAnimation anim = new AlphaAnimation(0.4f,1.0f);
        anim.setDuration(AnimationTimes.TIME_PAUSE_LARGE);
        this.textViewName.startAnimation(anim);
        this.exerciseCounter++;
        if (this.exerciseCounter > this.exerciseMax) {
            this.exerciseCounter = 0;
        }
        this.textViewName.setText(String.valueOf(this.exerciseCounter));
        this.animationState = AnimationStates.STATE_PULSE_SINGLE;
        this.animationInternalCounter = 0;
    }

    private void GoPause() {
        this.textViewName.setBackgroundResource(R.drawable.bigcircle);
        this.animationState = AnimationStates.STATE_PAUSE_SINGLE;
        this.animationInternalCounter = 0;
    }

}
