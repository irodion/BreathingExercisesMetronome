package org.lottomaster.breathingexercisesmetronome;

/**
 * Factory for creation custom counter animations
 * Created by Rodion on 03.11.2014.
 */
public class AnimationFactory {

    public static AnimationMachine CreateAnimation(AnimationParameters params) {

        switch (params.Type) {

            case AnimationMode.ANIMATION_SINGLE:
                return new AnimationFsmSingle(params);


            case AnimationMode.ANIMATION_DOUBLED:
                break;

            case AnimationMode.ANIMATION_SMALL:
                return new AnimationFsmSmall(params);


            default:


        }
        return null;
    }
}
