package org.lottomaster.breathingexercisesmetronome;

/**
 * Created by Rodion on 03.11.2014.
 */
public class AnimationFactory {

    public static AnimationMachine CreateAnimation(AnimationParameters params) {

        if (params.type == AnimationMode.ANIMATION_SINGLE) {
            return new AnimationFsm(params);
        }
        else
            return null;
    }
}
