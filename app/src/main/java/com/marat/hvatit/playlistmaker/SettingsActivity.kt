package com.marat.hvatit.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import java.util.concurrent.Flow

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val settingsGetIntent = intent
        val gettext = settingsGetIntent.getStringExtra("messege")
        val lldarktheme : LinearLayout = findViewById<LinearLayout>(R.id.llone)
        lldarktheme.setOnClickListener{
            Toast.makeText(this@SettingsActivity,gettext, Toast.LENGTH_SHORT).show()
        }

    }
}