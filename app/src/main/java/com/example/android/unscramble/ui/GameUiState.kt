package com.example.android.unscramble.ui

import com.example.android.unscramble.data.Word

data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val score: Int = 0,
    val currentWordCount: Int = 0,
    val isGameOver: Boolean = false,
    val wordsLoaded : Boolean = false,
    val gameWords : List<Word> = listOf()
)