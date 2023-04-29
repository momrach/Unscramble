package com.example.android.unscramble.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.android.unscramble.MyApplication
import com.example.android.unscramble.data.WordRepository

class GameViewModelFactory(private val wordRepository: WordRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        // Get the Application object from extras
        val application = checkNotNull(extras[APPLICATION_KEY])

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel((application as MyApplication).container.wordRepository) as T
        }
    throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}