package com.mfahmi.myfundamentalandroid.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mfahmi.myfundamentalandroid.R

class AppPreferenceFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}