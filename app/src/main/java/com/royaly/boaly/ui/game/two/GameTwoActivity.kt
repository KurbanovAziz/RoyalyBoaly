package com.royaly.boaly.ui.game.two

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.royaly.boaly.R
import com.royaly.boaly.databinding.ActivityGameTwoBinding
import com.royaly.boaly.ui.MenuActivity
import com.royaly.boaly.ui.MenuActivity.Companion.KEY_BALANCE
import com.royaly.boaly.ui.MenuActivity.Companion.KEY_NEW_BALANCE
import com.royaly.boaly.utils.showToast
import kotlin.random.Random

class GameTwoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameTwoBinding
    private var balance = 0.0
    private var win: Double = 0.0
    private var bet = 10.0

    private val random = Random

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        balance = intent.getDoubleExtra(KEY_BALANCE, 0.0)
        update()
        initListener()

    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            setResult(
                RESULT_OK, Intent().putExtra(
                    KEY_NEW_BALANCE, balance
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
        binding.tvWin.text = getString(R.string.win, win.toInt())
    }

    private fun spinSlots() {
        val icons = listOf(
            R.drawable.ic_game2_1, R.drawable.ic_game2_2, R.drawable.ic_game2_3,
            R.drawable.ic_game2_4, R.drawable.ic_game2_5, R.drawable.ic_game2_6,
            R.drawable.ic_game2_7
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

        binding.column11.setImageResource(slot1Icons[0])
        binding.column12.setImageResource(slot1Icons[1])
        binding.column13.setImageResource(slot1Icons[2])

        binding.column21.setImageResource(slot2Icons[0])
        binding.column22.setImageResource(slot2Icons[1])
        binding.column23.setImageResource(slot2Icons[2])

        binding.column31.setImageResource(slot3Icons[0])
        binding.column32.setImageResource(slot3Icons[1])
        binding.column33.setImageResource(slot3Icons[2])


        var winMultiplier = 0.0
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

        val payout = if (winMultiplier > 0.0) bet * winMultiplier else -bet

        balance += payout
        win = payout
        update()
    }


    private fun getMultiplier(iconResId: Int): Double {
        return when (iconResId) {
            R.drawable.ic_game2_1 -> 1.0
            R.drawable.ic_game2_2 -> 1.5
            R.drawable.ic_game2_3 -> 1.5
            R.drawable.ic_game2_4 -> 2.0
            R.drawable.ic_game2_5 -> 2.5
            R.drawable.ic_game2_6 -> 3.0
            R.drawable.ic_game2_7 -> 5.0
            else -> 0.0
        }
    }
}