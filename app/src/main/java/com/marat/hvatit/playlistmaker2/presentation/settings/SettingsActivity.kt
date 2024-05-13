package com.marat.hvatit.playlistmaker2.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.marat.hvatit.playlistmaker2.R
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "SettingsActivity"


class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var buttonSwitchTheme: SwitchCompat

    companion object {
        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SettingsActivity::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<View>(R.id.back)
        buttonSwitchTheme = findViewById<SwitchCompat>(R.id.bswitch)
        val buttonShare = findViewById<LinearLayout>(R.id.lltwo)
        val buttonSupport = findViewById<LinearLayout>(R.id.llthree)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.llfour)

        buttonShare.setOnClickListener { viewModel.createIntent(ActionFilter.SHARE) }
        buttonSupport.setOnClickListener { viewModel.createIntent(ActionFilter.SUPPORT) }
        buttonUserAgreement.setOnClickListener { viewModel.createIntent(ActionFilter.USERAGREEMENT) }
        //.....................................................

        buttonBack.setOnClickListener {
            onBackPressed()
        }

        buttonSwitchTheme.setOnCheckedChangeListener { _, isChecked ->
            switchTheme(isChecked)
        }
        viewModel.getLoadingLiveData().observe(this) {
            switchTheme(it)
        }

    }

    private fun switchTheme(isChecked: Boolean) {
        buttonSwitchTheme.isChecked = isChecked
        val mode =
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.storeMode(isChecked)
    }

}



