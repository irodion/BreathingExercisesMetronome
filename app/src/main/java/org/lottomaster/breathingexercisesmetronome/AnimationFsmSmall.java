package org.lottomaster.breathingexercisesmetronome;

/**
 * Created by Rodion on 04.11.2014.
 */

import android.view.animation.AlphaAnimation;

/**
 * Animation FSM for large exercise counter, single step
 * Created by Rodion on 02.11.2014.
 */
public final class AnimationFsmSmall extends AnimationFsm implements AnimationMachine{

    public short exerciseMax = 0;

    public AnimationFsmSmall(AnimationParameters params) {
        this.textViewName = params.TextView;
        exerciseMax = params.MaxCounterToReset;
    }

    public short RunAnimation() {

        switch(this.animationState){

            case AnimationStates.STATE_INIT:
                GoPause();
                this.defaultState = true;
                break;

            case AnimationStates.STATE_PAUSE_SINGLE:

                if (this.animationInternalCounter > AnimationTimes.TIME_PAUSE_LARGE) {
                    GoPulse();
                    this.defaultState = false;
                }

                break;

            case AnimationStates.STATE_PULSE_SINGLE:

                if (this.animationInternalCounter > AnimationTimes.TIME_PULSE_LARGE) {
                    GoPause();
                    this.defaultState = true;
                }
                break;

            case AnimationStates.STATE_EXIT:
                break;

        }

        this.animationInternalCounter++;
        return  this.exerciseCounter;
    }

    public void ClearToDefault() {

        if(!this.defaultState) {
            GoPause();
            this.defaultState = true;
        }
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
