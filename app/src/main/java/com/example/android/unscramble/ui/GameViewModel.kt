package com.example.android.unscramble.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.unscramble.data.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(val wordRepository: WordRepository) : ViewModel() {

    // Game UI state (no confundir con el estado de un composable)
    private val _uiState = MutableStateFlow(GameUiState())

    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private suspend fun getAllWords()  {
        val gameWords = wordRepository.getAllWords().first()
        if (!gameWords.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    wordsLoaded = true,
                    gameWords = gameWords
                )
            }
        }
    }

    fun reloadData(){
        viewModelScope.launch{
            getAllWords()
            pickRandomWordAndShuffle()
        }
    }

    private lateinit var currentWord: String

    //Estado del composable
    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set

    //private var gameWords: List<Word> = listOf<Word>()

    init {
        viewModelScope.launch{
            getAllWords()
        }
    }

    fun newWord(){
        pickRandomWordAndShuffle()
        _uiState.update { currentState ->
            currentState.copy(currentScrambledWord = pickRandomWordAndShuffle())
        }
    }

    private fun pickRandomWordAndShuffle(): String {
        //Función recursiva que se llama tantas veces como sea necesario,
        //hasta que una Word elegida random no esté en el set usedWrords.
        //cuando la random no está la añade al set y termina la recursión devolviendo esta palabra.
        // Continue picking up a new random word until you get one that hasn't been used before

        //currentWord = allWords.random()
        if (_uiState.value.gameWords.isEmpty()) {
            currentWord = ""
            return currentWord
        }
        else {
            currentWord = _uiState.value.gameWords.random().word
            if (usedWords.contains(currentWord)) {
                return pickRandomWordAndShuffle()
            } else {
                if (!currentWord.equals("")) {
                    usedWords.add(currentWord)
                }
                return shuffleCurrentWord(currentWord)
            }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    fun resetGame() {
//        viewModelScope.launch{
//            getAllWords()
//        }
        //Limpiamos la lista de palabras ya usadas
         usedWords.clear()
         _uiState.value = GameUiState(
             currentScrambledWord = pickRandomWordAndShuffle()
         )
    }

    fun wordsLoaded() : Boolean{
        return _uiState.value.wordsLoaded
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // User's guess is correct, increase the score
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {

        if (usedWords.size == MAX_NO_OF_WORDS) {
            //Last round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }
        else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc(),
                )
            }
        }
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }


}