package com.royaly.boaly.ui.game.one

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.royaly.boaly.R
import com.royaly.boaly.databinding.ActivityGameOneBinding
import com.royaly.boaly.ui.menu.MenuActivity
import com.royaly.boaly.ui.menu.MenuActivity.Companion.KEY_BALANCE
import com.royaly.boaly.utils.showToast
import java.util.*
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback

class GameOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameOneBinding
    private var balance = 0.0
    private var bet = 10.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        balance = intent.getDoubleExtra(KEY_BALANCE, 0.0)

        update()
        initListener()
        onBack()
    }

    private fun onBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(
                    RESULT_OK, Intent().putExtra(
                        MenuActivity.KEY_NEW_BALANCE, balance
                    )
                )
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
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
        binding.tvRate.text = getString(R.string.rate, bet.toInt())
    }

    private fun animateSpinner(view: ImageView, icons: List<Int>, delay: Long) {
        val duration = 350L

        val animator = ValueAnimator.ofFloat(0f, view.height.toFloat() * icons.size).apply {
            this.duration = duration
            startDelay = delay
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val value = it.animatedValue as Float
                view.translationY = value % view.height
            }
        }

        view.tag = 0
        view.setImageResource(icons.random())

        animator.start()

        val timer: Timer = Timer().also {
            it.schedule(object : TimerTask() {
                override fun run() {
                    val index = (view.tag as Int + 1) % icons.size
                    view.post { view.setImageResource(icons[index]) }
                    view.tag = index
                }
            }, duration / icons.size, duration / icons.size)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            animator.cancel()
            timer.cancel()
            view.clearAnimation()
            view.translationY = 0f
        }, 2000L)

    }

    private fun spinSlots() {

        val icons = listOf(
            R.drawable.ic_game1_1, R.drawable.ic_game1_2, R.drawable.ic_game1_3,
            R.drawable.ic_game1_4, R.drawable.ic_game1_5, R.drawable.ic_game1_6,
            R.drawable.ic_game1_7, R.drawable.ic_game1_8
        )

        val slot1Icons = mutableListOf<Int>()
        val slot2Icons = mutableListOf<Int>()
        val slot3Icons = mutableListOf<Int>()

        for (i in 0 until 3) {
            slot1Icons.add(icons.random())
            slot2Icons.add(icons.random())
            slot3Icons.add(icons.random())
        }

        animateSpinner(binding.column11, icons, 200L)
        animateSpinner(binding.column12, icons, 200L)
        animateSpinner(binding.column13, icons, 200L)
        animateSpinner(binding.column21, icons, 500L)
        animateSpinner(binding.column22, icons, 500L)
        animateSpinner(binding.column23, icons, 500L)
        animateSpinner(binding.column31, icons, 800L)
        animateSpinner(binding.column32, icons, 800L)
        animateSpinner(binding.column33, icons, 800L)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnPlay.isEnabled = true

            binding.column11.clearAnimation()
            binding.column12.clearAnimation()
            binding.column13.clearAnimation()
            binding.column21.clearAnimation()
            binding.column22.clearAnimation()
            binding.column23.clearAnimation()
            binding.column31.clearAnimation()
            binding.column32.clearAnimation()
            binding.column33.clearAnimation()

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
                winMultiplier =
                    getMultiplier(slot1Icons[0]) * getMultiplier(slot2Icons[1]) *
                            getMultiplier(slot3Icons[2])
            } else if (slot1Icons[2] == slot2Icons[1] && slot2Icons[1] == slot3Icons[0]) {
                winMultiplier =
                    getMultiplier(slot1Icons[2]) * getMultiplier(slot2Icons[1]) *
                            getMultiplier(slot3Icons[0])
            } else if (slot1Icons[0] == slot1Icons[1] && slot1Icons[1] == slot1Icons[2]) {
                winMultiplier = getMultiplier(slot1Icons[0])
            } else if (slot2Icons[0] == slot2Icons[1] && slot2Icons[1] == slot2Icons[2]) {
                winMultiplier = getMultiplier(slot2Icons[0])
            } else if (slot3Icons[0] == slot3Icons[1] && slot3Icons[1] == slot3Icons[2]) {
                winMultiplier = getMultiplier(slot3Icons[0])
            }
            val payout = if (winMultiplier > 0.0) bet * winMultiplier else -bet
            balance += payout.toInt()
            update()

        }, 2000L)


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