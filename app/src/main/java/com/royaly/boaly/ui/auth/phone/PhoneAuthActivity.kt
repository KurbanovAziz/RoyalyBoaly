package com.royaly.boaly.ui.auth.phone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
        binding.edAuth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ccp.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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