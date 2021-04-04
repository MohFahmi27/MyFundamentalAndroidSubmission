package com.mfahmi.myfundamentalandroid.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.alarm.AlarmReceiver
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.*

class AppPreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var NOTIFICATION_KEY: String
    private lateinit var LANGUAGE_KEY: String

    companion object {
        private const val PREFERENCE_NAME = "github_setting_pref"
    }

    private lateinit var mySharedPreferences: SharedPreferences
    private lateinit var dailyReminder: SwitchPreference
    private lateinit var languagePreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        mySharedPreferences =
            requireContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        init()
        dailyReminder.isChecked = mySharedPreferences.getBoolean(PREFERENCE_NAME, false)
        languagePreference.summary = Locale.getDefault().displayLanguage
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when (preference!!.key) {
            LANGUAGE_KEY -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                languagePreference.summary = Locale.getDefault().displayLanguage
                true
            }
            NOTIFICATION_KEY -> {
                val alarmReceiver = AlarmReceiver()
                if (dailyReminder.isChecked) {
                    alarmReceiver.setDailyReminder(
                        requireContext(),
                        resources.getString(R.string.content_text_notification)
                    )
                    showMessage(resources.getString(R.string.message_daily_reminder_on))
                    mySharedPreferences.edit { putBoolean(NOTIFICATION_KEY, true) }
                } else {
                    alarmReceiver.cancelAlarm(requireContext())
                    showMessage(resources.getString(R.string.message_daily_reminder_off))
                    mySharedPreferences.edit { putBoolean(NOTIFICATION_KEY, true) }
                }
                true
            }
            else -> false
        }
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(
            context,
            message,
            FancyToast.LENGTH_LONG,
            FancyToast.INFO,
            true
        ).show()
    }

    private fun init() {
        NOTIFICATION_KEY = resources.getString(R.string.notification)
        LANGUAGE_KEY = resources.getString(R.string.language)
        dailyReminder = findPreference<SwitchPreference>(NOTIFICATION_KEY) as SwitchPreference
        languagePreference = findPreference<Preference>(LANGUAGE_KEY) as Preference
    }
}