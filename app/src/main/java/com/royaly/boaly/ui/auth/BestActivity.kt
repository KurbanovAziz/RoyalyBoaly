package com.royaly.boaly.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.royaly.boaly.R
import com.royaly.boaly.data.Pref
import com.royaly.boaly.databinding.ActivityMainBinding
import com.royaly.boaly.ui.menu.MenuActivity
import com.royaly.boaly.ui.auth.email.EmailAuthActivity
import com.royaly.boaly.ui.auth.phone.PhoneAuthActivity
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class BestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: Pref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = Pref(this)

        if (pref.isAuthSeen()) {
            Intent(this, MenuActivity::class.java).apply {
                startActivity(this)
            }
        }
        initClicker()
    }

    private fun initClicker() {
        binding.btnEmail.setOnClickListener {
            Intent(this, EmailAuthActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.btnPhone.setOnClickListener {
            Intent(this, PhoneAuthActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.btnAnonymous.setOnClickListener {
            Intent(this, MenuActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }


}