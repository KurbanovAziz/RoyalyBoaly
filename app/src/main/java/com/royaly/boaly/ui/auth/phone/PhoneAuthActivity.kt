package com.royaly.boaly.ui.auth.phone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.royaly.boaly.R
import com.royaly.boaly.data.Pref
import com.royaly.boaly.databinding.ActivityPhoneAuthBinding
import com.royaly.boaly.ui.menu.MenuActivity
import com.royaly.boaly.utils.showToast
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

class PhoneAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneAuthBinding
    private lateinit var sharedPreferences: Pref
    private lateinit var phoneNumberUtil: PhoneNumberUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = Pref(this)

        phoneNumberUtil = PhoneNumberUtil.createInstance(this)

        initListener()

        if (sharedPreferences.isAuthSeen()) {
            goToMenu()
        }
    }

    private fun initListener() {
        binding.btnSignUp.setOnClickListener {
            authPhone()
        }
    }

    private fun authPhone() {

        val phone = binding.edAuth.text.toString()
        val countryCode = binding.ccp.selectedCountryCode
        if (phone.isNotEmpty() && countryCode.isNotEmpty()) {
            val numberProto = try {
                phoneNumberUtil.parse(phone, binding.ccp.selectedCountryNameCode)
            } catch (e: NumberParseException) {
                null
            }
            if (numberProto != null && phoneNumberUtil.isValidNumber(numberProto)) {
                phoneNumberUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164)
                sharedPreferences.setAuthSeen(true)
                goToMenu()
            } else {
                showToast(getString(R.string.please_enter_a_valid_phone))
            }
        }
        binding.ccp.registerCarrierNumberEditText(binding.edAuth)
    }

    private fun goToMenu() {
        Intent(this, MenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}