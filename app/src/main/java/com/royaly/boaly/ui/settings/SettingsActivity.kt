package com.royaly.boaly.ui.settings

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.royaly.boaly.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}