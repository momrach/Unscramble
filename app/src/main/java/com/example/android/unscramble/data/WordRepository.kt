package com.example.android.unscramble.data

import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDatabase: WordDatabase) {

    fun insertWord(word: Word) {
        wordDatabase.wordDao().insert(word)
    }

    fun getAllWords(): Flow<List<Word>> {
        return wordDatabase.wordDao().getAll()
    }


}