package com.star.geoquiz

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

private const val KEY_CURRENT_INDEX = "currentIndex"

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(Bundle?) called")

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0)
        }

        question_text_view.setOnClickListener {
            next_button.performClick()
        }

        true_button.setOnClickListener {
            getAnswer(true)
        }

        false_button.setOnClickListener {
            getAnswer(false)
        }

        prev_button.setOnClickListener {
            getQuestion(QuizViewModel.Order.PREV)
        }

        next_button.setOnClickListener {
            getQuestion(QuizViewModel.Order.NEXT)
        }

        prev_image_button.setOnClickListener {
            prev_button.performClick()
        }

        next_image_button.setOnClickListener {
            next_button.performClick()
        }

        getQuestion(QuizViewModel.Order.CURRENT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_CURRENT_INDEX, quizViewModel.currentIndex)
    }

    override fun onStart() {
        super.onStart()

        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()

        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy() called")
    }

    private fun getQuestion(order: QuizViewModel.Order) {

        quizViewModel.updateIndex(order)
        updateQuestion()
        updateButton()
    }

    private fun getAnswer(userAnswer: Boolean) {

        quizViewModel.updateAnswer(userAnswer)
        checkAnswer()
        updateButton()
    }

    private fun updateQuestion() {

        val questionTextResId = quizViewModel.currentQuestionTextResId
        question_text_view.setText(questionTextResId)
    }

    private fun updateButton() {

        val currentQuestionAnswered = quizViewModel.currentQuestionAnswered
        val allAnswered = quizViewModel.allAnswered

        true_button.isEnabled = !currentQuestionAnswered
        false_button.isEnabled = !currentQuestionAnswered

        prev_button.isEnabled = !allAnswered
        next_button.isEnabled = !allAnswered
        prev_image_button.isEnabled = !allAnswered
        next_image_button.isEnabled = !allAnswered
    }

    private fun checkAnswer() {

        val messageResId = when (quizViewModel.currentQuestionCorrect) {
            true -> R.string.correct_toast
            false -> R.string.incorrect_toast
        }

        var result = getString(messageResId)

        if (quizViewModel.allAnswered) {
            result += "\n" + "Score: " + String.format("%.2f", quizViewModel.getScore())
        }

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

}
