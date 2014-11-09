package org.lottomaster.breathingexercisesmetronome;

import android.widget.TextView;

/**
 * Animation FSM class
 * Created by Rodion on 02.11.2014.
 */
public abstract class AnimationFsm{

    protected int animationInternalCounter = 0;
    protected int animationState = AnimationStates.STATE_INIT;
    protected TextView textViewName;
    protected short resetCount = 0;
    protected boolean defaultState = true;
    public short exerciseCounter = 0;


}
