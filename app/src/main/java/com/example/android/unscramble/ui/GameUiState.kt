package com.example.android.unscramble.ui

// Estado del juego que se compone de
// la palabra barajada,
// si la palabra adivinada es incorrecta,
// la puntuación,
// el número de palabras adivinadas
// y si el juego ha terminado
data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val score: Int = 0,
    val currentWordCount: Int = 0,
    val isGameOver: Boolean = false
)