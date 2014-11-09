package org.lottomaster.breathingexercisesmetronome;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;


/**
 * Created by Rodion on 20.10.2014.
 */
public class PrefFragment extends PreferenceActivity {

    private static int prefAct = R.xml.preference;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            onCreatePreferenceActivity();
        } else {
            onCreatePreferenceFragment();
        }

        try {
            EditTextPreference editTextPreferenceExercises =
                    (EditTextPreference)findPreference("ExercisesInTouch");
            if (editTextPreferenceExercises != null) {
                EditText editTextExercises = editTextPreferenceExercises.getEditText();
                editTextExercises.setKeyListener(new DigitsKeyListener());
            }

            EditTextPreference editTextPreferenceTouch =
                    (EditTextPreference)findPreference("TouchMax");
            if (editTextPreferenceTouch != null) {
                EditText editTextTouch = editTextPreferenceTouch.getEditText();
                editTextTouch.setKeyListener(new DigitsKeyListener());
            }

        }
        catch (Exception ex) {

            EditText editTextExercises = ((EditTextPreference)
                    findPreference("ExercisesInTouch")).getEditText();
        }



    }

    /**
     * Wraps legacy {@link #onCreate(Bundle)} code for Android < 3 (i.e. API lvl
     * < 11).
     */
    @SuppressWarnings("deprecation")
    private void onCreatePreferenceActivity() {
        addPreferencesFromResource(R.xml.preference);
    }

    /**
     * Wraps {@link #onCreate(Bundle)} code for Android >= 3 (i.e. API lvl >=
     * 11).
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void onCreatePreferenceFragment() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }
}
