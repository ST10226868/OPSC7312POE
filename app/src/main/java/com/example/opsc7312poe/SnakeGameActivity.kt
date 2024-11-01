package com.example.opsc7312poe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.opsc7312poe.utils.HighScoreManager
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class SnakeGameActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snake_game)

        val board = findViewById<RelativeLayout>(R.id.board)
        val border = findViewById<RelativeLayout>(R.id.relativeLayout)
        val lilu = findViewById<LinearLayout>(R.id.lilu)
        val upButton = findViewById<ImageButton>(R.id.up)
        val downButton = findViewById<ImageButton>(R.id.down)
        val leftButton = findViewById<ImageButton>(R.id.left)
        val rightButton = findViewById<ImageButton>(R.id.right)
        val pauseButton = findViewById<ImageButton>(R.id.pause)
        val newGame = findViewById<Button>(R.id.new_game)
        val resume = findViewById<Button>(R.id.resume)
        val backButton = findViewById<Button>(R.id.backButton)
        val playAgain = findViewById<Button>(R.id.playagain)
        val score = findViewById<Button>(R.id.score)
        val score2 = findViewById<Button>(R.id.score2)
        val meat = ImageView(this)
        val snake = ImageView(this)
        val snakeSegments = mutableListOf(snake)
        val handler = Handler()
        var delayMillis = 30L
        var currentDirection = "right"
        var scoreCount = 0

        HighScoreManager.init(this)

        backButton.setOnClickListener {
            val intent = Intent(this, SnakeHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

            // Initialize views
        board.visibility = View.INVISIBLE
        playAgain.visibility = View.INVISIBLE
        score.visibility = View.INVISIBLE
        score2.visibility = View.INVISIBLE


        newGame.setOnClickListener {
            board.visibility = View.VISIBLE
            newGame.visibility = View.INVISIBLE
            resume.visibility = View.INVISIBLE
            score2.visibility = View.VISIBLE
            backButton.visibility = View.INVISIBLE

            // Initialize snake
            snake.setImageResource(R.drawable.snake)
            snake.layoutParams = ViewGroup.LayoutParams(50, 50)
            board.addView(snake)
            snakeSegments.clear()
            snakeSegments.add(snake)

            // Initialize position variables
            var snakeX = (board.width / 2).toFloat()
            var snakeY = (board.height / 2).toFloat()
            snake.x = snakeX
            snake.y = snakeY

            // Initialize food
            meat.setImageResource(R.drawable.meat)
            meat.layoutParams = ViewGroup.LayoutParams(50, 50)
            board.addView(meat)

            val random = Random()

            fun randomizeMeatPosition() {
                val padding = 70 // Padding from edges
                val randomX = padding + random.nextInt(board.width - 2 * padding)
                val randomY = padding + random.nextInt(board.height - 2 * padding)
                meat.x = randomX.toFloat()
                meat.y = randomY.toFloat()
            }

            board.post {
                randomizeMeatPosition()
            }

            fun checkFoodCollision() {
                val distance = sqrt(
                    (snake.x - meat.x).pow(2) +
                            (snake.y - meat.y).pow(2)
                )

                if (distance < snake.width) {
                    val newSnakeSegment = ImageView(this)
                    newSnakeSegment.setImageResource(R.drawable.snake)
                    newSnakeSegment.layoutParams = ViewGroup.LayoutParams(50, 50)
                    board.addView(newSnakeSegment)
                    snakeSegments.add(newSnakeSegment)

                    randomizeMeatPosition()
                    delayMillis = (delayMillis * 0.95).toLong()
                    scoreCount++
                    score2.text = "Score: $scoreCount"
                }
            }

            fun checkBorderCollision(x: Float, y: Float): Boolean {
                // Check exact border collision
                return x < 0 || // Left border
                        y < 0 || // Top border
                        x + snake.layoutParams.width > board.width || // Right border
                        y + snake.layoutParams.height > board.height  // Bottom border
            }

            fun endGame() {
                HighScoreManager.setHighScore(this, scoreCount)
                border.setBackgroundColor(getResources().getColor(R.color.red))
                playAgain.visibility = View.VISIBLE
                currentDirection = "pause"
                lilu.visibility = View.INVISIBLE
                score.text = "Your score is $scoreCount | High Score: ${HighScoreManager.getHighScore()}"
                score.visibility = View.VISIBLE
                score2.visibility = View.INVISIBLE
                HighScoreManager.syncHighScoreWithFirebaseIfNeeded(this)
            }

            val runnable = object : Runnable {
                override fun run() {
                    // Update tail segments
                    for (i in snakeSegments.size - 1 downTo 1) {
                        snakeSegments[i].x = snakeSegments[i - 1].x
                        snakeSegments[i].y = snakeSegments[i - 1].y
                    }

                    val moveAmount = 10f
                    var newX = snakeX
                    var newY = snakeY

                    // Calculate new position based on direction
                    when (currentDirection) {
                        "up" -> newY -= moveAmount
                        "down" -> newY += moveAmount
                        "left" -> newX -= moveAmount
                        "right" -> newX += moveAmount
                    }

                    // Only end game if new position would result in collision
                    if (checkBorderCollision(newX, newY)) {
                        endGame()
                    } else if (currentDirection != "pause") {
                        // Update position if no collision and not paused
                        snakeX = newX
                        snakeY = newY
                        snake.x = snakeX
                        snake.y = snakeY
                        checkFoodCollision()
                    }

                    handler.postDelayed(this, delayMillis)
                }
            }

            handler.postDelayed(runnable, delayMillis)

            // Direction controls
            upButton.setOnClickListener {
                if (currentDirection != "down") currentDirection = "up"
            }
            downButton.setOnClickListener {
                if (currentDirection != "up") currentDirection = "down"
            }
            leftButton.setOnClickListener {
                if (currentDirection != "right") currentDirection = "left"
            }
            rightButton.setOnClickListener {
                if (currentDirection != "left") currentDirection = "right"
            }
            pauseButton.setOnClickListener {
                currentDirection = "pause"
                board.visibility = View.INVISIBLE
                newGame.visibility = View.INVISIBLE
                resume.visibility = View.VISIBLE
                backButton.visibility = View.VISIBLE
            }
            resume.setOnClickListener {
                currentDirection = "pause"
                board.visibility = View.VISIBLE
                newGame.visibility = View.INVISIBLE
                resume.visibility = View.INVISIBLE
                backButton.visibility = View.INVISIBLE
            }
            playAgain.setOnClickListener {
                recreate()
            }
        }
    }
}
