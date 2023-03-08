package com.royaly.boaly.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.royaly.boaly.R
import com.royaly.boaly.databinding.ActivityMainBinding
import com.royaly.boaly.ui.MenuActivity
import com.royaly.boaly.utils.showToast
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class BestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var phoneNumberUtil: PhoneNumberUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        phoneNumberUtil = PhoneNumberUtil.createInstance(this)

        initClicker()

        if (sharedPreferences.getBoolean(IS_AUTH, false)) {
            goToMenu()
            return
        }
    }

    private fun initClicker() {
        binding.btnEmail.setOnClickListener {
            with(binding) {
                mainAuthLayout.visibility = View.INVISIBLE
                authLayout.visibility = View.VISIBLE
                tvTitle.text = getString(R.string.enter_your_email)
                edInput.hint = getString(R.string.email)
                edAuth.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            }

        }
        binding.btnPhone.setOnClickListener {
            with(binding) {
                mainAuthLayout.visibility = View.INVISIBLE
                authLayout.visibility = View.VISIBLE
                tvTitle.text = getString(R.string.enter_your_phone)
                edInput.hint = getString(R.string.phone)
                edAuth.inputType = InputType.TYPE_CLASS_PHONE
                ccp.visibility = View.VISIBLE
            }
        }
        binding.btnSignUp.setOnClickListener {
            if (binding.edInput.hint == getString(R.string.phone)) {
                authPhone()
            } else {
                authEmail()
            }
        }
    }

    private fun authPhone() {

        val phone = binding.edAuth.text.toString()
        if (isValidPhoneNumber(phone)) {
            sharedPreferences.edit().putBoolean(IS_AUTH, true).apply()
            goToMenu()
        } else {
            showToast(getString(R.string.please_enter_a_valid_phone))
        }
    }

    private fun authEmail() {
        val email = binding.edAuth.text.toString()

        if (isValidEmail(email)) {
            sharedPreferences.edit().putBoolean(IS_AUTH, true).apply()
            goToMenu()
        } else {
            showToast(getString(R.string.please_enter_a_valid_email))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Patterns.EMAIL_ADDRESS.toRegex()
        return email.matches(emailRegex)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val countryCode = binding.ccp.selectedCountryCode

        return try {
            val numberProto = phoneNumberUtil.parse(phoneNumber, countryCode)
            phoneNumberUtil.isValidNumber(numberProto)
        } catch (e: NumberParseException) {
            false
        }
    }

    private fun goToMenu() {
        Intent(this, MenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    companion object {
        private const val PREF_NAME = "pref"
        const val IS_AUTH = "isAuthorized"
    }
}