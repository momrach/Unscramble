package com.example.android.unscramble.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    fun getAll(): Flow<List<Word>>

    //Metodo insertAll para insertar multiples palabras de una vez
    @Insert
    fun insertAll(vararg words: Word)

    //Metodo insert para insertar una palabra a la vez
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    //Metodo delete para borrar una palabra a la vez
    @Delete
    fun delete(vararg word: Word)

    //Medoto update para actualizar varias palabaras a la vez
    //cada Word tiene el id que no cambia y actualiza la columna word con la Word.word)
    @Update
    fun update(vararg word: Word)
}

