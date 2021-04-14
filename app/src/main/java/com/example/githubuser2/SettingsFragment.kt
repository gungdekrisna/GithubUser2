package com.example.githubuser2

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var NOTIFICATION: String
    private lateinit var notificationPreference: SwitchPreference
    private lateinit var reminderReceiver: ReminderReceiver

    companion object {
        private const val REMINDER_TIME = "09:00"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        init()
        setPreference()
        reminderReceiver = ReminderReceiver()
    }

    private fun init() {
        NOTIFICATION = "notification"
        notificationPreference = findPreference<SwitchPreference> (NOTIFICATION) as SwitchPreference
    }

    private fun setPreference() {
        val sh = preferenceManager.sharedPreferences
        notificationPreference.isChecked = sh.getBoolean(NOTIFICATION, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == NOTIFICATION) {
            notificationPreference.isChecked = sharedPreferences.getBoolean(NOTIFICATION, false)
            if (notificationPreference.isChecked){
                val repeatTime = REMINDER_TIME
                val title = resources.getString(R.string.daily_reminder_title)
                val message = resources.getString(R.string.daily_reminder_message)
                reminderReceiver.setReminder(requireContext(),
                    repeatTime, title, message)
            } else {
                reminderReceiver.cancelReminder(requireContext())
            }
        }
    }
}