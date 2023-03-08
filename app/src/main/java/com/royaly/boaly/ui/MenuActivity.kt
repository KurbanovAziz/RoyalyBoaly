package com.royaly.boaly.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.royaly.boaly.databinding.ActivityMenuBinding
import com.royaly.boaly.ui.game.one.GameOneActivity
import com.royaly.boaly.ui.game.two.GameTwoActivity
import com.royaly.boaly.ui.game.bonus.BonusGameActivity
import com.royaly.boaly.ui.settings.SettingsActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var balance: Double = 500.0
    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val newBalance = result.data?.getDoubleExtra(KEY_NEW_BALANCE, 0.0)
                if (newBalance != null) {
                    balance = newBalance
                    if (newBalance < 10){
                        balance = 500.0
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() {
        binding.btnGameOne.setOnClickListener {
            val intent = Intent(this@MenuActivity, GameOneActivity::class.java)
            intent.putExtra(KEY_BALANCE, balance)
            result.launch(intent)
        }
        binding.btnGameTwo.setOnClickListener {
            val intentTwo = Intent(this@MenuActivity, GameTwoActivity::class.java)
            intentTwo.putExtra(KEY_BALANCE, balance)
            result.launch(intentTwo)
        }
        binding.btnBonusGame.setOnClickListener {
            val intentBonus = Intent(this@MenuActivity, BonusGameActivity::class.java)
            result.launch(intentBonus)
        }
        binding.btnSettings.setOnClickListener {
            val intentSettings = Intent(this@MenuActivity, SettingsActivity::class.java)
            result.launch(intentSettings)
        }
    }

    companion object {
        const val KEY_BALANCE = "balance"
        const val KEY_NEW_BALANCE = "newBalance"

    }
}