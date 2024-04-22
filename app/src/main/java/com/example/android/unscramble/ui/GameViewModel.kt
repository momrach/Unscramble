package com.example.android.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE
import com.example.android.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    // Game UI state (no confundir con el estado de un composable)
    private val _uiState = MutableStateFlow(GameUiState())

    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String

    //Estado del composable
    // Set of words used in the game
    // La inicialización se hace con mutableSetOf() para que se pueda modificar pero se inicializa vacío
    private var usedWords: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set // No se puede modificar desde fuera de la clase

    // Inicialización de la clase. init se llama cuando se crea una instancia de la clase
    init {
        resetGame()
    }

    // Función para seleccionar una palabra al azar y barajarla
    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random() //allWords es un set de palabras que se define en WordsData.kt
        if (usedWords.contains(currentWord)) { //Si la palabra ya se ha usado, se vuelve a llamar a la función
            return pickRandomWordAndShuffle()
        } else { //Si la palabra no se ha usado, se añade al set de palabras usadas y se baraja
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    // Función para barajar la palabra. Barajar la palabra es simplemente cambiar el orden de las letras
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        //Si la palabra barajada es igual a la palabra original, se vuelve a barajar
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord) // Devuelve la palabra barajada convertida  a String
    }

    // Función para reiniciar el juego (se llama al principio y al final del juego)
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    // Función para actualizar la palabra que ha introducido el usuario
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    // Función para comprobar si la palabra introducida por el usuario es correcta
    fun checkUserGuess() {
        // Comprueba si la palabra introducida por el usuario es igual a la palabra original
        if (userGuess.equals(currentWord, ignoreCase = true)) { //ignoreCase = true para ignorar mayúsculas y minúsculas
            // User's guess is correct, increase the score
            // Aumenta la puntuación usando una variable intermediaria
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE) //
            updateGameState(updatedScore) //Actualiza el estado del juego
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                //Copia el estado actual y cambia el valor de isGuessedWordWrong a true
                //En Kotlin, las clases de datos (data class) son inmutables por defecto.
                // Esto significa que una vez que se crea una instancia de una clase de datos,
                // no puedes cambiar sus propiedades. En este caso, GameUiState es
                // una clase de datos.
                // La función copy() es una función generada automáticamente en las
                // clases de datos en Kotlin. Esta función crea una nueva instancia
                // de la clase de datos con los mismos valores de las propiedades que
                // la instancia original. Sin embargo, puedes pasar argumentos a la
                // función copy() para cambiar algunos de los valores de las propiedades
                // en la nueva instancia.
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }

    // Función para actualizar el estado del juego
    private fun updateGameState(updatedScore: Int) {
        // Comprueba si se han adivinado todas las palabras
        if (usedWords.size == MAX_NO_OF_WORDS) {
            //Last round in the game
            _uiState.update { currentState ->
                //Copia el estado actual y cambia el valor de isGameOver a true
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }
        else {
            // Continue the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc(), //inc() es una función de Kotlin para incrementar un valor en 1
                )
            }
        }
    }

    // Función para saltar una palabra
    fun skipWord() {
        // Aumenta la puntuación en 0 puntos
        // La función updateGameState no solo se encarga de actualizar la puntuación,
        // sino que también controla el flujo del juego.
        // Se llama a updateGameState para realizar las siguientes acciones:
        // - Comprobar si se han adivinado todas las palabras , si es así,
        // se marca el juego como terminado (isGameOver = true).
        // - Si el juego no ha terminado, se selecciona una nueva palabra
        // se mantiene la puntuación actual y se incrementa el contador de
        // palabras
        // Por lo tanto, aunque la puntuación no se modifique al saltar una palabra,
        // es necesario llamar a updateGameState para mantener el correcto
        // funcionamiento del juego.
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }
}