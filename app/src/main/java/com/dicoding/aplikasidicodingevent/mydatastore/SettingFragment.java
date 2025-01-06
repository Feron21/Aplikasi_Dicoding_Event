package com.dicoding.aplikasidicodingevent.mydatastore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.dicoding.aplikasidicodingevent.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingFragment extends Fragment {

    private SwitchMaterial switchTheme;

    // Konstruktor kosong diperlukan
    public SettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi SwitchMaterial
        switchTheme = view.findViewById(R.id.switch_theme);

        // Inisialisasi DataStore
        RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(requireContext(), "settings").build();
        SettingPreferences pref = SettingPreferences.getInstance(dataStore);

        // Inisialisasi ViewModel
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelFactory(pref)).get(MainViewModel.class);
        mainViewModel.getThemeSettings().observe(getViewLifecycleOwner(), isDarkModeActive -> {
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                switchTheme.setChecked(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                switchTheme.setChecked(false);
            }
        });

        // Menyimpan preferensi tema ketika switch diubah
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainViewModel.saveThemeSetting(isChecked);
        });
    }
}
