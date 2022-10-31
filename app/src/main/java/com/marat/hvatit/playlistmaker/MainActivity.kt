package com.marat.hvatit.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintwo)

        val buttonSearch = findViewById<Button>(R.id.button_bigOne)
        buttonSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        //................................................................................
        val buttonMedialib = findViewById<Button>(R.id.button_bigTwo)
        buttonMedialib.setOnClickListener {
            val medialibIntent = Intent(this, MedialibraryActivity::class.java)
            startActivity(medialibIntent)
        }
        //................................................................................
        val buttonSettings = findViewById<Button>(R.id.button_bigThree)
        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            val messege = "Android - это круто!"
            displayIntent.putExtra("messege", messege)
            startActivity(displayIntent)
        }
    }
}