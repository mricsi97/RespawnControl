package hu.respawncontrol.view.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import hu.respawncontrol.R;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
