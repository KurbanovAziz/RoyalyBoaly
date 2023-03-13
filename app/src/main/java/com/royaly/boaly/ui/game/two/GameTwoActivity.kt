package com.royaly.boaly.ui.game.two

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.royaly.boaly.R
import com.royaly.boaly.databinding.ActivityGameTwoBinding
import com.royaly.boaly.ui.game.two.viewmodel.GameTwoViewModel
import com.royaly.boaly.ui.menu.MenuActivity.Companion.KEY_BALANCE
import com.royaly.boaly.ui.menu.MenuActivity.Companion.KEY_NEW_BALANCE
import com.royaly.boaly.utils.showToast
import java.util.*

class GameTwoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameTwoBinding
    private lateinit var viewModel: GameTwoViewModel
    private var animationInProgress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[GameTwoViewModel::class.java]
        viewModel.balance = intent.getDoubleExtra(KEY_BALANCE, 0.0)
        update()
        initListener()
        onBack()
        savedInstanceState?.let { savedState ->
            viewModel.balance = savedState.getDouble(KEY_BALANCE, 0.0)
            update()
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(KEY_BALANCE, viewModel.balance)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.balance = savedInstanceState.getDouble(KEY_BALANCE, 0.0)
        update()
    }

    private fun onBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(
                    RESULT_OK, Intent().putExtra(
                        KEY_NEW_BALANCE, viewModel.balance
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
                    KEY_NEW_BALANCE, viewModel.balance
                )
            )
            finish()
        }
        binding.btnPlay.setOnClickListener {
            if (animationInProgress) {
                showToast(getString(R.string.wait_for_completion))
            } else if (viewModel.balance > 0 && viewModel.bet <= viewModel.balance) {
                spinSlots()
            } else {
                showToast(getString(R.string.not_enough_money))
            }
        }
        binding.btnBetPlus.setOnClickListener {
            if (viewModel.bet < viewModel.balance) {
                viewModel.bet += 10
            }
            update()
        }
        binding.btnBetPlus.setOnLongClickListener {
            viewModel.bet = viewModel.balance
            return@setOnLongClickListener false
        }
        binding.btnBetMinus.setOnLongClickListener {
            if (viewModel.bet != 10.0) {
                viewModel.bet = 10.0
            }
            return@setOnLongClickListener false
        }
        binding.btnBetMinus.setOnClickListener {
            if (viewModel.bet != 10.0) {
                viewModel.bet -= 10
            }
            update()
        }
    }

    private fun update() {
        binding.tvBalance.text = getString(R.string.balance, viewModel.balance.toInt())
        binding.tvRate.text = getString(R.string.rate, viewModel.bet.toInt())
        binding.tvWin.text = getString(R.string.win, viewModel.win.toInt())
    }

    private fun animateSpinner(view: ImageView, icons: List<Int>, delay: Long) {
        animationInProgress = true
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
            animationInProgress = false
            animator.cancel()
            timer.cancel()
            view.clearAnimation()
            view.translationY = 0f
        }, 2040L)

    }


    private fun spinSlots() {
        val icons = listOf(
            R.drawable.ic_game2_1, R.drawable.ic_game2_2, R.drawable.ic_game2_3,
            R.drawable.ic_game2_4, R.drawable.ic_game2_5, R.drawable.ic_game2_6,
            R.drawable.ic_game2_7
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
        animateSpinner(binding.column21, icons, 300L)
        animateSpinner(binding.column22, icons, 300L)
        animateSpinner(binding.column23, icons, 300L)
        animateSpinner(binding.column31, icons, 400L)
        animateSpinner(binding.column32, icons, 400L)
        animateSpinner(binding.column33, icons, 400L)

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

            checkWin(slot1Icons, slot2Icons, slot3Icons)

        }, 2040L)

    }

    private fun checkWin(slot1Icons: List<Int>, slot2Icons: List<Int>, slot3Icons: List<Int>) {
        var winMultiplier = 0.0

        if (slot1Icons[0] == slot2Icons[0] && slot2Icons[0] == slot3Icons[0]) {
            winMultiplier = getMultiplier(slot1Icons[0])
        }
        if (slot1Icons[1] == slot2Icons[1] && slot2Icons[1] == slot3Icons[1]) {
            winMultiplier = getMultiplier(slot1Icons[1])
        }
        if (slot1Icons[2] == slot2Icons[2] && slot2Icons[2] == slot3Icons[2]) {
            winMultiplier = getMultiplier(slot1Icons[2])
        }
        if (slot1Icons[0] == slot2Icons[1] && slot2Icons[1] == slot3Icons[2]) {
            winMultiplier =
                getMultiplier(slot1Icons[0]) * getMultiplier(slot2Icons[1]) *
                        getMultiplier(slot3Icons[2])
        }
        if (slot1Icons[2] == slot2Icons[1] && slot2Icons[1] == slot3Icons[0]) {
            winMultiplier =
                getMultiplier(slot1Icons[2]) * getMultiplier(slot2Icons[1]) *
                        getMultiplier(slot3Icons[0])
        }
        if (slot1Icons[0] == slot1Icons[1] && slot1Icons[1] == slot1Icons[2]) {
            winMultiplier = getMultiplier(slot1Icons[0])
        }
        if (slot2Icons[0] == slot2Icons[1] && slot2Icons[1] == slot2Icons[2]) {
            winMultiplier = getMultiplier(slot2Icons[0])
        }
        if (slot3Icons[0] == slot3Icons[1] && slot3Icons[1] == slot3Icons[2]) {
            winMultiplier = getMultiplier(slot3Icons[0])
        }
        val payout = if (winMultiplier > 0.0) viewModel.bet * winMultiplier else -viewModel.bet
        viewModel.balance += payout.toInt()
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