package com.example.android.unscramble.data

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity(tableName = Word.TABLE_NAME)
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @NotNull
    val word: String
){
    companion object {
        const val TABLE_NAME = "words"
    }
}


