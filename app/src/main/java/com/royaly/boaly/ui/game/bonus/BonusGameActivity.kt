package com.royaly.boaly.ui.game.bonus

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.royaly.boaly.R
import com.royaly.boaly.data.Pref
import com.royaly.boaly.databinding.ActivityBonusGameBinding
import com.royaly.boaly.ui.menu.MenuActivity
import com.royaly.boaly.utils.showToast
import kotlin.random.Random

class BonusGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBonusGameBinding

    private lateinit var pref : Pref

    private val scoreIcons = listOf(
        R.drawable.ic_score_0, R.drawable.ic_score_150, R.drawable.ic_score_200
    )

    private val defaultIcon = R.drawable.ic_bonus_game

    private val gameGrid = mutableListOf<ImageView>()

    private var score = 0

    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBonusGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = Pref(this)
        initListener()
        gameGrid()
        onBack()
        score = pref.getScore()
    }


    private fun onBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                pref.saveScore(score)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initListener() {
        binding.tvScore.text = pref.getScore().toString()

        binding.btnBack.setOnClickListener {
            pref.saveScore(score)
            finish()
        }
    }

    private fun gameGrid() {
        gameGrid.apply {
            add(binding.cell11)
            add(binding.cell12)
            add(binding.cell13)
            add(binding.cell21)
            add(binding.cell22)
            add(binding.cell23)
            add(binding.cell31)
            add(binding.cell32)
            add(binding.cell33)
        }

        gameGrid.forEach { imageView ->
            imageView.setOnClickListener {
                if (!gameOver && imageView.tag == null) {
                    val iconIndex = Random.nextInt(scoreIcons.size)
                    imageView.setImageResource(scoreIcons[iconIndex])
                    imageView.tag = scoreIcons[iconIndex]
                    updateScore(iconIndex)
                }
            }
        }
    }

    private fun updateScore(iconIndex: Int) {
        when (iconIndex) {
            0 -> {
                gameOver = true
                showToast(getString(R.string.game_over))
                gameGrid.forEach { imageView ->
                    imageView.setImageResource(defaultIcon)
                    imageView.tag = null
                }
                resetGame()
            }
            1 -> {
                score += 150
                binding.tvScore.text = score.toString()
            }
            else -> {
                score += 200
                binding.tvScore.text = score.toString()
            }
        }
        if (isGameOver()) {
            gameOver = true
            restartGame()
        }
    }

    private fun isGameOver(): Boolean {
        return gameGrid.all { imageView -> imageView.tag != null }
    }

    private fun restartGame() {
        gameGrid.forEach { imageView ->
            imageView.setImageResource(defaultIcon)
            imageView.tag = null
        }
        gameOver = false
    }

    private fun resetGame() {
        score = 0
        binding.tvScore.text = score.toString()
        gameGrid.forEach { imageView ->
            imageView.setImageResource(defaultIcon)
            imageView.tag = null
        }
        gameOver = false
    }
}
