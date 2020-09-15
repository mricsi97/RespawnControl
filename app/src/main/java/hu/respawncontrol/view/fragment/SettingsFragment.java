package hu.respawncontrol.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import hu.respawncontrol.R;
import hu.respawncontrol.helper.MusicManager;

public class SettingsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        getActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.settings, new SettingsPreferenceFragment())
        .commit();

        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("music_toggle")) {
            boolean musicEnabled = sharedPreferences.getBoolean(key, true);
            if(musicEnabled) {
                MusicManager.getInstance(getActivity()).startMusic();
            } else {
                MusicManager.getInstance(getActivity()).stopMusic();
            }
        } else if(key.equals("music_volume")) {
            MusicManager.getInstance(getActivity()).changeVolume();
        }
    }
}
