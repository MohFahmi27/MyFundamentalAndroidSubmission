package com.mfahmi.myfundamentalandroid.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.alarm.AlarmReceiver
import java.util.*

class AppPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var NOTIFICATION_KEY: String
    private lateinit var LANGUAGE_KEY: String

    private lateinit var dailyReminder: SwitchPreference
    private lateinit var languagePreference: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        alarmReceiver = AlarmReceiver()
        dailyReminder.isChecked = preferenceManager.sharedPreferences.getBoolean(NOTIFICATION_KEY, false)
        languagePreference.summary = Locale.getDefault().displayLanguage
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when (preference!!.key) {
            LANGUAGE_KEY -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                languagePreference.summary = Locale.getDefault().displayLanguage
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init() {
        NOTIFICATION_KEY = resources.getString(R.string.notification)
        LANGUAGE_KEY = resources.getString(R.string.language)
        dailyReminder = findPreference<SwitchPreference>(NOTIFICATION_KEY) as SwitchPreference
        languagePreference = findPreference<Preference>(LANGUAGE_KEY) as Preference
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == NOTIFICATION_KEY) {
            sharedPreferences?.let {
                dailyReminder.isChecked = sharedPreferences.getBoolean(NOTIFICATION_KEY, false)
            }
        }
        val dailyReminderState = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NOTIFICATION_KEY, false)
        if (dailyReminderState) {
            alarmReceiver.setDailyReminder(requireContext(),
                    getString(R.string.content_title_notification),
                    getString(R.string.content_text_notification))
        }
        if (!dailyReminderState) {
            alarmReceiver.cancelAlarm(requireContext())
        }
    }

}