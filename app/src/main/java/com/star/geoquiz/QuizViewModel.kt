package com.star.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var allAnswered = false

    val currentQuestionTextResId: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].answered
    val currentQuestionCorrect: Boolean
        get() = questionBank[currentIndex].correct

    fun updateIndex(order: Order) {

        while (true) {
            currentIndex = when (order) {
                Order.PREV -> (currentIndex + questionBank.size - 1) % questionBank.size
                Order.CURRENT -> currentIndex
                Order.NEXT -> (currentIndex + 1) % questionBank.size
            }

            if ((order == Order.CURRENT) || (!questionBank[currentIndex].answered)) {
                break
            }
        }
    }

    fun updateAnswer(userAnswer: Boolean) {

        val correctAnswer = questionBank[currentIndex].answer

        questionBank[currentIndex].answered = true
        questionBank[currentIndex].correct = (userAnswer == correctAnswer)
        allAnswered = questionBank.all { element -> element.answered }
    }

    fun getScore(): Double {

        val correctAnswers = questionBank.count { element -> element.correct }

        return correctAnswers.toDouble() / questionBank.size * 100
    }

    enum class Order {
        PREV, CURRENT, NEXT
    }

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()

        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}