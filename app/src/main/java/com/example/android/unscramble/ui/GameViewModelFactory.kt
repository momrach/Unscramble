package com.example.android.unscramble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.unscramble.data.WordRepository
import com.example.android.unscramble.ui.GameViewModel

class GameViewModelFactory(private val wordRepository: WordRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(wordRepository) as T
        }
    throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}