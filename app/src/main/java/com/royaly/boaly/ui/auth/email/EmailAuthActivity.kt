package com.royaly.boaly.ui.auth.email

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.royaly.boaly.R
import com.royaly.boaly.data.Pref
import com.royaly.boaly.databinding.ActivityEmailAuthBinding
import com.royaly.boaly.ui.menu.MenuActivity
import com.royaly.boaly.utils.showToast
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

class EmailAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailAuthBinding
    private lateinit var sharedPreferences: Pref
    private lateinit var phoneNumberUtil: PhoneNumberUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = Pref(this)

        phoneNumberUtil = PhoneNumberUtil.createInstance(this)

        initClicker()

        if (sharedPreferences.isAuthSeen()) {
            goToMenu()
            return
        }

    }

    private fun initClicker() {

        binding.btnSignUp.setOnClickListener {
            authEmail()
        }
    }

    private fun authEmail() {
        val email = binding.edAuth.text.toString()

        if (isValidEmail(email)) {
            sharedPreferences.setAuthSeen(true)
            goToMenu()
        } else {
            showToast(getString(R.string.please_enter_a_valid_email))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Patterns.EMAIL_ADDRESS.toRegex()
        return email.matches(emailRegex)
    }

    private fun goToMenu() {
        Intent(this, MenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}