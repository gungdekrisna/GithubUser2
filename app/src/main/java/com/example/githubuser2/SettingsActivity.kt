package com.example.githubuser2

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var lyChangeLanguage : LinearLayout
    private lateinit var tvCurrentLanguage : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = resources.getString(R.string.settings)

        supportFragmentManager.beginTransaction()
            .replace(R.id.notification_frame, SettingsFragment())
            .commit();

        lyChangeLanguage = findViewById(R.id.change_language)
        tvCurrentLanguage = findViewById(R.id.tv_current_language)

        val currentLanguage = Locale.getDefault().getDisplayLanguage()
        tvCurrentLanguage.setText(currentLanguage)

        lyChangeLanguage.setOnClickListener {
            val settings = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(settings)
        }
    }
}