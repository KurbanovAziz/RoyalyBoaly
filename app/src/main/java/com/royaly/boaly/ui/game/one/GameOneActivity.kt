package com.royaly.boaly.ui.game.one

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.royaly.boaly.R
import com.royaly.boaly.databinding.ActivityGameOneBinding
import com.royaly.boaly.ui.MenuActivity
import com.royaly.boaly.ui.MenuActivity.Companion.KEY_BALANCE
import com.royaly.boaly.utils.showToast
import kotlin.random.Random

class GameOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameOneBinding
    private var balance: Double = 0.0
    private var bet = 10.0

    private val random = Random

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        balance = intent.getDoubleExtra(KEY_BALANCE, 0.0)

        update()
        initListener()
    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            setResult(
                RESULT_OK, Intent().putExtra(
                    MenuActivity.KEY_NEW_BALANCE, balance
                )
            )
            finish()
        }

        binding.btnPlay.setOnClickListener {
            if (balance > 0 && bet <= balance) {
                spinSlots()
            } else {
                showToast(getString(R.string.not_enough_money))
            }
        }


        binding.btnBetPlus.setOnClickListener {
            if (bet < balance) {
                bet += 10
            }
            update()
        }
        binding.btnBetMinus.setOnLongClickListener {
            if (bet != 10.0) {
                bet = 10.0
            }
            return@setOnLongClickListener false
        }

        binding.btnBetMinus.setOnClickListener {
            if (bet != 10.0) {
                bet -= 10
            }
            update()
        }
    }

    private fun update() {
        binding.tvBalance.text = getString(R.string.balance, balance.toInt())
        binding.tvBet.text = bet.toInt().toString()
    }


    private fun spinSlots() {
        val icons = listOf(
            R.drawable.ic_game1_1, R.drawable.ic_game1_2, R.drawable.ic_game1_3,
            R.drawable.ic_game1_4, R.drawable.ic_game1_5, R.drawable.ic_game1_6,
            R.drawable.ic_game1_7, R.drawable.ic_game1_8
        )

        val slot1Icons = listOf(
            icons[random.nextInt(icons.size)],
            icons[random.nextInt(icons.size)],
            icons[random.nextInt(icons.size)]
        )
        val slot2Icons = listOf(
            icons[random.nextInt(icons.size)],
            icons[random.nextInt(icons.size)],
            icons[random.nextInt(icons.size)]
        )
        val slot3Icons = listOf(
            icons[random.nextInt(icons.size)],
            icons[random.nextInt(icons.size)],
            icons[random.nextInt(icons.size)]
        )

        val animation = AnimationUtils.loadAnimation(this, R.anim.slot_animation)
        binding.column11.startAnimation(animation)
        binding.column12.startAnimation(animation)
        binding.column13.startAnimation(animation)
        binding.column21.startAnimation(animation)
        binding.column22.startAnimation(animation)
        binding.column23.startAnimation(animation)
        binding.column31.startAnimation(animation)
        binding.column32.startAnimation(animation)
        binding.column33.startAnimation(animation)

        var winMultiplier = 0.0

        Handler(Looper.getMainLooper()).postDelayed({

            binding.column11.setImageResource(slot1Icons[0])
            binding.column12.setImageResource(slot1Icons[1])
            binding.column13.setImageResource(slot1Icons[2])

            binding.column21.setImageResource(slot2Icons[0])
            binding.column22.setImageResource(slot2Icons[1])
            binding.column23.setImageResource(slot2Icons[2])

            binding.column31.setImageResource(slot3Icons[0])
            binding.column32.setImageResource(slot3Icons[1])
            binding.column33.setImageResource(slot3Icons[2])

        if (slot1Icons[0] == slot2Icons[0] && slot2Icons[0] == slot3Icons[0]) {
            winMultiplier = getMultiplier(slot1Icons[0])
        } else if (slot1Icons[1] == slot2Icons[1] && slot2Icons[1] == slot3Icons[1]) {
            winMultiplier = getMultiplier(slot1Icons[1])
        } else if (slot1Icons[2] == slot2Icons[2] && slot2Icons[2] == slot3Icons[2]) {
            winMultiplier = getMultiplier(slot1Icons[2])
        } else if (slot1Icons[0] == slot2Icons[1] && slot2Icons[1] == slot3Icons[2]) {
            winMultiplier = getMultiplier(slot1Icons[0]) * getMultiplier(slot2Icons[1]) *
                    getMultiplier(slot3Icons[2])
        } else if (slot1Icons[2] == slot2Icons[1] && slot2Icons[1] == slot3Icons[0]) {
            winMultiplier = getMultiplier(slot1Icons[2]) * getMultiplier(slot2Icons[1]) *
                    getMultiplier(slot3Icons[0])
        } else if (slot1Icons[0] == slot1Icons[1] && slot1Icons[1] == slot1Icons[2]) {
            winMultiplier = getMultiplier(slot1Icons[0])
        } else if (slot2Icons[0] == slot2Icons[1] && slot2Icons[1] == slot2Icons[2]) {
            winMultiplier = getMultiplier(slot2Icons[0])
        } else if (slot3Icons[0] == slot3Icons[1] && slot3Icons[1] == slot3Icons[2]) {
            winMultiplier = getMultiplier(slot3Icons[0])
        }
        },500)

        val payout = if (winMultiplier > 0.0) bet * winMultiplier else -bet

        balance += payout
        update()
    }


    private fun getMultiplier(iconResId: Int): Double {
        return when (iconResId) {
            R.drawable.ic_game1_1 -> 0.5
            R.drawable.ic_game1_2 -> 1.0
            R.drawable.ic_game1_3 -> 1.5
            R.drawable.ic_game1_4 -> 1.5
            R.drawable.ic_game1_5 -> 2.0
            R.drawable.ic_game1_6 -> 2.5
            R.drawable.ic_game1_7 -> 3.0
            R.drawable.ic_game1_8 -> 5.0
            else -> 0.0
        }
    }
}